package com.example.anilist.data.local.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.anilist.data.local.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite ORDER BY popularity DESC LIMIT :limit")
    fun getAllPopularAndLimit(limit: Int): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg favorite: FavoriteEntity)

    @Update
    fun update(vararg favorite: FavoriteEntity)

    @Query("DELETE FROM favorite WHERE id = :id")
    fun delete(id: Int)

    @RawQuery(observedEntities = [FavoriteEntity::class])
    fun getAllFiltered(query: SupportSQLiteQuery): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorite WHERE id = :id")
    fun getById(id: Int): FavoriteEntity?
}