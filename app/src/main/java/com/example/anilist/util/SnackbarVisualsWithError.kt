package com.example.anilist.util

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals

class SnackbarVisualsWithError(
    override val message: String,
    val isError: Boolean
) : SnackbarVisuals {
    override val actionLabel: String?
        get() = null
    override val withDismissAction: Boolean
        get() = false
    override val duration: SnackbarDuration
        get() = SnackbarDuration.Short
}