package com.kesicollection.articles.di

import com.kesicollection.core.app.qualifiers.ArticlesAdKey
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ArticlesEntryPoint {

    @ArticlesAdKey
    fun articlesAdKey(): String
}