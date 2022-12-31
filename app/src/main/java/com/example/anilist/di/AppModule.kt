package com.example.anilist.di

import com.example.anilist.util.AppDispatcher
import com.example.anilist.util.AppDispatcherImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideAppDispatcher(): AppDispatcher = AppDispatcherImpl()
}