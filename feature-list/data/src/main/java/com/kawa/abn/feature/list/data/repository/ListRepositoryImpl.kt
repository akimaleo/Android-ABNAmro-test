package com.kawa.abn.feature.list.data.repository

import com.kawa.abn.feature.list.data.datasoruce.GitHubApiService
import javax.inject.Inject

class ListRepositoryImpl @Inject constructor(
    private val apiService: GitHubApiService,
) {
    fun get(page: Int, pageSize: Int): List<String> {
        return listOf()
    }
}
