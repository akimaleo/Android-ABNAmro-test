package com.kawa.abn.feature.list.data.datasource

import com.kawa.abn.feature.list.data.entitry.RepoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApiService {
    @GET("users/abnamrocoesd/repos")
    suspend fun getRepos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Result<List<RepoDto>>
}
