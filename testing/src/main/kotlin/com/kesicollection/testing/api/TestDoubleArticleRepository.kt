package com.kesicollection.testing.api

import com.kesicollection.core.model.Article
import com.kesicollection.data.repository.ArticleRepository
import com.kesicollection.testing.testdata.ArticlesTestData

/**
 * A successful result containing a list of articles from test data.
 */
val successGetArticleResult = Result.success(ArticlesTestData.items)

/**
 * A variable holding the result of a getArticles operation.
 * It defaults to [successGetArticleResult] but can be changed to simulate different scenarios in tests.
 */
var getArticlesResult = successGetArticleResult

/**
 * A successful result containing the first article from [ArticlesTestData.items].
 */
val successGetArticleById = Result.success(ArticlesTestData.items.first())
/**
 *  Result of the `getArticleById` operation.
 *  This property dictates the outcome returned by the `getArticleById` method when invoked in tests.
 *  By modifying this value, you can simulate successful or failed article retrieval scenarios.
 *  Defaults to [successGetArticleById]`, simulating a successful retrieval.
 */
var getArticleByIdResult = successGetArticleById

/**
 * A test double implementation of [ArticleRepository] for use in unit tests.
 *
 * This class allows you to control the results of [getArticles] and [getArticleById]
 * by setting the [getArticlesResult] and [getArticleByIdResult] properties.
 *
 * This enables testing of different scenarios and edge cases without relying on
 * external data sources.
 *
 * @property getArticlesResult The result to be returned by [getArticles].
 * @property getArticleByIdResult The result to be returned by [getArticleById].
 */
class TestDoubleArticleRepository : ArticleRepository {
    override suspend fun getArticles(): Result<List<Article>> = getArticlesResult

    override suspend fun getArticleById(id: String): Result<Article> = getArticleByIdResult
}