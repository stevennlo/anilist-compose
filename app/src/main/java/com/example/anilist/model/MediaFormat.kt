package com.example.anilist.model

import com.example.anilist.data.remote.graphql.type.MediaFormat as RemoteMediaFormat

enum class MediaFormat(val label: String) {
    TV("TV Show"),
    TV_SHORT("TV Short"),
    MOVIE("Movie"),
    SPECIAL("Special"),
    OVA("OVA"),
    ONA("ONA"),
    MUSIC("Music"),
    MANGA("Manga"),
    NOVEL("Novel"),
    ONE_SHOT("One Shot"),
    UNKNOWN("Unknown");

    companion object {
        fun parse(mediaFormat: RemoteMediaFormat?): MediaFormat {
            return when (mediaFormat) {
                RemoteMediaFormat.TV -> TV
                RemoteMediaFormat.TV_SHORT -> TV_SHORT
                RemoteMediaFormat.MOVIE -> MOVIE
                RemoteMediaFormat.SPECIAL -> SPECIAL
                RemoteMediaFormat.OVA -> OVA
                RemoteMediaFormat.ONA -> ONA
                RemoteMediaFormat.MUSIC -> MUSIC
                RemoteMediaFormat.MANGA -> MANGA
                RemoteMediaFormat.NOVEL -> NOVEL
                RemoteMediaFormat.ONE_SHOT -> ONE_SHOT
                else -> UNKNOWN
            }
        }
    }
}