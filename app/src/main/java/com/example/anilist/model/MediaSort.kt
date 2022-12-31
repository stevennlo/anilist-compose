package com.example.anilist.model

import com.example.anilist.data.remote.graphql.type.MediaSort as RemoteMediaSort

enum class MediaSort(val label: String) {
    TITLE("Title"),
    POPULARITY("Popularity"),
    TRENDING("Trending"),
    FAVORITES("Favorites"),
    RELEASE_DATE("Release Date");

    fun toRemoteMediaSorts(): List<RemoteMediaSort> {
        return when (this) {
            TITLE -> listOf(RemoteMediaSort.TITLE_ROMAJI)
            POPULARITY -> listOf(RemoteMediaSort.POPULARITY_DESC)
            TRENDING -> listOf(RemoteMediaSort.TRENDING_DESC, RemoteMediaSort.POPULARITY_DESC)
            FAVORITES -> listOf(RemoteMediaSort.FAVOURITES_DESC)
            RELEASE_DATE -> listOf(RemoteMediaSort.START_DATE_DESC)
        }
    }
}