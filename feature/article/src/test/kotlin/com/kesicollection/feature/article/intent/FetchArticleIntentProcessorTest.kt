package com.kesicollection.feature.article.intent

import com.google.common.truth.Truth.assertThat
import com.kesicollection.core.app.CrashlyticsWrapper
import com.kesicollection.core.app.PreviewCrashlyticsWrapper
import com.kesicollection.core.app.Reducer
import com.kesicollection.domain.GetArticleByIdUseCase
import com.kesicollection.domain.GetMarkdownAsString
import com.kesicollection.feature.article.UiArticleState
import com.kesicollection.feature.article.uimodel.asUiPodcast
import com.kesicollection.test.core.fake.FakeArticles
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchArticleIntentProcessorTest {

    // Using MockK DSL for mock creation
    private val getArticleByIdUseCase: GetArticleByIdUseCase = mockk()
    private val getMarkdownAsString: GetMarkdownAsString = mockk()
    private val crashlyticsWrapper: CrashlyticsWrapper = mockk()

    private lateinit var processor: FetchArticleIntentProcessor

    private val articleId = "test-article-id"

    @Before
    fun setUp() {
        every { crashlyticsWrapper.params } returns PreviewCrashlyticsWrapper.params
        processor = FetchArticleIntentProcessor(
            articleId = articleId,
            getArticleByIdUseCase = getArticleByIdUseCase,
            getMarkdownAsString = getMarkdownAsString,
            crashlyticsWrapper = crashlyticsWrapper
        )
    }

    @Test
    fun `processIntent WHEN fetching article is successful THEN updates state with article data`() =
        runTest {
            // Arrange
            val mockArticle = FakeArticles.items.first()
            val mockMarkdownString = "Processed Markdown Content"
            val expectedUiPodcast = FakeArticles.items.first().podcast?.asUiPodcast()

            coEvery { getArticleByIdUseCase(articleId) } returns Result.success(mockArticle)
            coEvery { getMarkdownAsString(mockArticle.markdown) } returns Result.success(
                mockMarkdownString
            )

            val states = mutableListOf<UiArticleState>()
            val capturingReducer: (Reducer<UiArticleState>) -> Unit = { reducer ->
                // Apply the processor's reducer to a new/default state and capture the result
                states.add(reducer(UiArticleState()))
            }

            // Act
            processor.processIntent(capturingReducer)

            // Assert
            assertThat(states).hasSize(2) // Loading state + Success state

            val loadingState = states[0]
            assertThat(loadingState.isLoading).isTrue()
            assertThat(loadingState.error).isNull()
            assertThat(loadingState.content).isEqualTo("") // As per processor's initial loading update
            assertThat(loadingState.imageUrl).isEqualTo("") // As per processor's initial loading update
            assertThat(loadingState.podcast).isNull()    // As per processor's initial loading update


            val successState = states[1]
            assertThat(successState.isLoading).isFalse()
            assertThat(successState.error).isNull()
            assertThat(successState.title).isEqualTo(mockArticle.title)
            assertThat(successState.imageUrl).isEqualTo(mockArticle.img)
            assertThat(successState.content).isEqualTo(mockMarkdownString)
            assertThat(successState.podcast).isEqualTo(expectedUiPodcast)

            coVerifyOrder {
                getArticleByIdUseCase(articleId)
                getMarkdownAsString(mockArticle.markdown)
            }
            verify { crashlyticsWrapper wasNot Called }
        }
}