package com.example.anilist.ui.view.landing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AssignmentInd
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.anilist.R
import com.example.anilist.model.LandingMedia
import com.example.anilist.model.Media
import com.example.anilist.model.MediaSort
import com.example.anilist.model.Result
import com.example.anilist.ui.theme.AniListTheme
import com.example.anilist.ui.theme.Shapes
import com.example.anilist.ui.view.about.AboutActivity
import com.example.anilist.ui.view.detail.DetailActivity
import com.example.anilist.ui.view.search.SearchActivity
import com.example.anilist.util.AppUtil
import com.example.anilist.util.HttpUtil.getErrorMessage
import com.example.anilist.util.SnackbarVisualsWithError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LandingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            AniListTheme {
                LandingScreen()
            }
        }
    }

    data class UiState(
        val landingMedia: LandingMedia = LandingMedia(
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList()
        ),
        val favoriteMedia: List<Media> = emptyList(),
        val initLoading: Boolean = true,
        val refreshing: Boolean = false,
        val error: Result.Error<*>? = null
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun LandingScreen(modifier: Modifier = Modifier, viewModel: LandingViewModel = viewModel()) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val landingMedia = uiState.landingMedia
    val favoriteMedia = uiState.favoriteMedia
    val initLoading = uiState.initLoading
    val error = uiState.error
    val refreshing = uiState.refreshing

    val snackbarHostState = remember { SnackbarHostState() }
    error?.let {
        val message = it.getErrorMessage(context)
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(
                SnackbarVisualsWithError(message, true)
            )
            viewModel.onEvent(LandingViewModel.UiEvent.ErrorShown)
        }
    }
    val pullRefreshState = rememberPullRefreshState(
        refreshing,
        { if (!refreshing) viewModel.onEvent(LandingViewModel.UiEvent.Refresh) }
    )

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onEvent(LandingViewModel.UiEvent.RefreshFavorites)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val (trending, season, nextSeason, popular) = landingMedia

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                ResultSnackbar(snackbarData = it)
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Image(
                        painter = painterResource(R.drawable.ic_logo),
                        contentDescription = stringResource(R.string.logo_content_desc),
                        modifier = Modifier
                            .size(36.dp)
                            .clip(Shapes.small)
                    )
                },
                actions = {
                    IconButton(onClick = {
                        navigateToSearch(context)
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = stringResource(R.string.search_action_content_desc)
                        )
                    }
//                    IconButton(onClick = {
//                        val intent = Intent(context, AboutActivity::class.java)
//                        context.startActivity(intent)
//                    }) {
//                        Icon(
//                            imageVector = Icons.Rounded.AssignmentInd,
//                            contentDescription = stringResource(R.string.about_action_content_desc)
//                        )
//                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            if (landingMedia.isEmpty() && favoriteMedia.isEmpty() && !initLoading) {
                EmptyView(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(96.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (initLoading) {
                        loadingSection()
                        loadingSection()
                    } else {
                        loadSection(
                            titleResId = R.string.trending_now_label,
                            medias = trending,
                            onViewAll = {
                                navigateToSearch(
                                    context,
                                    false,
                                    listOf(Pair(SearchActivity.EXTRA_SORT, MediaSort.TRENDING))
                                )
                            },
                            onCardClick = { navigateToDetail(context, it) }
                        )
                        loadSection(
                            titleResId = R.string.popular_this_season_label,
                            medias = season,
                            onViewAll = {
                                navigateToSearch(
                                    context,
                                    false,
                                    listOf(
                                        Pair(
                                            SearchActivity.EXTRA_SEASON,
                                            viewModel.currentSeason.second
                                        ),
                                        Pair(
                                            SearchActivity.EXTRA_SEASON_YEAR,
                                            viewModel.currentSeason.first
                                        ),
                                        Pair(SearchActivity.EXTRA_SORT, MediaSort.POPULARITY)
                                    )
                                )
                            },
                            onCardClick = { navigateToDetail(context, it) }
                        )
                        loadSection(
                            titleResId = R.string.upcoming_next_season_label,
                            medias = nextSeason,
                            onViewAll = {
                                navigateToSearch(
                                    context,
                                    false,
                                    listOf(
                                        Pair(
                                            SearchActivity.EXTRA_SEASON,
                                            viewModel.nextSeason.second
                                        ),
                                        Pair(
                                            SearchActivity.EXTRA_SEASON_YEAR,
                                            viewModel.nextSeason.first
                                        )
                                    )
                                )
                            },
                            onCardClick = { navigateToDetail(context, it) }
                        )
                        loadSection(
                            titleResId = R.string.all_time_popular_label,
                            medias = popular,
                            onViewAll = {
                                navigateToSearch(
                                    context,
                                    false,
                                    listOf(Pair(SearchActivity.EXTRA_SORT, MediaSort.POPULARITY))
                                )
                            },
                            onCardClick = { navigateToDetail(context, it) }
                        )
                        loadSection(
                            titleResId = R.string.my_favorite_label,
                            medias = favoriteMedia,
                            onViewAll = {
                                navigateToSearch(
                                    context,
                                    false,
                                    listOf(
                                        Pair(SearchActivity.EXTRA_ONLY_FAVORITES, true),
                                        Pair(SearchActivity.EXTRA_SORT, MediaSort.POPULARITY)
                                    )
                                )
                            },
                            onCardClick = { navigateToDetail(context, it) }
                        )
                    }
                }
            }
            PullRefreshIndicator(
                refreshing,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

private fun navigateToSearch(
    context: Context,
    isInitialSearch: Boolean = true,
    extras: List<Pair<String, java.io.Serializable>> = emptyList()
) {
    val intent = Intent(context, SearchActivity::class.java).apply {
        putExtra(SearchActivity.EXTRA_INITIAL_SEARCH, isInitialSearch)
        extras.forEach {
            putExtra(it.first, it.second)
        }
    }
    context.startActivity(intent)
}

private fun navigateToDetail(context: Context, media: Media) {
    val intent = Intent(context, DetailActivity::class.java).apply {
        putExtra(DetailActivity.EXTRA_MEDIA, media)
    }
    context.startActivity(intent)
}

private fun LazyGridScope.loadSection(
    @StringRes titleResId: Int,
    medias: List<Media>,
    onViewAll: () -> Unit,
    onCardClick: (Media) -> Unit
) {
    if (medias.isNotEmpty()) {
        item(key = titleResId.toString(), span = AppUtil.maxLineSpan) {
            SectionHeader(stringResource(titleResId).uppercase(), onViewAll = onViewAll)
        }
        items(medias, key = { "$titleResId|${it.id}" }) {
            MediaSmallCard(
                media = it,
                onClick = { onCardClick(it) },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        item(span = AppUtil.maxLineSpan) {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun LazyGridScope.loadingSection() {
    item(span = AppUtil.maxLineSpan) {
        LoadingSectionHeader()
    }
    items(3) {
        LoadingMediaSmallCard(modifier = Modifier.padding(bottom = 16.dp))
    }
    item(span = AppUtil.maxLineSpan) {
        Spacer(modifier = Modifier.height(16.dp))
    }
}