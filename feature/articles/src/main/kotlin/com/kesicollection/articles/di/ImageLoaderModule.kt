package com.kesicollection.articles.di

import android.content.Context
import coil3.ImageLoader
import coil3.map.Mapper
import coil3.network.NetworkFetcher
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.Options
import coil3.request.crossfade
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Qualifier annotation for the [ImageLoader] instance provided through the
 * [ImageLoaderEntryPoint].
 *
 * Use this annotation to differentiate the [ImageLoader] provided by the
 * [ImageLoaderEntryPoint] from other [ImageLoader] instances that might be
 * available in the dependency graph.
 *
 * This is useful when you have multiple image loaders and want to specify
 * which one should be injected.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ArticlesEntryPointImageLoader

/**
 * Entry point for accessing the [ImageLoader] instance from the Hilt dependency graph.
 *
 * This interface allows components that are not directly managed by Hilt, such as
 * custom view or composable functions, to retrieve the [ImageLoader] instance
 * configured within the [SingletonComponent].
 *
 * The [imageLoader] function provides the [ImageLoader] instance.
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface ImageLoaderEntryPoint {
    @ArticlesEntryPointImageLoader
    fun imageLoader(): ImageLoader
}

@InstallIn(SingletonComponent::class)
@Module
object ImageLoaderModule {

    /**
     * Provides a singleton instance of [OkHttpClient] for network operations.
     *
     * This [OkHttpClient] is pre-configured with a basic interceptor that
     * forwards the original request, serving as a foundation for future
     * interceptor additions for logging, headers, or other request modifications.
     * This setup allows for easy expansion of network request handling capabilities.
     *
     * @return A singleton instance of [OkHttpClient].
     */
    @Provides
    @ArticlesEntryPointImageLoader
    @Singleton
    fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            // Keep it ready for extensions
            it.proceed(it.request())
        }.build()

    /**
     * Provides a [NetworkFetcher.Factory] for fetching images over the network using OkHttp.
     *
     * This function returns an [OkHttpNetworkFetcherFactory] instance, which is responsible for creating
     * [NetworkFetcher] instances that utilize OkHttp for network operations.
     *
     * The factory is configured with the provided [OkHttpClient] to handle the network requests. This ensures
     * that all network operations performed by the fetcher are carried out through the configured OkHttp client.
     *
     * This provider is scoped to the [SingletonComponent], guaranteeing a single, shared instance of the
     * fetcher factory across the application.
     *
     * @param okHttpClient The [OkHttpClient] instance used by the network fetcher factory for making network requests.
     * @return A [NetworkFetcher.Factory] instance configured to use OkHttp.
     */
    @Provides
    @ArticlesEntryPointImageLoader
    @Singleton
    fun providesNetworkFetcherFactory(
        @ArticlesEntryPointImageLoader
        okHttpClient: OkHttpClient
    ): NetworkFetcher.Factory = OkHttpNetworkFetcherFactory(callFactory = { okHttpClient })

    @Provides
    @ArticlesEntryPointImageLoader
    @Singleton
    fun providesMapper(): Mapper<String, String> = object : Mapper<String, String> {
        override fun map(data: String, options: Options) =
            "https://raw.githubusercontent.com/kesicollection/kesi-android-api-data/refs/heads/v1/images/${data}"

    }

    /**
     * Provides a singleton instance of [ImageLoader] for loading images.
     *
     * This function configures an [ImageLoader] with the following:
     * - A crossfade transition for smooth image loading.
     * - A [NetworkFetcher.Factory] for fetching images over the network.
     * - A custom [Mapper] for transforming image URLs.
     *
     * The [ImageLoader] is built using the application context, ensuring it is tied
     * to the application's lifecycle. It's also scoped to the [SingletonComponent],
     * guaranteeing a single instance throughout the application.
     *
     * @param context The application context.
     * @param okHttpNetworkFetcherFactory The network fetcher factory for loading images over the network.
     * @param mapper A mapper for transforming image URLs.
     * @return A configured [ImageLoader] instance.
     */
    @Provides
    @ArticlesEntryPointImageLoader
    @Singleton
    fun providesImageLoader(
        @ApplicationContext context: Context,
        @ArticlesEntryPointImageLoader
        okHttpNetworkFetcherFactory: NetworkFetcher.Factory,
        @ArticlesEntryPointImageLoader
        mapper: Mapper<String, String>,
    ): ImageLoader =
        ImageLoader.Builder(context)
            .crossfade(true)
            .components {
                add(okHttpNetworkFetcherFactory)
                add(mapper)
            }.build()
}