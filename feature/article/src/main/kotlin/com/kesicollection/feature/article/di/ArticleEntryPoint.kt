package com.kesicollection.feature.article.di

import com.kesicollection.core.app.qualifiers.ArticleAdKey
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt Entry Point for accessing dependencies related to articles.
 *
 * This interface defines the entry points for obtaining dependencies that are
 * specifically required for article-related features within the application.
 * It is intended to be used by classes that are not directly managed by Hilt,
 * allowing them to retrieve necessary dependencies from the Hilt graph.
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface ArticleEntryPoint {

    @ArticleAdKey
    fun getArticleAdKey(): String
}