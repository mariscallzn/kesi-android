package com.kesicollection.articles.di

import com.kesicollection.articles.Intent
import com.kesicollection.articles.UiArticlesState
import com.kesicollection.articles.intentprocessor.DefaultIntentProcessorFactory
import com.kesicollection.core.app.IntentProcessorFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
    }
}