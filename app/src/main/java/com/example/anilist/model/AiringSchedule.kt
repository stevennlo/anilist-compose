package com.example.anilist.model

import android.os.Parcelable
import com.example.anilist.data.remote.graphql.fragment.Media
import kotlinx.parcelize.Parcelize

@Parcelize
data class AiringSchedule(
    val id: Int,
    val timeUntilAiring: Long,
    val episode: Int
) : Parcelable {
    companion object {
        fun parse(nextAiringEpisode: Media.NextAiringEpisode?): AiringSchedule? {
            return nextAiringEpisode?.let {
                AiringSchedule(
                    id = it.id,
                    timeUntilAiring = it.timeUntilAiring.toLong(),
                    episode = it.episode
                )
            }
        }
    }
}
