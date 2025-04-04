package com.kesicollection.data.repository

import com.kesicollection.core.model.Article
import com.kesicollection.data.api.ArticleApi
import com.kesicollection.data.api.RemoteArticleSource
import javax.inject.Inject

interface ArticleRepository {
    suspend fun getArticles(): Result<List<Article>>
}

class ArticleRepositoryImpl @Inject constructor(
    @RemoteArticleSource
    private val remoteArticleApi: ArticleApi,
) : ArticleRepository {
    override suspend fun getArticles(): Result<List<Article>> {
        return remoteArticleApi.getAll()
    }
}