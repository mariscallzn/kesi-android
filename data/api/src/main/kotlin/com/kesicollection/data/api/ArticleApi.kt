package com.kesicollection.data.api

import com.kesicollection.core.model.Article
import javax.inject.Qualifier

/**
 * Qualifier annotation to differentiate between remote and local sources of [Article] data.
 * This annotation is used to specify that a dependency should provide data from a remote source,
 * such as a network API.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RemoteArticleSource

/**
 * Qualifier annotation to mark a dependency that provides access to articles from a local source,
 * such as a database or local storage.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalArticleSource

/**
 * Interface defining the API for fetching article data.
 *
 * This interface provides methods for retrieving a list of articles
 * and fetching the content of a specific article by its ID.
 */
interface ArticleApi {
    /**
     * Retrieves all articles.
     *
     * @return A [Result] containing a [List] of [Article] if successful, or an error if not.
     */
    suspend fun getAll(): Result<List<Article>>

    /**
     * Retrieves an [Article] by its unique identifier.
     *
     * @param id The unique identifier of the article to retrieve.
     * @return A [Result] containing the [Article] if found, or an error if not.
     */
    suspend fun getContentById(id: String): Result<Article>
}