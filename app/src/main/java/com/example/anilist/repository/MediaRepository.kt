package com.example.anilist.repository

import com.example.anilist.model.*
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getLandingMedia(
        season: MediaSeason,
        year: Int,
        nextSeason: MediaSeason,
        nextYear: Int
    ): Flow<Result<LandingMedia>>

    fun searchMedia(
        keyword: String?,
        season: MediaSeason?,
        year: Int?,
        sort: MediaSort?,
        onlyFavorites: Boolean
    ): Flow<Result<List<Media>>>

    fun getCharacterAndRecommendation(id: Int): Flow<Result<Pair<List<Character>, List<Media>>>>

    suspend fun addFavorite(media: Media, isFavorite: Boolean)

    fun getFavoritePopularMedia(limit: Int): Flow<List<Media>>

    suspend fun isFavorite(id: Int): Boolean
}