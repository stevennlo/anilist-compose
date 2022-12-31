package com.example.anilist.ui.view.search

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.anilist.R
import com.example.anilist.model.MediaSeason
import com.example.anilist.model.MediaSort
import com.example.anilist.util.AppUtil
import com.google.accompanist.flowlayout.FlowRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    param: FilterScreenParam,
    onUpdateSeason: (MediaSeason?) -> Unit,
    onUpdateYear: (Int?) -> Unit,
    onUpdateSort: (MediaSort?) -> Unit,
    onUpdateOnlyFavorites: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedSeason = param.selectedSeason
    val selectedYear = param.selectedYear
    val selectedSort = param.selectedSort
    val onlyFavorites = param.onlyFavorites

    val years = param.year.toList()
    val maxHeight = (LocalConfiguration.current.screenHeightDp * 3 / 4).dp

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.filter_label)) },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = stringResource(R.string.navigate_up_action_content_desc)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onSubmit
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Done,
                            contentDescription = stringResource(R.string.apply_action_content_desc)
                        )
                    }
                }
            )
        },
        modifier = modifier.heightIn(max = maxHeight),
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        LazyVerticalGrid(
            modifier = Modifier.padding(innerPadding),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            loadSection(R.string.season_label) {
                items(MediaSeason.values(), key = { it.ordinal }) { season ->
                    MediaFilterChip(
                        text = season.label,
                        selected = selectedSeason == season,
                        onClick = {
                            onUpdateSeason(season.takeIf { selectedSeason != it })
                        }
                    )
                }
            }
            item(span = AppUtil.maxLineSpan) {
                Divider(modifier = Modifier.padding(bottom = 4.dp))
            }
            loadSection(R.string.year_label) {
                item(span = AppUtil.maxLineSpan) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(years) { year ->
                            MediaFilterChip(
                                text = year.toString(),
                                selected = selectedYear == year,
                                onClick = { onUpdateYear(year.takeIf { selectedYear != year }) }
                            )
                        }
                    }
                }
            }
            loadDivider()
            loadSection(R.string.sort_label) {
                item(span = AppUtil.maxLineSpan) {
                    FlowRow(mainAxisSpacing = 8.dp, crossAxisSpacing = 4.dp) {
                        MediaSort.values().forEach { sort ->
                            MediaFilterChip(
                                text = sort.label,
                                selected = selectedSort == sort,
                                onClick = { onUpdateSort(sort.takeIf { selectedSort != it }) }
                            )
                        }
                    }
                }
            }
            loadDivider()
            item(span = AppUtil.maxLineSpan) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.only_favorites_label),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(checked = onlyFavorites, onCheckedChange = { onUpdateOnlyFavorites(it) })
                }
            }
        }
    }
}

private fun LazyGridScope.loadSection(
    @StringRes titleResId: Int,
    content: LazyGridScope.() -> Unit
) {
    item(key = titleResId.toString(), span = AppUtil.maxLineSpan) {
        Text(
            text = stringResource(titleResId),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
    content()
    item(span = AppUtil.maxLineSpan) {
        Spacer(modifier = Modifier.height(12.dp))
    }
}

private fun LazyGridScope.loadDivider() {
    item(span = AppUtil.maxLineSpan) {
        Divider(modifier = Modifier.padding(bottom = 4.dp))
    }
}

data class FilterScreenParam(
    val year: IntProgression,
    val selectedSeason: MediaSeason?,
    val selectedYear: Int?,
    val selectedSort: MediaSort?,
    val onlyFavorites: Boolean
)