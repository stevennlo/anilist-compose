package com.example.anilist.di

import com.example.anilist.repository.MediaRepository
import com.example.anilist.repository.MediaRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMediaRepository(mediaRepositoryImpl: MediaRepositoryImpl): MediaRepository
}