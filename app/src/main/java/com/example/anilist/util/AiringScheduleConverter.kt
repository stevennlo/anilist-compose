package com.example.anilist.util

import androidx.room.TypeConverter
import com.example.anilist.model.AiringSchedule
import com.google.gson.Gson

class AiringScheduleConverter {

    @TypeConverter
    fun fromAiringEpisode(value: AiringSchedule?): String? {
        return value?.let { Gson().toJson(value) }
    }

    @TypeConverter
    fun toAiringEpisode(value: String?): AiringSchedule? {
        return value?.let { Gson().fromJson(value, AiringSchedule::class.java) }
    }
}