package com.kesicollection.feature.article.di

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


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ArticleEntryPointImageLoader

/**
 * Entry point for providing an [ImageLoader] instance to components that
 * require image loading capabilities within the article feature module.
 * This interface is intended to be implemented by a Hilt-generated class.
 * The implementation will provide a singleton instance of [ImageLoader].
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface ArticleImageLoader {
    @ArticleEntryPointImageLoader
    fun getImageLoader(): ImageLoader
}

@Module
@InstallIn(SingletonComponent::class)
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
    @ArticleEntryPointImageLoader
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
    @ArticleEntryPointImageLoader
    @Singleton
    fun providesNetworkFetcherFactory(
        @ArticleEntryPointImageLoader
        okHttpClient: OkHttpClient
    ): NetworkFetcher.Factory = OkHttpNetworkFetcherFactory(callFactory = { okHttpClient })

    @Provides
    @ArticleEntryPointImageLoader
    @Singleton
    fun providesMapper(): Mapper<String, String> = object : Mapper<String, String> {
        override fun map(data: String, options: Options) =
            "https://raw.githubusercontent.com/kesicollection/kesi-android-api-data/refs/heads/v1/images/${data}"

    }

    @Provides
    @ArticleEntryPointImageLoader
    @Singleton
    fun providesImageLoader(
        @ApplicationContext context: Context,
        @ArticleEntryPointImageLoader
        okHttpNetworkFetcherFactory: NetworkFetcher.Factory,
        @ArticleEntryPointImageLoader
        mapper: Mapper<String, String>,
    ): ImageLoader =
        ImageLoader.Builder(context)
            .crossfade(true)
            .components {
                add(okHttpNetworkFetcherFactory)
                add(mapper)
            }.build()
}