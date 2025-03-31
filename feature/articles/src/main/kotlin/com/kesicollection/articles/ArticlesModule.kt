package com.kesicollection.articles

import coil3.ImageLoader
import com.kesicollection.articles.intentprocessor.DefaultIntentProcessorFactory
import com.kesicollection.articles.intentprocessor.IntentProcessorFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
    fun imageLoader(): ImageLoader
}

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
    ): IntentProcessorFactory
}