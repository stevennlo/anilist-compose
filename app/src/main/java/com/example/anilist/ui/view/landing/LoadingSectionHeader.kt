package com.example.anilist.ui.view.landing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anilist.ui.theme.AniListTheme
import com.example.anilist.ui.theme.Shapes


@Composable
fun LoadingSectionHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .width(200.dp)
                .height(20.dp)
                .clip(Shapes.extraSmall)
                .background(MaterialTheme.colorScheme.outline)
        )
        Spacer(modifier = Modifier.weight(1f))
        Spacer(
            modifier = Modifier
                .width(72.dp)
                .height(20.dp)
                .clip(Shapes.extraSmall)
                .background(MaterialTheme.colorScheme.outline)
        )
    }
}

@Preview
@Composable
fun LoadingSectionHeaderPreview() {
    AniListTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            LoadingSectionHeader()
        }
    }
}

@Preview
@Composable
fun LoadingSectionHeaderPreviewDark() {
    AniListTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            LoadingSectionHeader()
        }
    }
}