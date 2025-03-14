package com.kawa.abn.feature.list.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.paging.map
import androidx.room.withTransaction
import com.kawa.abn.feature.list.data.datasource.GitHubApiService
import com.kawa.abn.feature.list.data.mapper.ReposDtoToRepositoriesListMapper
import com.kawa.abn.feature.list.data.mapper.RepositoryEntityToRepositoriesListMapper
import com.kawa.abn.feature.list.domain.entity.RepositoryItem
import com.kawa.abn.feature.list.domain.repository.ListRepository
import com.kawa.abn.foundation.database.AppDatabase
import com.kawa.abn.foundation.database.dao.RepositoriesDao
import com.kawa.abn.foundation.database.entity.RepositoryEntity
import com.kawa.abn.foundation.kotlin.ResultOf
import com.kawa.abn.foundation.kotlin.error
import com.kawa.abn.foundation.kotlin.fold
import com.kawa.abn.foundation.kotlin.map
import com.kawa.abn.foundation.kotlin.success
import com.kawa.abn.foundation.kotlin.toResultOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

const val PAGE_SIZE = 10

class ListRepositoryImpl @Inject constructor(
    private val repoDao: RepositoriesDao,
    private val repoRemoteMediator: RepoRemoteMediator,
    private val mapper: RepositoryEntityToRepositoriesListMapper
) : ListRepository {

    override suspend fun getRepo(id: Int): ResultOf<RepositoryItem, Throwable> {

        return try {
            withContext(Dispatchers.IO) {
                mapper.map(repoDao.getRepo(id)).success()
            }
        } catch (e: Exception) {
            e.error()
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPaginatedRepos(): Flow<PagingData<RepositoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
            ),
            remoteMediator = repoRemoteMediator,
            pagingSourceFactory = { repoDao.pagingSource() },
        ).flow.map { pagingData ->
            pagingData.map { entity -> mapper.map(entity) }
        }
    }
}

@OptIn(ExperimentalPagingApi::class)
@Singleton
class RepoRemoteMediator @Inject constructor(
    private val apiService: GitHubApiService,
    private val db: AppDatabase,
    private val repoDao: RepositoriesDao,
    private val reposDtoToRepositoriesListMapper: ReposDtoToRepositoriesListMapper,
    private val repositoryEntityToRepositoriesListMapper: RepositoryEntityToRepositoriesListMapper
) : RemoteMediator<Int, RepositoryEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RepositoryEntity>
    ): MediatorResult {
        try {
            Timber.d("$loadType")
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    val nextKey = lastItem.page + 1
                    Timber.d("APPEND page: $nextKey")
                    nextKey
                }
            }

            return apiService.getRepos(page, state.config.pageSize).toResultOf()
                .map { reposDtoToRepositoriesListMapper.map(it, page) }
                .map { repositoryEntityToRepositoriesListMapper.map(it) }
                .fold(
                    onSuccess = { repoEntities ->
                        db.withTransaction {
                            if (loadType == LoadType.REFRESH) {
                                repoDao.clearAll()
                                Timber.d("Clean database")
                            }
                            repoDao.insertAll(repoEntities)
                            Timber.d("Insert database page: $page: $repoEntities")
                        }
                        val endReached = repoEntities.size < PAGE_SIZE
                        if (endReached) {
                            Timber.d("End page $page is reached")
                        }
                        MediatorResult.Success(endOfPaginationReached = endReached)
                    },
                    onError = { MediatorResult.Error(it) }
                )
        } catch (e: Exception) {
            e.printStackTrace()
            return MediatorResult.Error(e)
        }
    }

}
