package com.kawa.abn.foundation.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kawa.abn.foundation.network.BuildConfig
import com.kawa.abn.foundation.network.adapter.ResultCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BaseUrl

    @Provides
    @BaseUrl
    fun provideBaseUrl(): String = "https://api.github.com/"

    @Provides
    fun provideRetrofit(
        @BaseUrl url: String,
    ): Retrofit {
        val logger = HttpLoggingInterceptor()
        logger.setLevel(HttpLoggingInterceptor.Level.BASIC)

        val contentType = "application/json".toMediaType()
        val networkJson = Json { ignoreUnknownKeys = true }
        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${BuildConfig.GITHUB_API_KEY}")
                    .build()
                chain.proceed(request)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(networkJson.asConverterFactory(contentType))
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .build()
    }

}
