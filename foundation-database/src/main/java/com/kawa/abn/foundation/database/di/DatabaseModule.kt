package com.kawa.abn.foundation.database.di

import android.content.Context
import androidx.room.Room
import com.kawa.abn.foundation.database.AppDatabase
import com.kawa.abn.foundation.database.dao.RepositoriesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext applicationContext: Context): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "abn-repos"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRepoDao(db: AppDatabase): RepositoriesDao {
        return db.repoDao()
    }
}
