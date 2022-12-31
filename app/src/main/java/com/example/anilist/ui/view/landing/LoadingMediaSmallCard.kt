package com.example.anilist.ui.view.landing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anilist.ui.theme.AniListTheme
import com.example.anilist.ui.theme.Shapes

@Composable
fun LoadingMediaSmallCard(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .shadow(2.dp, Shapes.small)
                .background(MaterialTheme.colorScheme.outline)
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .padding(end = 12.dp)
                .height(16.dp)
                .clip(Shapes.extraSmall)
                .background(MaterialTheme.colorScheme.outline)
        )
    }
}

@Preview(widthDp = 96)
@Composable
fun LoadingMediaSmallCardPreview() {
    AniListTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            LoadingMediaSmallCard()
        }
    }
}


@Preview(widthDp = 96)
@Composable
fun LoadingMediaSmallCardPreviewDark() {
    AniListTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            LoadingMediaSmallCard()
        }
    }
}