package com.kawa.abn.feature.list.domain.entity

data class RepositoriesList(
    val source: DataSource,
    val list: List<RepositoryItem>
)

data class RepositoryItem(
    val id: Int,
    val name: String,
    val fullName: String,
    val description: String?,
    val avatarUrl: String,
    val htmlUrl: String,
    val language: String?,
    val isPrivate: Boolean,
    val visibility: Visibility,
    val page: Int,
)

enum class DataSource { Cached, Live }
enum class Visibility { Private, Public, Unknown }

fun String.toVisibilityEnum() = when (this.lowercase()) {
    "private" -> Visibility.Private
    "public" -> Visibility.Public
    else -> Visibility.Unknown
}
