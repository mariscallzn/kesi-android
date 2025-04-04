package com.kesicollection.data.usecase

import com.kesicollection.core.model.Article
import com.kesicollection.data.repository.ArticleRepository
import javax.inject.Inject

class GetArticlesUseCase @Inject constructor(
    private val articleRepository: ArticleRepository,
) {
    suspend operator fun invoke(): Result<List<Article>> {
        return articleRepository.getArticles()
    }
}