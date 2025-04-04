package com.kesicollection.data.retrofit.di

import com.kesicollection.data.api.ArticleApi
import com.kesicollection.data.api.RemoteArticleSource
import com.kesicollection.data.retrofit.RetrofitArticleApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ArticleApiModule {

    @Binds
    @Singleton
    @RemoteArticleSource
    abstract fun bindsArticleApi(
        implements: RetrofitArticleApi
    ): ArticleApi
}