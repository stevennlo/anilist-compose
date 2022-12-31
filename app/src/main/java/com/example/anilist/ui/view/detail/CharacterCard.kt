package com.example.anilist.ui.view.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.anilist.R
import com.example.anilist.model.Character
import com.example.anilist.ui.theme.AniListTheme
import com.example.anilist.util.FakeData

@Composable
fun CharacterCard(character: Character, modifier: Modifier = Modifier) {
    val placeholder = painterResource(R.drawable.i_placeholder)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(character.imageUrl)
                    .build(),
                placeholder = placeholder,
                error = placeholder,
                fallback = placeholder,
                contentDescription = stringResource(R.string.character_image_content_desc),
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(3f / 4f),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = character.name,
                    modifier = Modifier.align(Alignment.TopStart),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = character.role.label,
                    modifier = Modifier.align(Alignment.BottomStart),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.inverseSurface
                )
            }
        }
        Spacer(modifier = Modifier.width(4.dp))
        Row(modifier = Modifier.weight(1f)) {
            if (character.voiceActor != null) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    Text(
                        text = character.voiceActor.name,
                        modifier = Modifier.align(Alignment.TopEnd),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = character.voiceActor.language,
                        modifier = Modifier.align(Alignment.BottomEnd),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.inverseSurface,
                        textAlign = TextAlign.End
                    )
                }
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(character.voiceActor.imageUrl)
                        .build(),
                    placeholder = placeholder,
                    error = placeholder,
                    fallback = placeholder,
                    contentDescription = stringResource(R.string.character_image_content_desc),
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(3f / 4f),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Preview
@Composable
fun CharacterCardPreview() {
    AniListTheme {
        CharacterCard(FakeData.character)
    }
}

@Preview
@Composable
fun CharacterCardPreviewDark() {
    AniListTheme(darkTheme = true) {
        CharacterCard(FakeData.character)
    }
}