package com.example.anilist.util

import android.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import kotlin.math.max
import kotlin.math.min
import androidx.compose.ui.graphics.Color as ComposeColor

object ColorUtil {

    fun ComposeColor.lightenColor(
        value: Float
    ): ComposeColor {
        val hsl = FloatArray(3)
        ColorUtils.colorToHSL(toArgb(), hsl)
        hsl[2] += value
        hsl[2] = max(0f, min(hsl[2], 1f))
        return ComposeColor(ColorUtils.HSLToColor(hsl))
    }

    fun ComposeColor.getContrastColor(): ComposeColor {
        val whiteContrast = ColorUtils.calculateContrast(Color.WHITE, toArgb())
        val blackContrast = ColorUtils.calculateContrast(Color.BLACK, toArgb())
        val contrastColor = if (whiteContrast > blackContrast) Color.WHITE else Color.BLACK
        return ComposeColor(contrastColor)
    }
}