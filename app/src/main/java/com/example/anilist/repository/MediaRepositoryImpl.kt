package com.example.anilist.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.apollographql.apollo3.ApolloClient
import com.example.anilist.data.local.model.FavoriteConstant.FAVORITES_FIELD
import com.example.anilist.data.local.model.FavoriteConstant.POPULARITY_FIELD
import com.example.anilist.data.local.model.FavoriteConstant.SEASON_FIELD
import com.example.anilist.data.local.model.FavoriteConstant.SEASON_YEAR_FIELD
import com.example.anilist.data.local.model.FavoriteConstant.START_DATE_FIELD
import com.example.anilist.data.local.model.FavoriteConstant.TITLE_FIELD
import com.example.anilist.data.local.model.FavoriteConstant.TRENDING_FIELD
import com.example.anilist.data.local.dao.FavoriteDao
import com.example.anilist.data.remote.graphql.DetailMediaQuery
import com.example.anilist.data.remote.graphql.LandingMediaQuery
import com.example.anilist.data.remote.graphql.SearchMediaQuery
import com.example.anilist.model.*
import com.example.anilist.util.AppDispatcher
import com.example.anilist.util.HttpUtil.getErrorResult
import com.example.anilist.util.HttpUtil.orAbsent
import com.example.anilist.util.HttpUtil.toResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient,
    private val favoriteDao: FavoriteDao,
    private val dispatcher: AppDispatcher
) : MediaRepository {

    override fun getLandingMedia(
        season: MediaSeason,
        year: Int,
        nextSeason: MediaSeason,
        nextYear: Int
    ): Flow<Result<LandingMedia>> {
        return apolloClient.query(
            LandingMediaQuery(
                season.toRemoteMediaSeason(),
                year,
                nextSeason.toRemoteMediaSeason(),
                nextYear
            )
        )
            .toFlow()
            .map {
                it.toResult(LandingMedia::parse)
            }.catch { e ->
                emit(e.getErrorResult())
            }.flowOn(dispatcher.io())
    }

    override fun searchMedia(
        keyword: String?,
        season: MediaSeason?,
        year: Int?,
        sort: MediaSort?,
        onlyFavorites: Boolean
    ): Flow<Result<List<Media>>> {
        return (if (onlyFavorites) {
            val query = StringBuilder().append("SELECT * FROM favorite")
            val conditions = mutableListOf<String>()
            keyword?.let {
                conditions.add("$TITLE_FIELD LIKE \"%$it%\"")
            }
            season?.let {
                conditions.add("$SEASON_FIELD = \"$season\"")
            }
            year?.let {
                conditions.add("$SEASON_YEAR_FIELD = \"$year\"")
            }
            if (conditions.isNotEmpty()) {
                query.append(" WHERE ${conditions.joinToString(" AND ")}")
            }
            sort?.let {
                val orderBy = when (sort) {
                    MediaSort.TITLE -> "$TITLE_FIELD ASC"
                    MediaSort.POPULARITY -> "$POPULARITY_FIELD DESC"
                    MediaSort.TRENDING -> "$TRENDING_FIELD DESC, $POPULARITY_FIELD DESC"
                    MediaSort.FAVORITES -> "$FAVORITES_FIELD DESC"
                    MediaSort.RELEASE_DATE -> "$START_DATE_FIELD DESC"
                }
                query.append(" ORDER BY $orderBy")
            }

            favoriteDao.getAllFiltered(SimpleSQLiteQuery(query.toString()))
                .map { entities -> Result.Success(entities.map { it.toMedia() }) }
        } else {
            val mediaSeason = season?.toRemoteMediaSeason()
            val mediaSort = sort?.toRemoteMediaSorts()
            apolloClient.query(
                SearchMediaQuery(
                    keyword.orAbsent(),
                    mediaSeason.orAbsent(),
                    year.orAbsent(),
                    mediaSort.orAbsent()
                )
            )
                .toFlow()
                .map {
                    it.toResult { data ->
                        data.Page?.media?.mapNotNull { medium -> Media.parse(medium?.media) }
                            .orEmpty()
                    }
                }.catch { e ->
                    emit(e.getErrorResult())
                }
        }).flowOn(dispatcher.io())
    }

    override fun getCharacterAndRecommendation(id: Int): Flow<Result<Pair<List<Character>, List<Media>>>> =
        apolloClient.query(DetailMediaQuery(id))
            .toFlow()
            .map { response ->
                response.toResult { data ->
                    Media.parse(data.Media?.media)?.toFavoriteEntity()
                        ?.let { favoriteDao.update(it) }
                    val characters = data.Media?.characters?.edges?.mapNotNull { edge ->
                        edge?.let {
                            Character.parse(it)
                        }
                    }.orEmpty()
                    val medias = data.Media?.recommendations?.nodes?.mapNotNull { node ->
                        node?.mediaRecommendation?.media?.let {
                            Media.parse(it)
                        }
                    }.orEmpty()
                    Pair(characters, medias)
                }
            }.catch { e ->
                emit(e.getErrorResult())
            }.flowOn(dispatcher.io())

    override suspend fun addFavorite(media: Media, isFavorite: Boolean) {
        if (isFavorite) {
            favoriteDao.insert(media.toFavoriteEntity())
        } else {
            favoriteDao.delete(media.id)
        }
    }

    override fun getFavoritePopularMedia(limit: Int): Flow<List<Media>> {
        return favoriteDao.getAllPopularAndLimit(limit)
            .map { entities -> entities.map { it.toMedia() } }
            .flowOn(dispatcher.io())
    }

    override suspend fun isFavorite(id: Int): Boolean {
        return favoriteDao.getById(id) != null
    }
}