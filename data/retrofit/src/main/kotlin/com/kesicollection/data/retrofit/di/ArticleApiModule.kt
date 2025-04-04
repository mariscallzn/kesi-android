package com.kesicollection.data.retrofit.di

import com.kesicollection.data.api.ArticleApi
import com.kesicollection.data.api.RemoteArticleSource
import com.kesicollection.data.retrofit.RetrofitArticleApi
import com.kesicollection.data.retrofit.service.KesiAndroidService
import com.kesicollection.data.retrofit.service.fakeData
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing dependencies related to the Article API.
 *
 * This module is installed in the [SingletonComponent] scope, ensuring that the
 * provided dependencies are singletons and shared across the application.
 *
 * It provides:
 * - A binding for [ArticleApi], using [RetrofitArticleApi] as the implementation.
 *   This binding is annotated with [@RemoteArticleSource] to distinguish it from other potential
 *   ArticleApi implementations.
 * - A singleton instance of [KesiAndroidService], which is currently backed by fake data for testing or development purposes.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ArticleApiModule {

    @Binds
    @Singleton
    @RemoteArticleSource
    abstract fun bindsArticleApi(
        implements: RetrofitArticleApi
    ): ArticleApi

    companion object {

        @Provides
        @Singleton
        fun providesKesiAndroidService(): KesiAndroidService = fakeData
    }
}