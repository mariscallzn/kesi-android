package com.kesicollection.data.repository

import com.kesicollection.core.model.Article
import com.kesicollection.data.api.ArticleApi
import com.kesicollection.data.api.RemoteArticleSource
import javax.inject.Inject

/**
 * Repository interface for managing [Article] data.
 *
 * This interface defines the contract for fetching articles,
 * including retrieving a list of all articles and fetching a specific article by its ID.
 */
interface ArticleRepository {
    /**
     * Retrieves a list of articles from the remote data source.
     *
     * @return A [Result] containing a [List] of [Article] objects if successful,
     *         or an error if the operation fails.
     */
    suspend fun getArticles(): Result<List<Article>>
    /**
     * Retrieves an [Article] by its unique identifier.
     *
     * @param id The unique identifier of the article.
     * @return A [Result] containing the [Article] if found, or an error if not.
     */
    suspend fun getArticleById(id: String): Result<Article>
}

/**
 * Concrete implementation of [ArticleRepository].
 *
 * This class provides the logic for fetching [Article] data from a remote source.
 * It utilizes the [ArticleApi] to communicate with the network and retrieve article information.
 * This implementation fulfills the contract defined by the [ArticleRepository] interface.
 *
 * @property remoteArticleApi The [ArticleApi] service used to interact with the remote data source.
 */
class ArticleRepositoryImpl @Inject constructor(
    @RemoteArticleSource
    private val remoteArticleApi: ArticleApi,
) : ArticleRepository {
    override suspend fun getArticles(): Result<List<Article>> {
        return remoteArticleApi.getAll()
    }

    override suspend fun getArticleById(id: String): Result<Article> {
        return remoteArticleApi.getContentById(id)
    }
}