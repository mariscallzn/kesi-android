package com.kesicollection.articles.di

import com.kesicollection.articles.Intent
import com.kesicollection.articles.UiArticlesState
import com.kesicollection.articles.intentprocessor.DefaultIntentProcessorFactory
import com.kesicollection.core.app.IntentProcessorFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
}