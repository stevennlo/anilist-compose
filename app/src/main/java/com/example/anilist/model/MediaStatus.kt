package com.example.anilist.model

import com.example.anilist.data.remote.graphql.type.MediaStatus as RemoteMediaStatus

enum class MediaStatus(val label: String) {
    FINISHED("Finished"),
    RELEASING("Releasing"),
    NOT_YET_RELEASED("Not Yet Released"),
    CANCELLED("Cancelled"),
    HIATUS("Hiatus"),
    UNKNOWN("Unknown");

    companion object {
        fun parse(mediaStatus: RemoteMediaStatus?): MediaStatus {
            return when (mediaStatus) {
                RemoteMediaStatus.FINISHED -> FINISHED
                RemoteMediaStatus.RELEASING -> RELEASING
                RemoteMediaStatus.NOT_YET_RELEASED -> NOT_YET_RELEASED
                RemoteMediaStatus.CANCELLED -> CANCELLED
                RemoteMediaStatus.HIATUS -> HIATUS
                else -> UNKNOWN
            }
        }
    }
}