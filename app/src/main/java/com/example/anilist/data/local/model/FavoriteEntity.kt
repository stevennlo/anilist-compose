package com.example.anilist.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.anilist.data.local.model.FavoriteConstant.FAVORITES_FIELD
import com.example.anilist.data.local.model.FavoriteConstant.FAVORITE_ENTITY
import com.example.anilist.data.local.model.FavoriteConstant.POPULARITY_FIELD
import com.example.anilist.data.local.model.FavoriteConstant.SEASON_FIELD
import com.example.anilist.data.local.model.FavoriteConstant.SEASON_YEAR_FIELD
import com.example.anilist.data.local.model.FavoriteConstant.START_DATE_FIELD
import com.example.anilist.data.local.model.FavoriteConstant.TITLE_FIELD
import com.example.anilist.data.local.model.FavoriteConstant.TRENDING_FIELD
import com.example.anilist.model.*
import java.util.*

@Entity(tableName = FAVORITE_ENTITY)
data class FavoriteEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = TITLE_FIELD)
    val title: String,
    @ColumnInfo(name = "cover_image_url")
    val coverImageUrl: String,
    @ColumnInfo(name = "banner_image_url")
    val bannerImageUrl: String,
    @ColumnInfo(name = "image_color")
    val imageColor: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "studios")
    val studios: List<String>,
    @ColumnInfo(name = "genres")
    val genres: List<String>,
    @ColumnInfo(name = "duration")
    val duration: Int?,
    @ColumnInfo(name = "episodes")
    val episodes: Int?,
    @ColumnInfo(name = "format")
    val format: MediaFormat,
    @ColumnInfo(name = SEASON_FIELD)
    val season: MediaSeason?,
    @ColumnInfo(name = SEASON_YEAR_FIELD)
    val seasonYear: Int?,
    @ColumnInfo(name = "status")
    val status: MediaStatus,
    @ColumnInfo(name = "next_airing_episode")
    val nextAiringEpisode: AiringSchedule?,
    @ColumnInfo(name = START_DATE_FIELD)
    val startDate: Date?,
    @ColumnInfo(name = "average_score")
    val averageScore: Int?,
    @ColumnInfo(name = POPULARITY_FIELD)
    val popularity: Int,
    @ColumnInfo(name = FAVORITES_FIELD)
    val favorites: Int,
    @ColumnInfo(name = TRENDING_FIELD)
    val trending: Int
) {
    fun toMedia(): Media {
        return Media(
            id = id,
            title = title,
            coverImageUrl = coverImageUrl,
            bannerImageUrl = bannerImageUrl,
            imageColor = imageColor,
            description = description,
            studios = studios,
            genres = genres,
            duration = duration,
            episodes = episodes,
            format = format,
            season = season,
            seasonYear = seasonYear,
            status = status,
            nextAiringEpisode = nextAiringEpisode,
            startDate = startDate,
            averageScore = averageScore,
            popularity = popularity,
            favorites = favorites,
            trending = trending
        )
    }
}