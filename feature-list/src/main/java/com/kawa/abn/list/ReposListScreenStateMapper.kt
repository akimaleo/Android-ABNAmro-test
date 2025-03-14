package com.kawa.abn.list

import com.kawa.abn.feature.list.domain.entity.RepositoryItem
import javax.inject.Inject

class ReposListScreenStateMapper @Inject constructor() {

    fun map(data: RepositoryItem): RepoViewData = data.toViewData()

    private fun RepositoryItem.toViewData() = RepoViewData(
        id = id,
        title = name,
        imageUrl = avatarUrl,
        visibility = visibility.toString(),
        isPrivate = isPrivate,
        language = language,
        page = page,
    )
}
