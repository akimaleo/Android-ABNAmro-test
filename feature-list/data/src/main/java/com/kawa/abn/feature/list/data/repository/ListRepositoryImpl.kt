package com.kawa.abn.feature.list.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.kawa.abn.feature.list.data.mapper.RepositoryEntityToRepositoriesListMapper
import com.kawa.abn.feature.list.domain.entity.RepositoryItem
import com.kawa.abn.feature.list.domain.repository.ListRepository
import com.kawa.abn.foundation.database.dao.RepositoriesDao
import com.kawa.abn.foundation.kotlin.ResultOf
import com.kawa.abn.foundation.kotlin.error
import com.kawa.abn.foundation.kotlin.success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

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
