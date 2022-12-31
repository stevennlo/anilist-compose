package com.example.anilist.ui.view.search

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anilist.ui.theme.AniListTheme
import com.example.anilist.ui.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        shape = Shapes.extraLarge,
        label = {
            Text(
                text = text,
                modifier = Modifier
                    .padding(vertical = 12.dp),
                textAlign = TextAlign.Center
            )
        }
    )
}

@Preview
@Composable
fun MediaFilterChipPreview() {
    AniListTheme {
        MediaFilterChip(text = "Winter", selected = false, onClick = {})
    }
}

@Preview
@Composable
fun MediaFilterChipSelectedPreview() {
    AniListTheme {
        MediaFilterChip(text = "Winter", selected = true, onClick = {})
    }
}