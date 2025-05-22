package com.kesicollection.feature.article.intent

import com.kesicollection.core.model.ErrorState
import com.kesicollection.data.usecase.GetArticleByIdUseCase
import com.kesicollection.feature.article.ArticleErrors
import com.kesicollection.feature.article.UiArticleState
import com.kesicollection.feature.article.components.Code
import com.kesicollection.feature.article.initialState
import com.kesicollection.feature.article.uimodel.asUiPodcast
import com.kesicollection.testing.api.TestDoubleArticleRepository
import com.kesicollection.testing.api.getArticleByIdResult
import com.kesicollection.testing.api.successGetArticleById
import com.kesicollection.testing.testdata.ArticlesTestData
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test class for [FetchArticleIntentProcessor].
 *
 * This class contains unit tests to verify the behavior of [FetchArticleIntentProcessor]
 * in various scenarios, including successful article fetching and error handling.
 *
 * It uses a test double [TestDoubleArticleRepository] to simulate interactions with a
 * real article repository and [ArticlesTestData] to provide test article data.
 *
 * The tests validate the correct emission of loading states (`isLoading`) and the
 * accurate representation of fetched article data or errors in the [UiArticleState].
 *
 * The test class uses the following lists:
 * - `expectedStates`: To define the expected states that the [FetchArticleIntentProcessor] should emit.
 * - `actualStates`: To capture the states emitted by the [FetchArticleIntentProcessor] during the test execution.
 * - `@Test`:
 *     - `validate is loading before success response`: Verifies that the `FetchArticleIntentProcessor` correctly emits a loading state before successfully fetching and emitting the article details.
 *     - `validate is loading before a failure`: Validates that the `isLoading` state is set to `true` before a failure occurs when fetching an article.
 */
class FetchArticleIntentProcessorTest {

    private lateinit var fetchArticleIntentProcessor: FetchArticleIntentProcessor

    /**
     * Sets up the test environment before each test.
     *
     * This function initializes the [FetchArticleIntentProcessor] with a test double
     * [TestDoubleArticleRepository] and uses the first article ID from [ArticlesTestData]
     * to be fetched.
     */
    @Before
    fun setup() {
        fetchArticleIntentProcessor = FetchArticleIntentProcessor(
            articleId = ArticlesTestData.items.first().id,
            getArticleByIdUseCase = GetArticleByIdUseCase(
                articleRepository = TestDoubleArticleRepository()
            )
        )
    }

    /**
     * Resets the `getArticleByIdResult` to its default successful state after each test.
     * This ensures that subsequent tests start with a clean slate and are not affected by
     * the outcomes of previous tests.
     */
    @After
    fun tearDown() {
        getArticleByIdResult = successGetArticleById
    }

    /**
     * This test verifies that the `FetchArticleIntentProcessor` correctly emits a loading state
     * (`isLoading = true`) before successfully fetching and emitting the article details.
     *
     * It checks the following:
     * 1. The processor emits two states: one for loading and one for success.
     * 2. The first state has `isLoading` set to `true`.
     * 3. The second state has `isLoading` set to `false` and contains the expected article data
     *    (title, image URL, podcast, content) without any errors.
     * 4. All properties of the states are correctly set.
     *
     * The test uses a predefined article from `ArticlesTestData` and simulates a successful response
     * from the `GetArticleByIdUseCase`.
     */
    @Test
    fun `validate is loading before success response`() = runTest {
        val article = ArticlesTestData.items.first()
        val expectedStates = listOf(
            initialState.copy(isLoading = true),
            UiArticleState(
                isLoading = false,
                title = article.title,
                imageUrl = article.thumbnail,
                podcast = article.podcast?.asUiPodcast(),
                //For simplicity all is treat it as Code since don't care about the type in this test case
                content = article.content.map { Code(it.content) },
                error = null
            )
        )
        val actualStates = mutableListOf<UiArticleState>()
        fetchArticleIntentProcessor.processIntent {
            actualStates.add(UiArticleState().it())
        }
        assertEquals(expectedStates.size, actualStates.size)
        assertEquals(expectedStates[0], actualStates[0])
        assertEquals(expectedStates[1].isLoading, actualStates[1].isLoading)
        assertEquals(expectedStates[1].title, actualStates[1].title)
        assertEquals(expectedStates[1].imageUrl, actualStates[1].imageUrl)
        assertEquals(expectedStates[1].podcast, actualStates[1].podcast)
        assertEquals(expectedStates[1].content.size, actualStates[1].content.size)
        assertEquals(expectedStates[1].error, actualStates[1].error)
    }

    /**
     * Validates that the `isLoading` state is set to `true` before a failure occurs when fetching an article.
     *
     * This test verifies the following:
     * 1. The first state emitted by the [FetchArticleIntentProcessor] should have `isLoading` set to `true`.
     * 2. When the use case returns a failure result, the processor emits a second state.
     * 3. The second state should have `isLoading` set to `false`.
     * 4. The second state should contain an `ErrorState` with the expected error type ([ArticleErrors.NetworkError])
     *    and the error message from the simulated exception.
     * 5. Both the `isLoading` status and the `ErrorState` in the emitted states should match the expected values.
     */
    @Test
    fun `validate is loading before a failure`() = runTest {
        val mockErrorMessage = "TestError"
        val exception = Exception(mockErrorMessage)
        getArticleByIdResult = Result.failure(exception)
        val expectedStates = listOf(
            initialState.copy(isLoading = true),
            UiArticleState(
                isLoading = false,
                error = ErrorState(ArticleErrors.NetworkError, mockErrorMessage)
            )
        )
        val actualStates = mutableListOf<UiArticleState>()
        fetchArticleIntentProcessor.processIntent {
            actualStates.add(UiArticleState().it())
        }
        assertEquals(expectedStates.size, actualStates.size)
        assertEquals(expectedStates[0], actualStates[0])
        assertEquals(expectedStates[1].isLoading, actualStates[1].isLoading)
        assertEquals(expectedStates[1].error, actualStates[1].error)
    }
}