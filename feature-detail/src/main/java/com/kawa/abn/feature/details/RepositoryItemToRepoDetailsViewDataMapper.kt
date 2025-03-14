package com.kawa.abn.feature.details

import com.kawa.abn.feature.list.domain.entity.RepositoryItem
import javax.inject.Inject

class RepositoryItemToRepoDetailsViewDataMapper @Inject constructor() {

    fun map(repositoryItem: RepositoryItem): DetailsScreenState {
        return DetailsScreenState.Success(
            RepoDetailsViewData(
                title = repositoryItem.name,
                fullName = repositoryItem.fullName,
                description = repositoryItem.description,
                ownerImageUrl = repositoryItem.avatarUrl,
                visibility = repositoryItem.visibility.toString(),
                isPrivate = repositoryItem.isPrivate,
                htmlUrl = repositoryItem.htmlUrl
            )
        )
    }

    fun map(throwable: Throwable): DetailsScreenState {
        return DetailsScreenState.Error(throwable.message ?: "Error message wasn't provided")
    }

}
