package com.kawa.abn.feature.list.domain.repository

import androidx.paging.PagingData
import com.kawa.abn.feature.list.domain.entity.RepositoryItem
import com.kawa.abn.foundation.kotlin.ResultOf
import kotlinx.coroutines.flow.Flow

interface ListRepository {
    suspend fun getRepo(id: Int): ResultOf<RepositoryItem, Throwable>
    fun getPaginatedRepos(): Flow<PagingData<RepositoryItem>>
}
