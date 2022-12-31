package com.example.anilist.ui.view.landing

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.anilist.ui.theme.AniListTheme
import com.example.anilist.util.FakeData
import com.example.anilist.util.SnackbarVisualsWithError

@Composable
fun ResultSnackbar(snackbarData: SnackbarData, modifier: Modifier = Modifier) {
    val visuals = snackbarData.visuals
    val containerColor: Color
    val contentColor: Color
    if (visuals is SnackbarVisualsWithError && visuals.isError) {
        containerColor = MaterialTheme.colorScheme.errorContainer
        contentColor = MaterialTheme.colorScheme.onErrorContainer
    } else {
        containerColor = SnackbarDefaults.color
        contentColor = SnackbarDefaults.contentColor
    }

    Snackbar(
        snackbarData = snackbarData,
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor
    )
}

@Preview
@Composable
fun ResultSnackbarPreview() {
    AniListTheme {
        ResultSnackbar(snackbarData = FakeData.snackbarData)
    }
}

@Preview
@Composable
fun ResultSnackbarPreviewDark() {
    AniListTheme(darkTheme = true) {
        ResultSnackbar(snackbarData = FakeData.snackbarData)
    }
}
