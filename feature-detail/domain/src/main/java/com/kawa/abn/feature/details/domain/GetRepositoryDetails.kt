package com.kawa.abn.feature.details.domain

import com.kawa.abn.feature.list.domain.repository.ListRepository
import javax.inject.Inject

class GetRepositoryDetails @Inject constructor(
    private val listRepository: ListRepository
) {
    suspend operator fun invoke(id: Int) = listRepository.getRepo(id)
}
