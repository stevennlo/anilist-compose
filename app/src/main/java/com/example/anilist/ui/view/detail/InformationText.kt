package com.example.anilist.ui.view.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anilist.ui.theme.AniListTheme

@Composable
fun InformationText(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    valueTextColor: Color = MaterialTheme.colorScheme.onBackground,
    iconVector: ImageVector? = null,
    iconTint: Color = valueTextColor
) {
    Column(modifier = modifier, horizontalAlignment = horizontalAlignment) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.inverseSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = valueTextColor
            )
            if (iconVector != null) {
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = iconTint
                )
            }
        }
    }
}

@Preview
@Composable
fun InformationTextPreview() {
    AniListTheme {
        Surface {
            InformationText("Title Here", value = "Sample Value")
        }
    }
}

@Preview
@Composable
fun InformationTextPreviewDark() {
    AniListTheme(darkTheme = true) {
        Surface {
            InformationText("Title Here", value = "Sample Value")
        }
    }
}