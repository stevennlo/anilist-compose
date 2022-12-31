package com.example.anilist.ui.view.landing

import android.graphics.Color.parseColor
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.anilist.R
import com.example.anilist.model.Media
import com.example.anilist.ui.theme.AniListTheme
import com.example.anilist.ui.theme.Shapes
import com.example.anilist.util.FakeData

@Composable
fun MediaSmallCard(
    media: Media,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    elevation: Dp = 4.dp
) {
    val placeholder = try {
        ColorPainter(Color(parseColor(media.imageColor)))
    } catch (e: java.lang.Exception) {
        painterResource(id = R.drawable.i_placeholder)
    }
    Column(
        modifier = modifier
            .clip(Shapes.small)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(media.coverImageUrl)
                .build(),
            placeholder = placeholder,
            error = placeholder,
            fallback = placeholder,
            contentDescription = stringResource(R.string.media_image_content_desc),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .shadow(elevation, Shapes.small),
            contentScale = ContentScale.Crop
        )
        Text(
            text = media.title,
            modifier = Modifier.padding(vertical = 4.dp),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(widthDp = 96)
@Composable
fun MediaSmallCardPreview() {
    AniListTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            MediaSmallCard(
                media = FakeData.media,
                onClick = {}
            )
        }
    }
}

@Preview(widthDp = 96)
@Composable
fun MediaSmallCardPreviewDark() {
    AniListTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            MediaSmallCard(
                media = FakeData.media,
                onClick = {}
            )
        }
    }
}