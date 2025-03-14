package com.kawa.abn.feature.list.data.entitry

import kotlinx.serialization.SerialName

data class RepoDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("description") val description: String?,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("visibility") val visibility: String,
    @SerialName("private") val isPrivate: Boolean,
    @SerialName("owner") val owner: OwnerDto
)

data class OwnerDto(
    @SerialName("login") val login: String,
    @SerialName("avatar_url") val avatarUrl: String
)
