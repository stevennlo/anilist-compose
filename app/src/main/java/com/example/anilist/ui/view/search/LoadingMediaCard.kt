package com.example.anilist.ui.view.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anilist.ui.theme.AniListTheme
import com.example.anilist.ui.theme.Shapes

@Composable
fun LoadingMediaCard(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(236.dp)
            .shadow(6.dp, RectangleShape)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(2f / 3f)
                .background(MaterialTheme.colorScheme.outline)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .width(164.dp)
                    .height(20.dp)
                    .clip(Shapes.extraSmall)
                    .background(MaterialTheme.colorScheme.outline)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Spacer(
                modifier = Modifier
                    .width(80.dp)
                    .height(16.dp)
                    .clip(Shapes.extraSmall)
                    .background(MaterialTheme.colorScheme.outline)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Spacer(
                modifier = Modifier
                    .width(80.dp)
                    .height(16.dp)
                    .clip(Shapes.extraSmall)
                    .background(MaterialTheme.colorScheme.outline)
            )
        }
    }
}

@Preview
@Composable
fun LoadingMediaCardPreview() {
    AniListTheme {
        LoadingMediaCard()
    }
}

@Preview
@Composable
fun LoadingMediaCardPreviewDark() {
    AniListTheme(darkTheme = true) {
        LoadingMediaCard()
    }
}