package com.example.anilist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.anilist.data.local.dao.FavoriteDao
import com.example.anilist.data.local.model.FavoriteEntity
import com.example.anilist.util.AiringScheduleConverter
import com.example.anilist.util.DateConverter
import com.example.anilist.util.StringListConverter

@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class, StringListConverter::class, AiringScheduleConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}