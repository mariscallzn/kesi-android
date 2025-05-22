package com.kesicollection.kesiandroid.di

import com.kesicollection.core.app.qualifiers.KesiAndroidApiUrl
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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RetrofitModule {

    @Binds
    @Singleton
    @RemoteArticleSource
    abstract fun bindsArticleApi(
        implements: RetrofitArticleApi
    ): ArticleApi

    companion object {

        @Singleton
        @Provides
        fun providesJson(): Json = Json {
            ignoreUnknownKeys = true
        }

        @Provides
        @Singleton
        fun providesKesiAndroidService(
            json: Json,
            okHttpClient: OkHttpClient,
            @KesiAndroidApiUrl kesiAndroidUrl: String
        ): KesiAndroidService = Retrofit.Builder()
            .baseUrl(kesiAndroidUrl)
            .callFactory { okHttpClient.newCall(it) }
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(KesiAndroidService::class.java)
    }
}