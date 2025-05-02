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
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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

        /**
         * Provides a Json setting preset
         *
         * ignoreUnknownKeys is set to true to protect front-end from crashes due to unknown keys
         */
        @Singleton
        @Provides
        fun providesJson(): Json = Json {
            ignoreUnknownKeys = true
        }

        /**
         * Provides an OkHttp [Call.Factory] instance.
         *
         * This factory is configured with a [HttpLoggingInterceptor] to log HTTP request and response bodies.
         * The logging level is set to [HttpLoggingInterceptor.Level.BODY] for detailed logging.
         * This allows for observing the data being sent and received by the network calls,
         * which is very useful for debugging purposes.
         *
         * @return A configured [Call.Factory] instance.
         */
        @Singleton
        @Provides
        fun providesOkHttpCallFactory(): Call.Factory = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            })
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("Cache-Control", "no-cache")
                    .build()
                chain.proceed(request)
            }
            .build()

        /**
         * Provides a singleton instance of [KesiAndroidService] for interacting with the Kesi Android API.
         *
         * This function configures and builds a Retrofit client specifically for the Kesi Android API,
         * using the provided [Json] for serialization/deserialization and [Call.Factory] for network requests.
         *
         * The base URL for the Kesi Android API is hardcoded to:
         * "https://raw.githubusercontent.com/kesicollection/kesi-android-api-data/refs/heads/v1/"
         *
         * The function utilizes the `kotlinx-serialization` library to handle JSON data conversion,
         * and specifies "application/json" as the media type.
         *
         * @param json The [Json] instance used for serializing and deserializing data.
         * @param callFactory The [Call.Factory] used to create network call instances. Usually an OkHttp client.
         * @return A singleton instance of [KesiAndroidService] ready to be used for API requests.
         */
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