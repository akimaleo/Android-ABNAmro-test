package com.kawa.abn.feature.list.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.kawa.abn.feature.list.data.datasource.GitHubApiService
import com.kawa.abn.feature.list.data.mapper.ReposDtoToRepositoriesListMapper
import com.kawa.abn.feature.list.data.mapper.RepositoryEntityToRepositoriesListMapper
import com.kawa.abn.foundation.database.AppDatabase
import com.kawa.abn.foundation.database.dao.RepositoriesDao
import com.kawa.abn.foundation.database.entity.RepositoryEntity
import com.kawa.abn.foundation.kotlin.fold
import com.kawa.abn.foundation.kotlin.map
import com.kawa.abn.foundation.kotlin.toResultOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

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
                    // FIXME: Paging library has a known issue where state.lastItemOrNull()
                    //  returns incorrect value. It can be fixed by reading the data directly from DB
                    //  insuring that we handle current state correctly
                    //  https://issuetracker.google.com/issues/381126154
                    //  val lastItem = state.lastItemOrNull()
                    val lastItem = withContext(Dispatchers.IO) {
                        repoDao.getAllRepos().lastOrNull()
                    }
                    if (lastItem == null) {
                        Timber.d("APPEND lastItem is null")
                        return MediatorResult.Success(endOfPaginationReached = false)
                    }
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
