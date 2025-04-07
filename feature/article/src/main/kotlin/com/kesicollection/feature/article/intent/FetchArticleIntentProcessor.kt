package com.kesicollection.feature.article.intent

import com.kesicollection.core.model.ContentSection
import com.kesicollection.core.uisystem.ErrorState
import com.kesicollection.data.usecase.GetArticleByIdUseCase
import com.kesicollection.feature.article.ArticleErrors
import com.kesicollection.feature.article.Reducer
import com.kesicollection.feature.article.components.BulletList
import com.kesicollection.feature.article.components.Code
import com.kesicollection.feature.article.components.Paragraph
import com.kesicollection.feature.article.components.SubHeader
import com.kesicollection.feature.article.uimodel.UiPodcast

/**
 * [IntentProcessor] responsible for fetching an article by its ID.
 *
 * This class uses the [GetArticleByIdUseCase] to retrieve the article data
 * and updates the [Reducer] accordingly with the fetched content or any errors.
 *
 * The processing happens in the following steps:
 *
 * 1.  **Loading State:** It initially sets the `isLoading` flag in the [Reducer] to `true` and clears any previous errors.
 * 2.  **Fetch Article:** It uses the [GetArticleByIdUseCase] to fetch the article data associated with the provided [articleId].
 * 3.  **Successful Retrieval:** If the article is fetched successfully:
 *     *   It updates the [Reducer] with the article's `title`, `imageUrl`, and `content`.
 *     *   The content, which is a list of `ContentSection` models, is mapped to a list of UI components, like [BulletList], [Code], [Paragraph], and [SubHeader].
 *     * The podcast information is mapped to a [UiPodcast].
 *     *   It sets `isLoading` to `false` in the [Reducer].
 * 4.  **Error Handling:** If an exception occurs during the fetching process:
 *     *   It updates the [Reducer] with an [ErrorState], including the error type ([ArticleErrors.NetworkError]) and the error message.
 *     *   It sets `isLoading` to `false` in the [Reducer].
 *
 * @property articleId The ID of the article to be fetched.
 * @property getArticleByIdUseCase The use case for retrieving article data by ID.
 */
class FetchArticleIntentProcessor(
    private val articleId: String,
    private val getArticleByIdUseCase: GetArticleByIdUseCase,
) : IntentProcessor {
    override suspend fun processIntent(reducer: (Reducer) -> Unit) {
        reducer { copy(isLoading = true, error = null) }
        try {
            val result = getArticleByIdUseCase(articleId).getOrThrow()
            reducer {
                copy(
                    isLoading = false,
                    title = result.title,
                    imageUrl = result.thumbnail,
                    content = result.content.map {
                        when (it) {
                            is ContentSection.BulletList -> BulletList(it.content, it.bulletPoints)
                            is ContentSection.Code -> Code(it.content)
                            is ContentSection.Paragraph -> Paragraph(it.content)
                            is ContentSection.SubHeader -> SubHeader(it.content)
                        }
                    },
                    podcast = result.podcast?.let {
                        UiPodcast(
                            id = it.id,
                            title = it.title,
                            audioUrl = it.url
                        )
                    }
                )
            }
        } catch (e: Exception) {
            reducer {
                copy(
                    isLoading = false,
                    error = ErrorState(ArticleErrors.NetworkError, e.message)
                )
            }
        }
    }
}