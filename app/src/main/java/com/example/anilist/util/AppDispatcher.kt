package com.example.anilist.util

import kotlin.coroutines.CoroutineContext

interface AppDispatcher {
    fun computation(): CoroutineContext
    fun io(): CoroutineContext
    fun ui(): CoroutineContext
}