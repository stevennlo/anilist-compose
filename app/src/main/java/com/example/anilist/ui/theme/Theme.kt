package com.example.anilist.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.anilist.ui.theme.Colors.Blue
import com.example.anilist.ui.theme.Colors.Grey100
import com.example.anilist.ui.theme.Colors.Grey300
import com.example.anilist.ui.theme.Colors.Grey50
import com.example.anilist.ui.theme.Colors.Grey600
import com.example.anilist.ui.theme.Colors.Grey800
import com.example.anilist.ui.theme.Colors.Pink40
import com.example.anilist.ui.theme.Colors.Pink80
import com.example.anilist.ui.theme.Colors.PurpleGrey40
import com.example.anilist.ui.theme.Colors.PurpleGrey80

private val DarkColorScheme = darkColorScheme(
    primary = Blue,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    outlineVariant = Grey100
)

private val LightColorScheme = lightColorScheme(
    primary = Blue,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Grey100,
    surface = Grey50,
    surfaceVariant = Grey50,
    onBackground = Grey800,
    outline = Grey300,
    outlineVariant = Grey300,
    secondaryContainer = Blue,
    onSecondaryContainer = Color.White,
    inverseSurface = Grey600
)

@Composable
fun AniListTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        val currentWindow = (view.context as Activity).window

        SideEffect {
            currentWindow.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(currentWindow, view).isAppearanceLightStatusBars =
                darkTheme.not()
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}