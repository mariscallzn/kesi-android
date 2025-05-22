package com.kesicollection.articles.intentprocessor

import com.kesicollection.articles.ArticlesErrors
import com.kesicollection.articles.UiArticlesState
import com.kesicollection.articles.model.asUiArticle
import com.kesicollection.core.model.ErrorState
import com.kesicollection.core.app.Reducer
import com.kesicollection.data.usecase.GetArticlesUseCase
import com.kesicollection.data.usecase.IsArticleBookmarkedUseCase
import com.kesicollection.testing.api.TestDoubleArticleRepository
import com.kesicollection.testing.api.TestDoubleBookmarkRepository
import com.kesicollection.testing.api.TestDoubleCrashlyticsWrapper
import com.kesicollection.testing.api.getArticlesResult
import com.kesicollection.testing.api.successGetArticleResult
import com.kesicollection.testing.testdata.ArticlesTestData
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Test class for [FetchArticlesIntentProcessor].
 *
 * This class contains unit tests to verify the behavior of [FetchArticlesIntentProcessor]
 * in various scenarios, including successful article fetching and error handling.
 * It utilizes a [TestDoubleArticleRepository] to simulate the data layer and control
 * the outcomes of article retrieval.
 *
 * The tests cover:
 * - Emitting a loading state (`isLoading = true`) before a success response.
 * - Emitting a loading state (`isLoading = true`) before an error state.
 */
class FetchArticlesIntentProcessorTest {

    /**
     * The intent processor responsible for fetching articles.
     */
    private lateinit var fetchArticlesIntentProcessor: FetchArticlesIntentProcessor

    /**
     * Sets up the test environment before each test.
     *
     * Initializes the [fetchArticlesIntentProcessor] with a [GetArticlesUseCase]
     * that uses a [TestDoubleArticleRepository]. This setup ensures that each test
     * starts with a clean state and a mocked repository for controlled behavior.
     */
    @Before
    fun setUp() {
        fetchArticlesIntentProcessor = FetchArticlesIntentProcessor(
            getArticlesUseCase = GetArticlesUseCase(
                articleRepository = TestDoubleArticleRepository()
            ),
            isArticleBookmarkedUseCase = IsArticleBookmarkedUseCase(
                bookmarkRepository = TestDoubleBookmarkRepository()
            ),
            crashlyticsWrapper = TestDoubleCrashlyticsWrapper()
        )
    }

    /**
     * Resets the [getArticlesResult] to [successGetArticleResult] after each test.
     * This ensures that each test starts with a clean slate and a successful result for article fetching.
     */
    @After
    fun tearDown() {
        getArticlesResult = successGetArticleResult
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
    fun `validate is loading before a success response`() = runTest {
        val expectedStates: List<UiArticlesState> = listOf(
            UiArticlesState(isLoading = true, error = null, articles = emptyList()),
            UiArticlesState(
                isLoading = false,
                error = null,
                articles = ArticlesTestData.items.map { it.asUiArticle() }
            ),
        )
        val capturedStates = mutableListOf<UiArticlesState>()

        fetchArticlesIntentProcessor.processIntent { reducer: Reducer<UiArticlesState> ->
            capturedStates.add(UiArticlesState().reducer())
        }

        assertEquals(expectedStates.size, capturedStates.size)
        assertEquals(expectedStates[0].isLoading, capturedStates[0].isLoading)
        assertEquals(expectedStates[0].error, capturedStates[0].error)
        assertEquals(expectedStates[0].articles, capturedStates[0].articles)
        assertEquals(expectedStates[1].isLoading, capturedStates[1].isLoading)
        assertEquals(expectedStates[1].error, capturedStates[1].error)
        assertEquals(expectedStates[1].articles, capturedStates[1].articles)
    }

    /**
     * Validates the `FetchArticlesIntentProcessor`'s behavior when an error occurs during article fetching.
     *
     * This test verifies that the intent processor correctly emits a loading state (`isLoading = true`)
     * *before* transitioning to an error state (`isLoading = false`, `screenError` is not null). This ensures
     * that the UI displays a loading indicator while the data is being fetched, even if an error subsequently occurs.
     *
     * The test simulates a repository error and then checks for the following state sequence:
     * 1. Initial state: `isLoading = true`, `screenError = null`, `articles = emptyList()`.
     * 2. Error state: `isLoading = false`, `screenError` contains the error information, `articles = emptyList()`.
     *
     * This sequence confirms that the loading state is properly shown prior to the error state.
     */
    @Test
    fun `validate is loading before error fetching`() = runTest {
        val mockErrorMessage = "This is a error"
        val exception = Exception(mockErrorMessage)
        getArticlesResult = Result.failure(exception)
        val expectedStates: List<UiArticlesState> = listOf(
            UiArticlesState(isLoading = true, error = null, articles = emptyList()),
            UiArticlesState(
                isLoading = false,
                error = ErrorState(ArticlesErrors.NetworkError, exception.message),
                articles = emptyList()
            ),
        )
        val capturedStates = mutableListOf<UiArticlesState>()

        fetchArticlesIntentProcessor.processIntent { reducer: Reducer<UiArticlesState> ->
            capturedStates.add(UiArticlesState().reducer())
        }

        assertEquals(expectedStates.size, capturedStates.size)
        assertEquals(expectedStates[0].isLoading, capturedStates[0].isLoading)
        assertEquals(expectedStates[0].error, capturedStates[0].error)
        assertEquals(expectedStates[0].articles, capturedStates[0].articles)
        assertEquals(expectedStates[1].isLoading, capturedStates[1].isLoading)
        assertEquals(expectedStates[1].error, capturedStates[1].error)
        assertEquals(expectedStates[1].articles, capturedStates[1].articles)
    }
}