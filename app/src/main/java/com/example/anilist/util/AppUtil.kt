package com.example.anilist.util

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.lifecycle.SavedStateHandle

object AppUtil {

    val maxLineSpan: LazyGridItemSpanScope.() -> GridItemSpan = { GridItemSpan(maxCurrentLineSpan) }

    fun <T> SavedStateHandle.get(key: String, default: T): T {
        return get<T>(key) ?: default
    }
}