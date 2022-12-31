package com.example.anilist.ui.view.landing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anilist.R
import com.example.anilist.ui.theme.AniListTheme

@Composable
fun SectionHeader(title: String, onViewAll: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .clickable { onViewAll() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.view_all_label),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview
@Composable
fun SectionHeaderPreview() {
    AniListTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            SectionHeader(title = "TRENDING NOW", {})
        }
    }
}

@Preview
@Composable
fun SectionHeaderPreviewDark() {
    AniListTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            SectionHeader(title = "TRENDING NOW", {})
        }
    }
}