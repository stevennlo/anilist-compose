package com.example.anilist.model

import android.os.Parcelable
import com.example.anilist.data.local.model.FavoriteEntity
import com.example.anilist.util.NumberUtil.orZero
import com.example.anilist.util.StringUtil.orHyphen
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Media(
    val id: Int,
    val title: String,
    val coverImageUrl: String,
    val bannerImageUrl: String,
    val imageColor: String,
    val description: String,
    val studios: List<String>,
    val genres: List<String>,
    val duration: Int?,
    val episodes: Int?,
    val format: MediaFormat,
    val season: MediaSeason?,
    val seasonYear: Int?,
    val status: MediaStatus,
    val nextAiringEpisode: AiringSchedule?,
    val startDate: Date?,
    val averageScore: Int?,
    val popularity: Int,
    val favorites: Int,
    val trending: Int
) : Parcelable {

    fun toFavoriteEntity(): FavoriteEntity {
        return FavoriteEntity(
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

    companion object {
        fun parse(media: com.example.anilist.data.remote.graphql.fragment.Media?): Media? {
            return media?.let {
                val startDate = try {
                    val calendar = Calendar.getInstance()
                    calendar.set(it.startDate!!.year!!, it.startDate.month!!, it.startDate.day!!)
                    calendar.time
                } catch (e: Exception) {
                    null
                }
                Media(
                    id = it.id,
                    title = it.title?.userPreferred.orEmpty(),
                    coverImageUrl = it.coverImage?.large.orEmpty(),
                    bannerImageUrl = it.bannerImage.orEmpty(),
                    imageColor = it.coverImage?.color.orEmpty(),
                    description = it.description.orHyphen(),
                    studios = it.studios?.nodes?.mapNotNull { node -> node?.name }.orEmpty(),
                    genres = it.genres?.filterNotNull().orEmpty(),
                    duration = it.duration,
                    episodes = it.episodes,
                    format = MediaFormat.parse(it.format),
                    season = MediaSeason.parse(it.season),
                    seasonYear = it.seasonYear ?: it.startDate?.year,
                    status = MediaStatus.parse(it.status),
                    nextAiringEpisode = AiringSchedule.parse(it.nextAiringEpisode),
                    startDate = startDate,
                    averageScore = it.averageScore,
                    popularity = it.popularity.orZero(),
                    favorites = it.favourites.orZero(),
                    trending = it.trending.orZero()
                )
            }
        }
    }
}
