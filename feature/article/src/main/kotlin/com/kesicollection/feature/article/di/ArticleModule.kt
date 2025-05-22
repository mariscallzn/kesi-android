package com.kesicollection.feature.article.di

import com.kesicollection.core.app.IntentProcessorFactory
import com.kesicollection.feature.article.Intent
import com.kesicollection.feature.article.UiArticleState
import com.kesicollection.feature.article.intent.DefaultIntentProcessorFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger module for providing dependencies related to the article feature.
 *
 * This module is installed in the [SingletonComponent], meaning the provided
 * dependencies will be available throughout the application's lifecycle.
 *
 * It currently provides:
 * - [IntentProcessorFactory]: A factory for creating [com.kesicollection.core.app.IntentProcessor] instances.
 *   The implementation provided is [DefaultIntentProcessorFactory].
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ArticleModule {

    @Binds
    @Singleton
    abstract fun bindInterProcessorFactory(
        impl: DefaultIntentProcessorFactory
    ): IntentProcessorFactory<UiArticleState, Intent>
}