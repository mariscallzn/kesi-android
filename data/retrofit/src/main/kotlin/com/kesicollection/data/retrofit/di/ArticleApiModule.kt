package com.kesicollection.data.retrofit.di

import com.kesicollection.data.api.ArticleApi
import com.kesicollection.data.api.RemoteArticleSource
import com.kesicollection.data.retrofit.RetrofitArticleApi
import com.kesicollection.data.retrofit.service.KesiAndroidService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
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
        fun providesKesiAndroidService(
            json: Json,
            callFactory: Call.Factory
        ): KesiAndroidService = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/kesicollection/kesi-android-api-data/refs/heads/v1/")
            .callFactory { callFactory.newCall(it) }
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(KesiAndroidService::class.java)
    }
}