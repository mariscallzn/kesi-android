package com.kesicollection.feature.article.intent

import com.kesicollection.core.app.CrashlyticsWrapper
import com.kesicollection.core.app.IntentProcessor
import com.kesicollection.core.app.Reducer
import com.kesicollection.core.model.ErrorState
import com.kesicollection.domain.GetArticleByIdUseCase
import com.kesicollection.domain.GetMarkdownAsString
import com.kesicollection.feature.article.ArticleErrors
import com.kesicollection.feature.article.UiArticleState
import com.kesicollection.feature.article.uimodel.asUiPodcast

/**
 * [IntentProcessor] responsible for fetching an article by its ID.
 *
 * This class orchestrates the retrieval of an article using [GetArticleByIdUseCase] and updates the [Reducer]
 * with the fetched content or any encountered errors.
 *
 * The process is divided into the following steps:
 *
 * 1. **Loading State:** The processor starts by setting the `isLoading` flag in the [Reducer] to `true` and clearing any pre-existing errors,
 *    indicating that the article fetching process has begun.
 * 2. **Fetch Article:** It invokes [GetArticleByIdUseCase] to fetch the article data, identified by the provided [articleId].
 * 3. **Successful Retrieval:** Upon successfully fetching the article:
 *    - The [Reducer] is updated with the article's `title`, `imageUrl`, and `content`.
 *    - The `content`, which is a list of `ContentSection` models, is transformed into a list of UI components: [BulletList], [Code], [Paragraph], and [SubHeader].
 *    - The podcast information, if present, is converted into a [UiPodcast] object.
 *    - The `isLoading` flag in the [Reducer] is set to `false`, indicating the completion of the fetching process
 * 4. **Error Handling:** In case of an exception during the fetching process:
 *    - The [Reducer] is updated with an [ErrorState], including the error type (e.g., [ArticleErrors.NetworkError]) and the error message.
 *    - The `isLoading` flag in the [Reducer] is set to `false`, indicating that the process has finished with an error
 *
 * @property articleId The unique identifier of the article to be fetched.
 * @property getArticleByIdUseCase The use case responsible for retrieving article data based on its ID.
 * @property getMarkdownAsString The use case responsible for transforming markdown content into a string.
 * @property crashlyticsWrapper A wrapper for Crashlytics, used for logging exceptions.
 */
class FetchArticleIntentProcessor(
    private val articleId: String,
    private val getArticleByIdUseCase: GetArticleByIdUseCase,
    private val getMarkdownAsString: GetMarkdownAsString,
    private val crashlyticsWrapper: CrashlyticsWrapper,
) : IntentProcessor<UiArticleState> {
    override suspend fun processIntent(reducer: (Reducer<UiArticleState>) -> Unit) {
        reducer {
            copy(
                isLoading = true,
                error = null,
                content = "",
                imageUrl = "",
                podcast = null
            )
        }
        try {
            val article = getArticleByIdUseCase(articleId).getOrThrow()
            val result = getMarkdownAsString(article.markdown).getOrThrow()
            reducer {
                copy(
                    isLoading = false,
                    error = null,
                    content = result,
                    imageUrl = article.img,
                    title = article.title,
                    podcast = article.podcast?.asUiPodcast()
                )
            }
        } catch (e: Exception) {
            crashlyticsWrapper.recordException(
                e, mapOf(
                    crashlyticsWrapper.params.screenName to "Article",
                    crashlyticsWrapper.params.className to "FetchArticleIntentProcessor",
                    crashlyticsWrapper.params.action to "fetch",
                )
            )
            reducer {
                copy(
                    isLoading = false,
                    error = ErrorState(ArticleErrors.NetworkError, e.message)
                )
            }
        }
    }
}