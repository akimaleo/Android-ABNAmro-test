package com.kawa.abn.feature.list.domain.usecase

import com.kawa.abn.feature.list.domain.repository.ListRepository
import javax.inject.Inject

class GetListOfReposUseCase @Inject constructor(
    private val listRepository: ListRepository,
) {

    operator fun invoke() = listRepository.getPaginatedRepos()
}
