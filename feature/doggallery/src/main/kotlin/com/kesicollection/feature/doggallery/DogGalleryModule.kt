package com.kesicollection.feature.doggallery

import android.content.Context
import coil3.ImageLoader
import coil3.request.crossfade
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class DogGalleryModule {

    companion object {
        fun providesImageLoader(@ApplicationContext context: Context): ImageLoader =
            ImageLoader.Builder(context).crossfade(true).build()
    }
}