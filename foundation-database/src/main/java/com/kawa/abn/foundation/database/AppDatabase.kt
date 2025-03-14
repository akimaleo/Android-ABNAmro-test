package com.kawa.abn.foundation.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kawa.abn.foundation.database.dao.RepositoriesDao
import com.kawa.abn.foundation.database.entity.RepositoryEntity

@Database(entities = [RepositoryEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDao(): RepositoriesDao
}
