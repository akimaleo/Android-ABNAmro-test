package com.kawa.abn.feature.list.data.mapper

import com.kawa.abn.feature.list.data.entitry.RepoDto
import com.kawa.abn.feature.list.domain.entity.RepositoryItem
import com.kawa.abn.feature.list.domain.entity.toVisibilityEnum
import javax.inject.Inject

class ReposDtoToRepositoriesListMapper @Inject constructor() {

    fun map(repo: RepoDto,page:Int): RepositoryItem = repo.toRepositoryItem(page)

    fun map(list: List<RepoDto>, page:Int): List<RepositoryItem> = list.map { it.toRepositoryItem(page) }

    private fun RepoDto.toRepositoryItem(page:Int) = RepositoryItem(
        id = id,
        name = name,
        avatarUrl = owner.avatarUrl,
        language = language,
        isPrivate = isPrivate,
        visibility = visibility.toVisibilityEnum(),
        description = description,
        fullName = fullName,
        htmlUrl = htmlUrl,
        page = page,
    )
}
