package com.kesicollection.articles.di

import android.content.Context
import coil3.ImageLoader
import coil3.map.Mapper
import coil3.network.NetworkFetcher
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import com.kesicollection.articles.Intent
import com.kesicollection.articles.UiArticlesState
import com.kesicollection.articles.intentprocessor.DefaultIntentProcessorFactory
import com.kesicollection.core.app.qualifiers.KesiAndroidApiUrl
import com.kesicollection.core.uisystem.IntentProcessorFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Qualifier annotation for the default [CoroutineDispatcher] used within the articles feature.
 *
 * This annotation is used to distinguish the default dispatcher from other
 * dispatchers that might be injected, ensuring that the correct dispatcher is used
 * for operations within the articles module. In this case, it's used for [Dispatchers.Main].
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ArticlesDefaultDispatcher

/**
 * Dagger module for providing dependencies related to articles.
 *
 * This module is installed in the [SingletonComponent] scope, meaning the provided
 * dependencies will have a singleton lifecycle within the application.
 *
 * It currently provides a binding for the [IntentProcessorFactory],
 * mapping the [DefaultIntentProcessorFactory] implementation to the interface.
 */
@InstallIn(SingletonComponent::class)
@Module
abstract class ArticlesModule {

    @Binds
    @Singleton
    abstract fun bindIntentProcessorFactory(
        impl: DefaultIntentProcessorFactory
    ): IntentProcessorFactory<UiArticlesState, Intent>

    companion object {

        @Provides
        @ArticlesDefaultDispatcher
        fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Main


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
        fun providesMapper(
            @KesiAndroidApiUrl kesiAndroidApiUrl: String
        ): Mapper<String, String> =
            Mapper { data, _ -> "${kesiAndroidApiUrl}images/${data}" }

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
}