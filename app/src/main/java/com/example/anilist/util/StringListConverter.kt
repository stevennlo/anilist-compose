package com.example.anilist.util

import androidx.room.TypeConverter
import com.google.gson.Gson

class StringListConverter {

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.let { Gson().toJson(value) }
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.let { Gson().fromJson(value, Array<String>::class.java).toList() }
    }
}
