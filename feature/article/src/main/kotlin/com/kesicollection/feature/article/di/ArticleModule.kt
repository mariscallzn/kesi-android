package com.kesicollection.feature.article.di

import com.kesicollection.feature.article.intent.DefaultIntentProcessorFactory
import com.kesicollection.feature.article.intent.IntentProcessorFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ArticleDefaultDispatcher

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

    companion object {
        @Provides
        @ArticleDefaultDispatcher
        fun providesDispatcher(): CoroutineDispatcher = Dispatchers.Main
    }
}