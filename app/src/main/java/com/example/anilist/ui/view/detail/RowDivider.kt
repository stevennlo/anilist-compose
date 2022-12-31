package com.example.anilist.ui.view.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.anilist.ui.theme.AniListTheme

@Composable
fun RowDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = DividerDefaults.Thickness,
    color: Color = DividerDefaults.color
) {
    val targetThickness = if (thickness == Dp.Hairline) {
        (1f / LocalDensity.current.density).dp
    } else {
        thickness
    }
    Box(
        modifier
            .fillMaxHeight()
            .width(targetThickness)
            .background(color = color)
    )
}

@Preview(heightDp = 48, widthDp = 10)
@Composable
fun RowDividerPreview() {
    AniListTheme {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface),
            horizontalArrangement = Arrangement.Center
        ) {
            RowDivider()
        }
    }
}

@Preview(heightDp = 48, widthDp = 10)
@Composable
fun RowDividerPreviewDark() {
    AniListTheme(darkTheme = true) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface),
            horizontalArrangement = Arrangement.Center
        ) {
            RowDivider()
        }
    }
}
