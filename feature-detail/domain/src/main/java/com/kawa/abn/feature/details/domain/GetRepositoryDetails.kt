package com.kawa.abn.feature.details.domain

import com.kawa.abn.feature.list.domain.entity.RepositoryItem
import com.kawa.abn.feature.list.domain.repository.ListRepository
import com.kawa.abn.foundation.kotlin.ResultOf
import javax.inject.Inject

class GetRepositoryDetails @Inject constructor(
    private val listRepository: ListRepository
) {
    suspend operator fun invoke(id: Int): ResultOf<RepositoryItem, Throwable> =
        listRepository.getRepo(id)
}
