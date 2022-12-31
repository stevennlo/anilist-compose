package com.example.anilist.util

import com.example.anilist.model.MediaSeason
import java.util.*

object CalendarUtil {
    fun getCurrentSeason(calendar: Calendar = Calendar.getInstance()): Pair<Int, MediaSeason> {
        calendar.add(Calendar.DAY_OF_YEAR, 14)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val season = MediaSeason.values()[month % 12 / 3]

        return Pair(year, season)
    }

    fun getNextSeason(calendar: Calendar = Calendar.getInstance()): Pair<Int, MediaSeason> {
        calendar.add(Calendar.MONTH, 3)
        return getCurrentSeason(calendar)
    }
}