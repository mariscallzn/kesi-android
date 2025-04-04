package com.kesicollection.data.usecase

import com.kesicollection.core.model.Article
import com.kesicollection.data.repository.ArticleRepository
import javax.inject.Inject

/**
 * Use case for retrieving an article by its ID.
 *
 * This class encapsulates the logic for fetching an [Article] from the repository
 * given its unique identifier. It leverages the [ArticleRepository] to perform
 * the data retrieval.
 *
 * @property articleRepository The repository responsible for providing access to article data.
 */
class GetArticleByIdUseCase @Inject constructor(
    private val articleRepository: ArticleRepository
) {
    suspend operator fun invoke(id: String): Result<Article> {
        return articleRepository.getArticleById(id)
    }
}