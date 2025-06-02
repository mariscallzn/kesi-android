package com.kesicollection.articles.intentprocessor

import com.google.common.truth.Truth.assertThat
import com.kesicollection.articles.UiArticlesState
import com.kesicollection.articles.model.asUiArticle
import com.kesicollection.core.app.CrashlyticsWrapper
import com.kesicollection.core.app.PreviewCrashlyticsWrapper
import com.kesicollection.core.app.Reducer
import com.kesicollection.domain.GetArticlesUseCase
import com.kesicollection.domain.IsArticleBookmarkedUseCase
import com.kesicollection.test.core.fake.FakeArticles
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchArticlesIntentProcessorTest {

    private val getArticlesUseCase: GetArticlesUseCase = mockk()
    private val isArticleBookmarkedUseCase: IsArticleBookmarkedUseCase = mockk()
    private val crashlyticsWrapper: CrashlyticsWrapper = mockk()
    private lateinit var fetchArticlesIntentProcessor: FetchArticlesIntentProcessor


    @Before
    fun setUp() {
        every { crashlyticsWrapper.params } returns PreviewCrashlyticsWrapper.params
        fetchArticlesIntentProcessor = FetchArticlesIntentProcessor(
            getArticlesUseCase = getArticlesUseCase,
            isArticleBookmarkedUseCase = isArticleBookmarkedUseCase,
            crashlyticsWrapper = crashlyticsWrapper
        )
    }

    /**
     * Validates that the `FetchArticlesIntentProcessor` emits an `isLoading = true` state
     * before emitting a success state with the fetched articles.
     *
     * This test simulates the scenario where the intent processor is triggered to fetch articles.
     * It then verifies that the first state emitted indicates that the operation is loading
     * (i.e., `isLoading` is true), and subsequently, a second state is emitted indicating
     * successful retrieval of the articles (i.e., `isLoading` is false and the articles are present).
     *
     * The test checks for the following sequence of states:
     * 1. `isLoading = true`, `screenError = null`, `articles = emptyList()`
     * 2. `isLoading = false`, `screenError = null`, `articles = [list of articles]`
     */
    @Test
    fun `processIntent WHEN fetching articles is successful THEN updates state with articles data`() =
        runTest {

            coEvery { getArticlesUseCase() } returns Result.success(FakeArticles.items)
            coEvery { isArticleBookmarkedUseCase(any()) } returns false

            val capturedStates = mutableListOf<UiArticlesState>()

            fetchArticlesIntentProcessor.processIntent { reducer: Reducer<UiArticlesState> ->
                capturedStates.add(UiArticlesState().reducer())
            }

            assertThat(capturedStates.size).isEqualTo(2)

            val loadingState = capturedStates[0]
            assertThat(loadingState.isLoading).isTrue()
            assertThat(loadingState.error).isNull()
            assertThat(loadingState.articles).isEmpty()

            val successState = capturedStates[1]
            assertThat(successState.isLoading).isFalse()
            assertThat(successState.error).isNull()
            assertThat(successState.articles).isEqualTo(FakeArticles.items.map { it.asUiArticle() })

            coVerify {
                getArticlesUseCase()
                isArticleBookmarkedUseCase(any())
            }
            verify { crashlyticsWrapper wasNot Called }
        }
}