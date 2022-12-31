package com.example.anilist.ui.view.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anilist.ui.theme.AniListTheme
import com.example.anilist.ui.theme.Shapes

@Composable
fun GenreChip(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    color: Color = MaterialTheme.colorScheme.onSecondaryContainer
) {
    Text(
        text = text,
        modifier = modifier
            .clip(Shapes.large)
            .background(containerColor)
            .padding(vertical = 4.dp, horizontal = 8.dp),
        style = MaterialTheme.typography.labelSmall,
        color = color
    )
}

@Preview
@Composable
fun GenreChipPreview() {
    AniListTheme {
        GenreChip("action")
    }
}

@Preview
@Composable
fun GenreChipPreviewDark() {
    AniListTheme(darkTheme = true) {
        GenreChip("action")
    }
}