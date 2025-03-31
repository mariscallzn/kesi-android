package com.kesicollection.feature.doggallery

import android.content.Context
import coil3.ImageLoader
import coil3.network.NetworkFetcher
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.DefineComponent
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

//@DefineComponent(parent = SingletonComponent::class)
//interface DogGalleryComponent {
//
//    @DefineComponent.Builder
//    interface Builder {
//        fun build(): DogGalleryComponent
//    }
//}

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
//@EntryPoint
//@InstallIn(DogGalleryComponent::class)
//interface ImageLoaderEntryPoint {
//    fun imageLoader(): ImageLoader
//}

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

    /**
     * Binds the concrete implementation [DogGalleryRepositoryImpl] to the [DogGalleryRepository] interface.
     *
     * This provides a way for dependency injection to resolve dependencies on [DogGalleryRepository]
     * with an instance of [DogGalleryRepositoryImpl].
     *
     * This binding is scoped to the [SingletonComponent], ensuring a single instance of the repository
     * is used throughout the application.
     *
     * @param impl The concrete implementation of the dog gallery repository.
     * @return An instance of [DogGalleryRepository].
     */
    @Singleton
    @Binds
    abstract fun bindDogGalleryRepository(
        impl: DogGalleryRepositoryImpl
    ): DogGalleryRepository

//    companion object {

//        /**
//         * Provides a singleton instance of [OkHttpClient].
//         *
//         * This [OkHttpClient] is configured with a basic interceptor that
//         * simply proceeds with the original request, making it ready for adding
//         * more complex interceptors in the future if needed.
//         *
//         * @return A singleton instance of [OkHttpClient].
//         */
//        @Provides
//        @Singleton
//        fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
//            .addInterceptor {
//                // Keep it ready for extensions
//                it.proceed(it.request())
//            }.build()
//
//        /**
//         * Provides a [NetworkFetcher.Factory] for fetching images over the network using OkHttp.
//         *
//         * This function creates and returns an instance of [OkHttpNetworkFetcherFactory], which is
//         * responsible for creating [NetworkFetcher] instances that use OkHttp for network requests.
//         *
//         * The factory is configured to use the provided [OkHttpClient] for making network calls.
//         *
//         * This provider is scoped to the [SingletonComponent], ensuring a single instance of the
//         * fetcher factory is available throughout the application.
//         *
//         * @param okHttpClient The [OkHttpClient] instance to be used by the network fetcher factory.
//         * @return A [NetworkFetcher.Factory] instance configured with OkHttp.
//         */
//        @Provides
//        @Singleton
//        fun providesNetworkFetcherFactory(
//            okHttpClient: OkHttpClient
//        ): NetworkFetcher.Factory = OkHttpNetworkFetcherFactory(callFactory = { okHttpClient })
//
//        /**
//         * Provides a singleton instance of [ImageLoader] configured for the application.
//         *
//         * This method configures the [ImageLoader] with:
//         * - Crossfade transition enabled for smooth image loading.
//         * - [NetworkFetcher.Factory] for handling network image requests using OkHttp.
//         *
//         * The [ImageLoader] instance is built using the application context and is intended for
//         * general-purpose image loading throughout the application.
//         *
//         * @param context The application context, used to initialize the [ImageLoader].
//         * @param okHttpNetworkFetcherFactory The [NetworkFetcher.Factory] for handling network image requests.
//         * @return A configured [ImageLoader] instance.
//         */
//        @Provides
//        fun providesImageLoader(
//            @ApplicationContext context: Context,
//            okHttpNetworkFetcherFactory: NetworkFetcher.Factory,
//        ): ImageLoader =
//            ImageLoader.Builder(context)
//                .crossfade(true)
//                .components {
//                    add(okHttpNetworkFetcherFactory)
//                }.build()
//    }
}