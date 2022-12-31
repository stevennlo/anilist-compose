package com.example.anilist.ui.view.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anilist.ui.theme.AniListTheme
import com.example.anilist.ui.theme.Shapes

@Composable
fun LoadingCharacterCard(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(2f / 3f)
                .background(MaterialTheme.colorScheme.outline)
        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(8.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .width(60.dp)
                    .height(12.dp)
                    .clip(Shapes.extraSmall)
                    .background(MaterialTheme.colorScheme.outline)
                    .align(Alignment.TopStart)
            )
            Spacer(
                modifier = Modifier
                    .width(60.dp)
                    .height(12.dp)
                    .clip(Shapes.extraSmall)
                    .background(MaterialTheme.colorScheme.outline)
                    .align(Alignment.BottomStart)
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(8.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .width(60.dp)
                    .height(12.dp)
                    .clip(Shapes.extraSmall)
                    .background(MaterialTheme.colorScheme.outline)
                    .align(Alignment.TopEnd)
            )
            Spacer(
                modifier = Modifier
                    .width(60.dp)
                    .height(12.dp)
                    .clip(Shapes.extraSmall)
                    .background(MaterialTheme.colorScheme.outline)
                    .align(Alignment.BottomEnd)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(2f / 3f)
                .background(MaterialTheme.colorScheme.outline)
        )
    }
}


@Preview
@Composable
fun LoadingCharacterCardPreview() {
    AniListTheme {
        LoadingCharacterCard()
    }
}

@Preview
@Composable
fun LoadingCharacterCardPreviewDark() {
    AniListTheme(darkTheme = true) {
        LoadingCharacterCard()
    }
}