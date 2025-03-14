package com.kawa.abn.feature.list.data.di

import com.kawa.abn.feature.list.data.datasource.GitHubApiService
import com.kawa.abn.feature.list.data.repository.ListRepositoryImpl
import com.kawa.abn.feature.list.domain.repository.ListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoriesListModule {
    @Binds
    @Singleton
    fun bindListRepository(impl: ListRepositoryImpl): ListRepository

    companion object {
        @Provides
        fun provideGitHubApiService(retrofit: Retrofit): GitHubApiService {
            return retrofit.create(GitHubApiService::class.java)
        }
    }
}
