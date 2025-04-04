package com.kesicollection.data.api

import com.kesicollection.core.model.Article
import com.kesicollection.core.model.ArticleContent
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RemoteArticleSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalArticleSource

interface ArticleApi {
    suspend fun getAll(): Result<List<Article>>
    suspend fun getContentById(id: String): Result<ArticleContent>
}