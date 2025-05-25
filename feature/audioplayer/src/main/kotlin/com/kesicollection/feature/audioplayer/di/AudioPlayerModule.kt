package com.kesicollection.feature.audioplayer.di

import com.kesicollection.feature.audioplayer.player.AudioPlayerController
import com.kesicollection.feature.audioplayer.player.Media3AudioPlayerController
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AudioPlayerDefaultDispatcher

@Module
@InstallIn(SingletonComponent::class)
abstract class AudioPlayerModule {

    @Binds
    @Singleton
    abstract fun bindAudioPlayerController(
        media3AudioPlayerController: Media3AudioPlayerController
    ): AudioPlayerController

    companion object {

        @Provides
        @AudioPlayerDefaultDispatcher
        fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Main
    }
}