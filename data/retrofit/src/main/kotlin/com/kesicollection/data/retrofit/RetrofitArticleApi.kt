package com.kesicollection.data.retrofit

import com.kesicollection.core.model.Article
import com.kesicollection.data.api.ArticleApi
import com.kesicollection.data.retrofit.model.kesiandroid.asArticle
import com.kesicollection.data.retrofit.service.KesiAndroidService
import javax.inject.Inject

/**
 * Implementation of [ArticleApi] that fetches article data from a remote source using Retrofit.
 *
 * This class utilizes the [KesiAndroidService] to communicate with the backend API.
 *
 * @property kesiAndroidService The service responsible for handling network requests to the Kesi Android API.
 */
class RetrofitArticleApi @Inject constructor(
    private val kesiAndroidService: KesiAndroidService,
) : ArticleApi {
    override suspend fun getAll(): Result<List<Article>> = Result.runCatching {
        kesiAndroidService.fetchAllArticles().getOrThrow().map { it.asArticle() }
    }

    override suspend fun getContentById(id: String): Result<Article> {
        TODO("Not yet implemented")
    }
}