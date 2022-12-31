package com.example.anilist.model

import com.example.anilist.data.remote.graphql.LandingMediaQuery

data class LandingMedia(
    val trending: List<Media>,
    val season: List<Media>,
    val nextSeason: List<Media>,
    val popular: List<Media>
) {

    fun isEmpty(): Boolean {
        return trending.isEmpty() && season.isEmpty() && nextSeason.isEmpty() && popular.isEmpty()
    }

    companion object {
        fun parse(data: LandingMediaQuery.Data): LandingMedia {
            return LandingMedia(
                trending = data.trending?.media?.mapNotNull { Media.parse(it?.media) }.orEmpty(),
                season = data.season?.media?.mapNotNull { Media.parse(it?.media) }.orEmpty(),
                nextSeason = data.nextSeason?.media?.mapNotNull { Media.parse(it?.media) }
                    .orEmpty(),
                popular = data.popular?.media?.mapNotNull { Media.parse(it?.media) }.orEmpty()
            )
        }
    }
}