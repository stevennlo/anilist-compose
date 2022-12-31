package com.example.anilist.ui.view.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.anilist.R
import com.example.anilist.model.Media
import com.example.anilist.model.Result
import com.example.anilist.ui.theme.AniListTheme
import com.example.anilist.ui.theme.Shapes
import com.example.anilist.ui.view.detail.DetailActivity
import com.example.anilist.ui.view.landing.EmptyView
import com.example.anilist.ui.view.landing.ResultSnackbar
import com.example.anilist.util.HttpUtil.getErrorMessage
import com.example.anilist.util.SnackbarVisualsWithError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : ComponentActivity() {

    companion object {
        const val EXTRA_INITIAL_SEARCH = "EXTRA_INITIAL_SEARCH"
        const val EXTRA_SORT = "EXTRA_SORT"
        const val EXTRA_SEASON = "EXTRA_SEASON"
        const val EXTRA_SEASON_YEAR = "EXTRA_SEASON_YEAR"
        const val EXTRA_ONLY_FAVORITES = "EXTRA_ONLY_FAVORITES"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AniListTheme {
                SearchScreen()
            }
        }
    }

    data class UiState(
        val medias: List<Media> = emptyList(),
        val initialSearch: Boolean = true,
        val refreshing: Boolean = false,
        val loading: Boolean = false,
        val error: Result.Error<*>? = null,
        val filters: List<String> = emptyList(),
        @Suppress("EmptyRange") val filterScreenParam: FilterScreenParam =
            FilterScreenParam(
                0 until 0,
                null,
                null,
                null,
                false
            )
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SearchScreen(modifier: Modifier = Modifier, viewModel: SearchViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val initialSearch = uiState.initialSearch
    val refreshing = uiState.refreshing
    val error = uiState.error
    val medias = uiState.medias
    val loading = uiState.loading
    val filters = uiState.filters
    val isFilterVisible = filters.isNotEmpty()
    val filterScreenParam = uiState.filterScreenParam

    val pullRefreshState = rememberPullRefreshState(
        refreshing,
        { if (!refreshing) viewModel.onEvent(SearchViewModel.UiEvent.Refresh) }
    )

    var keyword by rememberSaveable {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val activity = context as? Activity
    val focusManager = LocalFocusManager.current

    val snackbarHostState = remember { SnackbarHostState() }
    error?.let {
        val message = it.getErrorMessage(context)
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(
                SnackbarVisualsWithError(message, true)
            )
            viewModel.onEvent(SearchViewModel.UiEvent.ErrorShown)
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onEvent(SearchViewModel.UiEvent.RefreshFavorite)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val focusRequester = remember { FocusRequester() }
    if (initialSearch) {
        LaunchedEffect(Unit) {
            delay(200)
            focusRequester.requestFocus()
        }
    }

    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val coroutineScope = rememberCoroutineScope()
    BackHandler(modalBottomSheetState.isVisible) {
        coroutineScope.launch { modalBottomSheetState.hide() }
    }

    ModalBottomSheetLayout(
        sheetContent = {
            FilterScreen(
                param = filterScreenParam,
                onUpdateSort = { viewModel.onEvent(SearchViewModel.UiEvent.UpdateSortFilterParam(it)) },
                onUpdateYear = { viewModel.onEvent(SearchViewModel.UiEvent.UpdateYearFilterParam(it)) },
                onUpdateSeason = {
                    viewModel.onEvent(
                        SearchViewModel.UiEvent.UpdateSeasonFilterParam(
                            it
                        )
                    )
                },
                onUpdateOnlyFavorites = {
                    viewModel.onEvent(
                        SearchViewModel.UiEvent.UpdateOnlyFavoritesFilterParam(
                            it
                        )
                    )
                },
                onSubmit = {
                    viewModel.onEvent(SearchViewModel.UiEvent.PersistFilter)
                    coroutineScope.launch { modalBottomSheetState.hide() }
                },
                onClose = { coroutineScope.launch { modalBottomSheetState.hide() } }
            )
        },
        sheetState = modalBottomSheetState,
        sheetShape = Shapes.medium.copy(bottomStart = ZeroCornerSize, bottomEnd = ZeroCornerSize)
    ) {
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
                        SearchField(
                            value = keyword,
                            onValueChange = { keyword = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyword = keyword.trim()
                                    viewModel.onEvent(SearchViewModel.UiEvent.Search(keyword))
                                    focusManager.clearFocus()
                                }
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { activity?.finish() }) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = stringResource(
                                    R.string.navigate_up_action_content_desc
                                )
                            )
                        }
                    },
                    actions = {
                        AnimatedVisibility(visible = !initialSearch) {
                            IconButton(onClick = {
                                viewModel.onEvent(SearchViewModel.UiEvent.RefreshFilterParam)
                                coroutineScope.launch { modalBottomSheetState.show() }
                            }) {
                                Icon(
                                    imageVector = Icons.Rounded.Tune,
                                    contentDescription = stringResource(
                                        R.string.filter_action_content_desc
                                    )
                                )
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            if (initialSearch) {
                EmptyView(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    text = stringResource(R.string.lets_search_description),
                    painterImage = painterResource(id = R.drawable.i_search)
                )
            } else {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    AnimatedVisibility(
                        modifier = Modifier.zIndex(1f),
                        visible = isFilterVisible,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        Column {
                            Divider()
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surface),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Style,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(vertical = 8.dp, horizontal = 12.dp)
                                )
                                LazyRow(
                                    modifier = Modifier
                                        .weight(1f),
                                    contentPadding = PaddingValues(horizontal = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    itemsIndexed(filters) { index, filter ->
                                        FilterChip(
                                            selected = true,
                                            onClick = {
                                                viewModel.onEvent(
                                                    SearchViewModel.UiEvent.RemoveFilter(
                                                        index
                                                    )
                                                )
                                            },
                                            label = { Text(text = filter) },
                                            trailingIcon = {
                                                Icon(
                                                    imageVector = Icons.Rounded.Close,
                                                    contentDescription = stringResource(R.string.remove_filter_content_desc)
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .pullRefresh(pullRefreshState)
                    ) {
                        if (medias.isEmpty() && !loading) {
                            EmptyView(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                if (loading) {
                                    items(3) {
                                        LoadingMediaCard()
                                    }
                                } else {
                                    items(medias, key = { it.id }) { media ->
                                        MediaCard(media = media, onClick = {
                                            val intent =
                                                Intent(context, DetailActivity::class.java).apply {
                                                    putExtra(DetailActivity.EXTRA_MEDIA, media)
                                                }
                                            context.startActivity(intent)
                                        })
                                    }
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
        }
    }
}
