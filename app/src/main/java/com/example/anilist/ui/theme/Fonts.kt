package com.example.anilist.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.anilist.R

object Fonts {
    val overpass = FontFamily(
        Font(R.font.overpass),
        Font(R.font.overpass_semibold, FontWeight.Medium),
        Font(R.font.overpass_bold, FontWeight.Bold)
    )
}