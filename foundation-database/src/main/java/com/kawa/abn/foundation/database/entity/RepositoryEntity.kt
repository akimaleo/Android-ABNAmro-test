package com.kawa.abn.foundation.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repos")
data class RepositoryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val fullName: String,
    val description: String?,
    val htmlUrl: String,
    val visibility: String,
    val isPrivate: Boolean,
    val ownerAvatar: String,
    val language: String?,
    val page:Int,
)
