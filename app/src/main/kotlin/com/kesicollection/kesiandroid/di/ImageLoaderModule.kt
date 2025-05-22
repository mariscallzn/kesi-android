package com.kesicollection.kesiandroid.di

import android.content.Context
import coil3.ImageLoader
import coil3.map.Mapper
import coil3.network.NetworkFetcher
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import com.kesicollection.core.app.qualifiers.KesiAndroidApiUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ImageLoaderModule {

    companion object {

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
        fun providesOkHttpClient(): OkHttpClient =
            OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }).build()

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

        @Provides
        @Singleton
        fun providesMapper(
            @KesiAndroidApiUrl kesiAndroidApiUrl: String
        ): Mapper<String, String> = Mapper { data, _ -> "${kesiAndroidApiUrl}images/${data}" }

        @Provides
        @Singleton
        fun providesImageLoader(
            @ApplicationContext context: Context,
            okHttpNetworkFetcherFactory: NetworkFetcher.Factory,
            mapper: Mapper<String, String>,
        ): ImageLoader = ImageLoader.Builder(context).crossfade(true).components {
                add(okHttpNetworkFetcherFactory)
                add(mapper)
            }.build()
    }
}