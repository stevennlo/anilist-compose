package com.example.anilist.ui.view.search

import android.graphics.Color.parseColor
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.anilist.R
import com.example.anilist.model.*
import com.example.anilist.ui.theme.AniListTheme
import com.example.anilist.ui.theme.Colors
import com.example.anilist.util.ColorUtil.getContrastColor
import com.example.anilist.util.ColorUtil.lightenColor
import com.example.anilist.util.FakeData
import com.example.anilist.util.NumberUtil.orZero
import com.example.anilist.util.StringUtil.orHyphen
import com.example.anilist.util.StringUtil.toAnnotatedString

@Composable
fun MediaCard(media: Media, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val dominantColor = try {
        Color(parseColor(media.imageColor))
    } catch (e: java.lang.Exception) {
        null
    }
    val lighterColor = dominantColor?.lightenColor(0.2f)
    val placeholder = if (dominantColor != null) {
        ColorPainter(dominantColor)
    } else {
        painterResource(id = R.drawable.i_placeholder)
    }
    val context = LocalContext.current
    val chipColor = lighterColor ?: MaterialTheme.colorScheme.onBackground
    val chipTextColor = chipColor.getContrastColor()
    val description = media.description.toAnnotatedString()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(236.dp)
            .shadow(6.dp, RectangleShape)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(2f / 3f)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(media.coverImageUrl)
                    .build(),
                placeholder = placeholder,
                error = placeholder,
                fallback = placeholder,
                contentDescription = stringResource(id = R.string.media_image_content_desc),
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(Colors.Overlay)
                    .padding(8.dp)
            ) {
                Text(
                    text = media.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = media.studios.firstOrNull().orHyphen(),
                    style = MaterialTheme.typography.bodySmall,
                    color = lighterColor ?: Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(12.dp)
            ) {
                if (media.status == MediaStatus.RELEASING) {
                    AiringInfoText(media.nextAiringEpisode)
                } else {
                    SeasonInfoText(media.season, media.seasonYear)
                }
                Spacer(modifier = Modifier.height(4.dp))
                SubInfoText(media)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(media.genres, key = { it }) { genre ->
                    GenreChip(
                        text = genre.lowercase(),
                        containerColor = chipColor,
                        color = chipTextColor
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SubInfoText(media: Media, modifier: Modifier = Modifier) {
    val episodeDuration = if (media.format == MediaFormat.MOVIE) {
        media.duration?.takeIf { it != 0 }?.let {
            val hours = it / 60
            val remainMinutes = it % 60
            val formattedTime = mutableListOf<String>()
            if (hours != 0) {
                val formattedHours =
                    pluralStringResource(id = R.plurals.hour_label, count = hours, hours)
                formattedTime.add(formattedHours)
            }
            if (remainMinutes != 0) {
                val formattedMinute =
                    pluralStringResource(
                        id = R.plurals.minute_label,
                        count = remainMinutes,
                        remainMinutes
                    )
                formattedTime.add(formattedMinute)
            }
            " • ${formattedTime.joinToString(", ")}"
        }
    } else {
        media.episodes?.let {
            " • ${pluralStringResource(id = R.plurals.episode_label, count = it, it)}"
        }
    }.orEmpty()

    val subInfo = media.format.label + episodeDuration

    Text(
        text = subInfo,
        modifier = modifier,
        style = MaterialTheme.typography.bodySmall,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.onSurface
    )
}


@Composable
fun SeasonInfoText(season: MediaSeason?, seasonYear: Int?, modifier: Modifier = Modifier) {
    val seasonLabel: String = when {
        season != null -> "${season.label} $seasonYear"
        else -> seasonYear?.toString() ?: stringResource(R.string.to_be_announced_label)
    }
    Text(
        text = seasonLabel,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AiringInfoText(airingSchedule: AiringSchedule?, modifier: Modifier = Modifier) {
    val timeUntilAiring = airingSchedule?.timeUntilAiring.orZero()
    val days = (timeUntilAiring / 86400).toInt()
    val remainHours = (timeUntilAiring / 3600).toInt() % 24
    val formattedTime = mutableListOf<String>()
    if (days != 0) {
        val formattedDays =
            pluralStringResource(id = R.plurals.day_label, count = days, days)
        formattedTime.add(formattedDays)
    }
    if (remainHours != 0) {
        val formattedHours =
            pluralStringResource(id = R.plurals.hour_label, count = remainHours, remainHours)
        formattedTime.add(formattedHours)
    }
    val airingIn = if (formattedTime.isEmpty()) {
        stringResource(R.string.airing_label)
    } else {
        formattedTime.joinToString(", ")
    }
    airingSchedule?.episode?.let {
        Text(
            text = stringResource(
                R.string.airing_description,
                it
            ),
            modifier = modifier,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
    Text(
        text = airingIn,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}


@Preview
@Composable
fun MediaCardPreview() {
    AniListTheme {
        MediaCard(media = FakeData.media, {})
    }
}

@Preview
@Composable
fun MediaCardPreviewDark() {
    AniListTheme(darkTheme = true) {
        MediaCard(media = FakeData.media, {})
    }
}