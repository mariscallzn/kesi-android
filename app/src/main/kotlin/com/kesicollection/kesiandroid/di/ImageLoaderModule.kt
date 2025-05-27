package com.kesicollection.kesiandroid.di

import android.content.Context
import coil3.ImageLoader
import coil3.map.Mapper
import coil3.network.NetworkFetcher
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import com.kesicollection.core.app.qualifiers.KesiAndroidMediaUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageLoaderModule {

    @Provides
    @Singleton
    fun providesNetworkFetcherFactory(
        okHttpClient: OkHttpClient
    ): NetworkFetcher.Factory = OkHttpNetworkFetcherFactory(callFactory = { okHttpClient })

    @Provides
    @Singleton
    fun providesMapper(
        @KesiAndroidMediaUrl kesiAndroidMediaUrl: String
    ): Mapper<String, String> = Mapper { data, _ -> "${kesiAndroidMediaUrl}images/${data}" }

    @Provides
    @Singleton
    fun providesImageLoader(
        @ApplicationContext context: Context,
        okHttpNetworkFetcherFactory: NetworkFetcher.Factory,
        mapper: Mapper<String, String>,
    ): ImageLoader = ImageLoader.Builder(context).crossfade(true).components {
        add(okHttpNetworkFetcherFactory)
        add(mapper)
    }.build()
}