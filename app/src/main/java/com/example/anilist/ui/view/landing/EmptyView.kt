package com.example.anilist.ui.view.landing

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anilist.R
import com.example.anilist.ui.theme.AniListTheme

@Composable
fun EmptyView(
    modifier: Modifier = Modifier,
    painterImage: Painter = painterResource(id = R.drawable.i_no_data),
    text: String = stringResource(R.string.empty_description),
    textColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterImage, contentDescription = null)
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = text, style = MaterialTheme.typography.titleLarge, color = textColor)
    }
}

@Preview
@Composable
fun EmptyViewPreview() {
    AniListTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            EmptyView(modifier = Modifier.fillMaxSize())
        }
    }
}

@Preview
@Composable
fun EmptyViewPreviewDark() {
    AniListTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            EmptyView(modifier = Modifier.fillMaxSize())
        }
    }
}