package com.example.anilist.util

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class AppDispatcherImpl : AppDispatcher {
    override fun computation(): CoroutineContext = Dispatchers.Default

    override fun io(): CoroutineContext = Dispatchers.IO

    override fun ui(): CoroutineContext = Dispatchers.Main
}