package com.example.anilist.util

import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarVisuals
import com.example.anilist.model.*
import java.util.*

object FakeData {
    @Suppress("DEPRECATION")
    val media = Media(
        id = 142838,
        title = "SPY×FAMILY Part 2",
        coverImageUrl = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx142838-ECZSqfknAqAT.jpg",
        bannerImageUrl = "https://s4.anilist.co/file/anilistcdn/media/anime/banner/142838-tynuN00wxmKO.jpg",
        imageColor = "#28bbfe",
        description = "The second half of <i>SPYxFAMILY</i>.",
        studios = listOf("Wit Studio", "CloverWorks"),
        genres = listOf(
            "Action",
            "Comedy",
            "Slice of Life",
            "Supernatural"
        ),
        duration = 24,
        episodes = 13,
        format = MediaFormat.TV,
        season = MediaSeason.FALL,
        seasonYear = 2022,
        status = MediaStatus.RELEASING,
        nextAiringEpisode = AiringSchedule(337999, 40664, 10),
        startDate = Date(2022, 10, 1),
        averageScore = 84,
        popularity = 137609,
        favorites = 3910,
        trending = 89
    )

    val snackbarData = object : SnackbarData {
        override val visuals: SnackbarVisuals = SnackbarVisualsWithError("Error here", true)
        override fun dismiss() {}
        override fun performAction() {}
    }

    val character = Character(
        id = 345821,
        name = "Loid Forger",
        role = CharacterRole.MAIN,
        imageUrl = "https://s4.anilist.co/file/anilistcdn/character/large/b138101-7NCB0Md8zA6G.png",
        voiceActor = VoiceActor(
            id = 102695,
            name = "Takuya Eguchi",
            language = "Japanese",
            imageUrl = "https://s4.anilist.co/file/anilistcdn/staff/large/n102695-dGzsJkor0KMj.jpg"
        )
    )
}