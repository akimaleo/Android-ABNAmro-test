package com.kawa.abn.foundation.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
//    https://api.github.com/users/abnamrocoesd/repos?page=1&per_page=10
    @Provides
    fun provideRetrofit() :Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .build()
    }
}
