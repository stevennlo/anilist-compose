package com.example.anilist.ui.view.detail

import android.graphics.Color.parseColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.anilist.R
import com.example.anilist.ui.theme.AniListTheme
import com.example.anilist.ui.theme.Colors
import com.example.anilist.ui.theme.Shapes
import com.example.anilist.util.FakeData

@Composable
fun DetailHeader(
    imageUrl: String,
    bannerImageUrl: String,
    imageColor: String,
    favorite: Boolean,
    onBack: () -> Unit,
    onFavoriteChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val placeholder = try {
        ColorPainter(Color(parseColor(imageColor)))
    } catch (e: java.lang.Exception) {
        painterResource(id = R.drawable.i_placeholder)
    }
    Box(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(bannerImageUrl)
                    .build(),
                placeholder = placeholder,
                error = placeholder,
                fallback = placeholder,
                contentDescription = stringResource(R.string.banner_image_content_desc),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 1.5f)
                    .drawWithCache {
                        val gradient = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.DarkGray),
                            startY = size.height * 0.6f,
                            endY = size.height
                        )
                        onDrawWithContent {
                            drawContent()
                            drawRect(gradient, blendMode = BlendMode.Multiply)
                        }
                    },
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 8.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.End
            ) {
                OutlinedIconToggleButton(
                    checked = favorite,
                    onCheckedChange = onFavoriteChange,
                    colors = IconButtonDefaults.outlinedIconToggleButtonColors(
                        checkedContainerColor = Colors.Red400,
                        checkedContentColor = Color.White
                    ),
                    shape = Shapes.small
                ) {
                    if (favorite) {
                        Icon(imageVector = Icons.Outlined.Favorite, contentDescription = null)
                    } else {
                        Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = null)
                    }
                }
            }
        }
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .build(),
            placeholder = placeholder,
            error = placeholder,
            fallback = placeholder,
            contentDescription = stringResource(R.string.media_image_content_desc),
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 24.dp)
                .width(96.dp)
                .height(144.dp)
                .shadow(12.dp, Shapes.extraSmall)
                .align(Alignment.BottomStart),
            contentScale = ContentScale.Crop
        )
        FilledIconButton(
            onClick = onBack,
            modifier = Modifier.padding(4.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = stringResource(R.string.navigate_up_action_content_desc)
            )
        }
    }
}

@Composable
@Preview
fun DetailHeaderPreview() {
    val media = FakeData.media
    AniListTheme {
        DetailHeader(media.coverImageUrl, media.bannerImageUrl, media.imageColor, false, {}, {})
    }
}

@Composable
@Preview
fun DetailHeaderCheckedPreview() {
    val media = FakeData.media
    AniListTheme {
        DetailHeader(media.coverImageUrl, media.bannerImageUrl, media.imageColor, true, {}, {})
    }
}