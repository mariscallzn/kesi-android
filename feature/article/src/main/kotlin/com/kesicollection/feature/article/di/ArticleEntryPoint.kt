package com.kesicollection.feature.article.di

import coil3.ImageLoader
import com.kesicollection.core.app.qualifiers.ArticleAdKey
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

/**
 * Custom qualifier annotation for identifying the image loader specifically used within the
 * article entry point (e.g., a thumbnail or preview image).
 *
 * This annotation helps to distinguish this specific image loader dependency from other
 * image loaders that might be used elsewhere in the application, especially when multiple
 * instances of an image loading interface or class are provided by a dependency injection framework.
 *
 * By using this qualifier, you can ensure that the correct image loader is injected into
 * components responsible for displaying images within the article entry point UI.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ArticleEntryPointImageLoader

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
    @ArticleEntryPointImageLoader
    fun getImageLoader(): ImageLoader

    @ArticleAdKey
    fun getArticleAdKey(): String
}