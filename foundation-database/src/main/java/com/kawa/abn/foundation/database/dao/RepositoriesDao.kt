package com.kawa.abn.foundation.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kawa.abn.foundation.database.entity.RepositoryEntity

@Dao
interface RepositoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<RepositoryEntity>)

    @Query("SELECT * FROM repos ORDER BY page ASC")
    fun pagingSource(): PagingSource<Int, RepositoryEntity>

    @Query("SELECT * FROM repos WHERE id = :id")
    fun getRepo(id: Int): RepositoryEntity

    @Query("SELECT * FROM repos ORDER BY page ASC")
    fun getAllRepos(): List<RepositoryEntity>

    @Query("DELETE FROM repos")
    suspend fun clearAll()
}
