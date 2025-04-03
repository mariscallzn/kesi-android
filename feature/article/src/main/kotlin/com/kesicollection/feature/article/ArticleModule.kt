package com.kesicollection.feature.article

import coil3.ImageLoader
import com.kesicollection.feature.article.intent.DefaultIntentProcessorFactory
import com.kesicollection.feature.article.intent.IntentProcessorFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Entry point for providing an [ImageLoader] instance to components that
 * require image loading capabilities within the article feature module.
 * This interface is intended to be implemented by a Hilt-generated class.
 * The implementation will provide a singleton instance of [ImageLoader].
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface ArticleImageLoader {
    fun getImageLoader(): ImageLoader
}

/**
 * Dagger module for providing dependencies related to the article feature.
 *
 * This module is installed in the [SingletonComponent], meaning the provided
 * dependencies will be available throughout the application's lifecycle.
 *
 * It currently provides:
 * - [IntentProcessorFactory]: A factory for creating [com.kesicollection.feature.article.intent.IntentProcessor] instances.
 *   The implementation provided is [DefaultIntentProcessorFactory].
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ArticleModule {

    @Binds
    @Singleton
    abstract fun bindInterProcessorFactory(
        impl: DefaultIntentProcessorFactory
    ): IntentProcessorFactory
}