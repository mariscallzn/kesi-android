package com.kesicollection.kesiandroid.di

import com.kesicollection.data.api.MarkdownApi
import com.kesicollection.kesiandroid.network.OkHttpMarkdownApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing network-related dependencies, specifically focusing on OkHttp client configurations.
 * This module is installed in the [SingletonComponent], meaning that the provided dependencies will have a singleton scope
 * and live as long as the application.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class OkHttpClientModule {
    /**
     * Binds the [OkHttpMarkdownApi] implementation to the [MarkdownApi] interface.
     * This allows other parts of the application to inject [MarkdownApi] and receive an instance of [OkHttpMarkdownApi].
     * The `@Singleton` annotation ensures that only one instance of [OkHttpMarkdownApi] is created and shared.
     * @param impl The concrete implementation of [MarkdownApi], which is [OkHttpMarkdownApi].
     * @return An instance of [MarkdownApi] (which will be [OkHttpMarkdownApi]).
     */
    @Binds
    @Singleton
    abstract fun bindMarkdownApi(
        impl: OkHttpMarkdownApi
    ): MarkdownApi
}