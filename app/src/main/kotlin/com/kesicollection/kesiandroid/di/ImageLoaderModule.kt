package com.kesicollection.kesiandroid.di

import android.content.Context
import coil3.ImageLoader
import coil3.network.NetworkFetcher
import coil3.network.NetworkFetcher.Factory
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * Hilt module responsible for providing dependencies related to image loading.
 *
 * This module configures and provides instances of:
 * - [OkHttpClient]: A client for making network requests.
 * - [NetworkFetcher.Factory]: A factory for creating network fetchers to download images.
 * - [ImageLoader]: The primary component for loading and displaying images.
 *
 * It's designed to be used in a Hilt application, ensuring that these dependencies
 * are available throughout the application's lifecycle.
 *
 * This module is installed in the [SingletonComponent], meaning that the dependencies
 * it provides are scoped to the application's lifecycle and will be singletons.
 */
@InstallIn(SingletonComponent::class)
@Module
object ImageLoaderModule {
    /**
     * Provides a singleton instance of [OkHttpClient].
     *
     * This [OkHttpClient] is configured with a basic interceptor that
     * simply proceeds with the original request, making it ready for adding
     * more complex interceptors in the future if needed.
     *
     * @return A singleton instance of [OkHttpClient].
     */
    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            // Keep it ready for extensions
            it.proceed(it.request())
        }.build()

    /**
     * Provides a [NetworkFetcher.Factory] for fetching images over the network using OkHttp.
     *
     * This function creates and returns an instance of [OkHttpNetworkFetcherFactory], which is
     * responsible for creating [NetworkFetcher] instances that use OkHttp for network requests.
     *
     * The factory is configured to use the provided [OkHttpClient] for making network calls.
     *
     * This provider is scoped to the [SingletonComponent], ensuring a single instance of the
     * fetcher factory is available throughout the application.
     *
     * @param okHttpClient The [OkHttpClient] instance to be used by the network fetcher factory.
     * @return A [NetworkFetcher.Factory] instance configured with OkHttp.
     */
    @Provides
    @Singleton
    fun providesNetworkFetcherFactory(
        okHttpClient: OkHttpClient
    ): NetworkFetcher.Factory = OkHttpNetworkFetcherFactory(callFactory = { okHttpClient })

    /**
     * Provides a singleton instance of [ImageLoader] configured for the application.
     *
     * This method configures the [ImageLoader] with:
     * - Crossfade transition enabled for smooth image loading.
     * - [NetworkFetcher.Factory] for handling network image requests using OkHttp.
     *
     * The [ImageLoader] instance is built using the application context and is intended for
     * general-purpose image loading throughout the application.
     *
     * @param context The application context, used to initialize the [ImageLoader].
     * @param okHttpNetworkFetcherFactory The [NetworkFetcher.Factory] for handling network image requests.
     * @return A configured [ImageLoader] instance.
     */
    @Provides
    @Singleton
    fun providesImageLoader(
        @ApplicationContext context: Context,
        okHttpNetworkFetcherFactory: Factory,
    ): ImageLoader =
        ImageLoader.Builder(context)
            .crossfade(true)
            .components {
                add(okHttpNetworkFetcherFactory)
            }.build()
}