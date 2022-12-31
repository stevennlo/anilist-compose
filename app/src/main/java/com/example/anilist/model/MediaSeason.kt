package com.example.anilist.model

import com.example.anilist.data.remote.graphql.type.MediaSeason as RemoteMediaSeason

enum class MediaSeason(val label: String) {
    WINTER("Winter"),
    SPRING("Spring"),
    SUMMER("Summer"),
    FALL("Fall");

    fun toRemoteMediaSeason(): RemoteMediaSeason {
        return when (this) {
            WINTER -> RemoteMediaSeason.WINTER
            SPRING -> RemoteMediaSeason.SPRING
            SUMMER -> RemoteMediaSeason.SUMMER
            FALL -> RemoteMediaSeason.FALL
        }
    }

    companion object {
        fun parse(mediaSeason: RemoteMediaSeason?): MediaSeason? {
            return when (mediaSeason) {
                RemoteMediaSeason.WINTER -> WINTER
                RemoteMediaSeason.SPRING -> SPRING
                RemoteMediaSeason.SUMMER -> SUMMER
                RemoteMediaSeason.FALL -> FALL
                else -> null
            }
        }
    }
}