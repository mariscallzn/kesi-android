package com.kesicollection.feature.doggallery

import android.content.Context
import coil3.ImageLoader
import coil3.network.NetworkFetcher
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * Entry point for accessing the [ImageLoader] instance from the Hilt dependency graph.
 *
 * This interface allows components outside of the [DogGalleryModule] to retrieve the
 * globally configured [ImageLoader] using Hilt's entry point mechanism.
 *
 * The [ImageLoader] provided through this entry point is configured with:
 * - Crossfade animation enabled.
 * - An [OkHttpNetworkFetcherFactory] for network requests.
 * - An [OkHttpClient] configured with a logging interceptor for debugging network responses.
 *
 * This entry point is installed in the [SingletonComponent], making the [ImageLoader]
 * available throughout the application.
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface ImageLoaderEntryPoint {
    fun imageLoader(): ImageLoader
}

/**
 * Hilt module responsible for providing dependencies related to the dog gallery feature.
 *
 * This module configures and provides dependencies such as:
 * - [OkHttpClient]: Configured for network operations within the dog gallery.
 * - [NetworkFetcher.Factory]: Configured to use OkHttp for fetching network images.
 * - [ImageLoader]: The Coil image loader instance, configured with crossfade animation and the
 *   OkHttp network fetcher.
 *
 * All provided dependencies are scoped to the [SingletonComponent], ensuring that a single instance
 * of each dependency is available throughout the application.
 */
@InstallIn(SingletonComponent::class)
@Module
abstract class DogGalleryModule {

    companion object {
        @Provides
        @Singleton
        fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor {
                // Keep it ready for extensions
                it.proceed(it.request())
            }
            .build()

        @Provides
        @Singleton
        fun providesNetworkFetcherFactory(
            okHttpClient: OkHttpClient
        ): NetworkFetcher.Factory = OkHttpNetworkFetcherFactory(callFactory = { okHttpClient })

        @Provides
        @Singleton
        fun providesImageLoader(
            @ApplicationContext context: Context,
            okHttpNetworkFetcherFactory: NetworkFetcher.Factory,
        ): ImageLoader =
            ImageLoader.Builder(context)
                .crossfade(true)
                .components {
                    add(okHttpNetworkFetcherFactory)
                }.build()
    }
}