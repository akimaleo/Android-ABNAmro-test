package com.kawa.abn.feature.list.data.mapper

import com.kawa.abn.feature.list.domain.entity.RepositoryItem
import com.kawa.abn.feature.list.domain.entity.toVisibilityEnum
import com.kawa.abn.foundation.database.entity.RepositoryEntity
import javax.inject.Inject

class RepositoryEntityToRepositoriesListMapper @Inject constructor() {

    fun map(entity: RepositoryEntity): RepositoryItem = entity.toRepositoryItem()

    fun map(item: RepositoryItem): RepositoryEntity = item.toRepositoryEntity()

    fun map(list: List<RepositoryItem>): List<RepositoryEntity> =
        list.map { it.toRepositoryEntity() }

    fun RepositoryEntity.toRepositoryItem() = RepositoryItem(
        id = id,
        name = name,
        avatarUrl = ownerAvatar,
        language = language,
        isPrivate = isPrivate,
        visibility = visibility.toVisibilityEnum(), // Ensuring visibility conversion
        description = description,
        fullName = fullName,
        htmlUrl = htmlUrl,
        page = page,
    )

    fun RepositoryItem.toRepositoryEntity() = RepositoryEntity(
        id = id,
        name = name,
        language = language,
        isPrivate = isPrivate,
        visibility = visibility.name.lowercase(),
        ownerAvatar = avatarUrl,
        description = description,
        fullName = fullName,
        htmlUrl = htmlUrl,
        page = page,
    )
}
