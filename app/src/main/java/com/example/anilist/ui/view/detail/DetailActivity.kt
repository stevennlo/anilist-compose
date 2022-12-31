package com.example.anilist.ui.view.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.anilist.R
import com.example.anilist.model.Character
import com.example.anilist.model.Media
import com.example.anilist.ui.theme.AniListTheme
import com.example.anilist.ui.theme.Colors
import com.example.anilist.ui.theme.Shapes
import com.example.anilist.ui.view.landing.LoadingMediaSmallCard
import com.example.anilist.ui.view.landing.MediaSmallCard
import com.example.anilist.ui.view.search.GenreChip
import com.example.anilist.util.NumberUtil.plus
import com.example.anilist.util.NumberUtil.prettyCount
import com.example.anilist.util.StringUtil.orHyphen
import com.example.anilist.util.StringUtil.toAnnotatedString
import com.google.accompanist.flowlayout.FlowRow
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class DetailActivity : ComponentActivity() {
    companion object {
        const val EXTRA_MEDIA = "EXTRA_MEDIA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AniListTheme {
                DetailScreen()
            }
        }
    }

    data class UiState(
        val initLoading: Boolean = true,
        val media: Media? = null,
        val recommendations: List<Media> = emptyList(),
        val characters: List<Character> = emptyList(),
        val isFavorite: Boolean = false
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(modifier: Modifier = Modifier, viewModel: DetailViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val activity = context as? Activity
    val media = uiState.media
    val recommendations = uiState.recommendations
    val isFavorite = uiState.isFavorite
    val initLoading = uiState.initLoading
    val characters = uiState.characters
    if (media == null) {
        activity?.finish()
        return
    }
    val description = media.description.toAnnotatedString()
    val episodeDuration = media.duration?.let {
        val hours = it / 60
        val remainMinutes = it % 60
        val formattedTime = mutableListOf<String>()
        if (hours != 0) {
            val formattedHours =
                pluralStringResource(id = R.plurals.hour_label, count = hours, hours)
            formattedTime.add(formattedHours)
        }
        if (remainMinutes != 0 || formattedTime.isEmpty()) {
            val formattedMinute =
                pluralStringResource(
                    id = R.plurals.minute_label,
                    count = remainMinutes,
                    remainMinutes
                )
            formattedTime.add(formattedMinute)
        }
        formattedTime.joinToString(", ")
    }.orHyphen()

    val seasonLabel: String = when {
        media.season != null -> "${media.season.label} ${media.seasonYear}"
        else -> media.seasonYear?.toString() ?: stringResource(R.string.to_be_announced_label)
    }
    val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    val nextAiring = media.nextAiringEpisode?.let {
        val days = (it.timeUntilAiring / 86400).toInt()
        val remainHours = (it.timeUntilAiring / 3600).toInt() % 60
        val formattedTime = mutableListOf<String>()
        if (days != 0) {
            formattedTime.add("${days}d")
        }
        if (remainHours != 0) {
            formattedTime.add("${remainHours}h")
        }
        val airingIn = if (formattedTime.isEmpty()) {
            stringResource(R.string.airing_label)
        } else {
            formattedTime.joinToString(" ")
        }
        "Ep ${it.episode}: $airingIn"
    }
    val favorites = isFavorite + media.favorites

    val information = listOf(
        Pair(R.string.format_label, media.format.label),
        Pair(R.string.episode_label, media.episodes?.toString().orHyphen()),
        Pair(R.string.episode_duration_label, episodeDuration),
        Pair(R.string.status_label, media.status.label),
        Pair(R.string.start_date_label, media.startDate?.let { dateFormat.format(it) }.orHyphen()),
        Pair(R.string.season_label, seasonLabel),
        Pair(R.string.studios_label, media.studios.joinToString(", ").ifBlank { "-" })
    )
    val scoreIcons = listOf(
        Icons.Rounded.SentimentVeryDissatisfied,
        Icons.Rounded.SentimentNeutral,
        Icons.Rounded.SentimentVerySatisfied
    )
    val scoreIcon = when (media.averageScore) {
        null -> Icons.Rounded.SentimentNeutral
        else -> {
            val finalScore = if (media.averageScore > 100) 100 else media.averageScore
            scoreIcons[finalScore / 34]
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onEvent(DetailViewModel.UiEvent.RefreshFavorites)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val lazyListState = rememberLazyListState()
    val isVisible by remember { derivedStateOf { lazyListState.firstVisibleItemIndex > 0 } }

    Scaffold(
        modifier = modifier,
        topBar = {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                TopAppBar(
                    title = {
                        Text(text = media.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    },
                    navigationIcon = {
                        IconButton(onClick = { activity?.finish() }) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = stringResource(R.string.navigate_up_action_content_desc)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { viewModel.onEvent(DetailViewModel.UiEvent.Favorite(!isFavorite)) }) {
                            if (isFavorite) {
                                Icon(
                                    imageVector = Icons.Rounded.Favorite,
                                    contentDescription = stringResource(R.string.unfavorite_action_content_desc),
                                    tint = Colors.Red400
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Rounded.FavoriteBorder,
                                    contentDescription = stringResource(R.string.favorite_action_content_desc)
                                )
                            }
                        }
                    }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { _ ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState
        ) {
            item {
                DetailHeader(
                    imageUrl = media.coverImageUrl,
                    bannerImageUrl = media.bannerImageUrl,
                    imageColor = media.imageColor,
                    favorite = isFavorite,
                    onBack = { activity?.finish() },
                    onFavoriteChange = { viewModel.onEvent(DetailViewModel.UiEvent.Favorite(it)) }
                )
            }
            item {
                Text(
                    text = media.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 16.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Divider()
                Spacer(modifier = Modifier.height(32.dp))
            }
            item {
                LazyRow(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .clip(Shapes.small)
                        .background(MaterialTheme.colorScheme.surface),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    if (nextAiring != null) {
                        item {
                            Column {
                                InformationText(
                                    title = stringResource(R.string.airing_label),
                                    value = nextAiring,
                                    valueTextColor = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                    items(information) { data ->
                        InformationText(
                            title = stringResource(data.first),
                            value = data.second
                        )
                    }
                }
            }
            loadPadding()
            if (media.genres.isNotEmpty()) {
                loadSection(R.string.genres_label) {
                    item {
                        FlowRow(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            mainAxisSpacing = 8.dp,
                            crossAxisSpacing = 8.dp
                        ) {
                            media.genres.forEach { genre ->
                                GenreChip(
                                    text = genre.lowercase()
                                )
                            }
                        }
                    }
                }
            }
            loadSection(R.string.interests_label) {
                item {
                    Row(
                        modifier = Modifier
                            .height(IntrinsicSize.Min)
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth()
                            .clip(Shapes.small)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(vertical = 12.dp)
                    ) {
                        InformationText(
                            title = stringResource(R.string.average_score_label),
                            value = media.averageScore?.let { "$it%" }.orHyphen(),
                            iconVector = scoreIcon,
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        )
                        RowDivider()
                        InformationText(
                            title = stringResource(R.string.favorites_label),
                            value = favorites.prettyCount(),
                            iconVector = Icons.Rounded.Favorite,
                            iconTint = Color.Red,
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        )
                        RowDivider()
                        InformationText(
                            title = stringResource(R.string.popularity_label),
                            value = media.popularity.prettyCount(),
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        )
                    }
                }
            }
            loadSection(R.string.description_label) {
                item {
                    Text(
                        text = description,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth()
                            .clip(Shapes.small)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
            if (characters.isNotEmpty() || initLoading) {
                loadSection(R.string.characters_label) {
                    if (initLoading) {
                        items(3) {
                            LoadingCharacterCard(modifier = Modifier.padding(horizontal = 20.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    } else {
                        items(characters) {
                            CharacterCard(
                                character = it,
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
            if (recommendations.isNotEmpty() || initLoading) {
                loadSection(R.string.recommendations_label) {
                    item {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (initLoading) {
                                items(3) {
                                    LoadingMediaSmallCard(modifier = Modifier.width(96.dp))
                                }
                            } else {
                                items(recommendations, key = { it.id }) {
                                    MediaSmallCard(
                                        media = it,
                                        onClick = {
                                            navigateToDetail(context, it)
                                        },
                                        modifier = Modifier.width(96.dp),
                                        elevation = 2.dp
                                    )
                                }
                            }
                        }
                    }
                }
            }
            loadPadding()
        }
    }
}

private fun navigateToDetail(context: Context, media: Media) {
    val intent = Intent(context, DetailActivity::class.java).apply {
        putExtra(DetailActivity.EXTRA_MEDIA, media)
    }
    context.startActivity(intent)
}

private fun LazyListScope.loadPadding() {
    item {
        Spacer(modifier = Modifier.height(12.dp))
    }
}

private fun LazyListScope.loadSection(
    @StringRes titleResId: Int,
    content: LazyListScope.() -> Unit
) {
    item(key = titleResId.toString()) {
        Text(
            text = stringResource(titleResId),
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 20.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
    content()
    item {
        Spacer(modifier = Modifier.height(12.dp))
    }
}