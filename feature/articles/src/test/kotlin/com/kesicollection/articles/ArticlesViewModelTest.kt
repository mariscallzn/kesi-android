package com.kesicollection.articles

import com.google.common.truth.Truth.assertThat
import com.kesicollection.core.app.IntentProcessor
import com.kesicollection.core.app.IntentProcessorFactory
import com.kesicollection.core.app.Reducer
import com.kesicollection.core.model.ErrorState
import com.kesicollection.articles.model.asUiArticle
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for the [ArticlesViewModel].
 *
 * This class tests the following aspects of the ViewModel:
 * - Initial UI state.
 * - Correct creation and usage of [IntentProcessor] upon sending an [Intent].
 * - State updates via the processor invoking a reducer for [Intent.FetchArticles] success.
 * - State updates via the processor invoking a reducer for [Intent.BookmarkClicked].
 * - State updates to an error state when [Intent.FetchArticles] processing indicates failure.
 *
 * Test methods:
 * - `initial UI state is correct`
 * - `sendIntent creates and uses correct IntentProcessor`
 * - `sendIntent - FetchArticles - updates state via processor invoking reducer`
 * - `sendIntent - BookmarkClicked - updates state via processor invoking reducer`
 * - `sendIntent - FetchArticles - updates state to error when processor indicates failure`
 */
@ExperimentalCoroutinesApi
class ArticlesViewModelTest {

    // Test dispatcher for coroutines, as per guidelines
    private val testDispatcher = StandardTestDispatcher()

    // Mocks for dependencies
    private lateinit var mockIntentProcessorFactory: IntentProcessorFactory<UiArticlesState, Intent>
    /** Mock [IntentProcessor] to simulate intent processing logic. */
    private lateinit var mockIntentProcessor: IntentProcessor<UiArticlesState>

    // ViewModel instance
    private lateinit var viewModel: ArticlesViewModel

    private val testAdKey = "test_ad_key"
    private val initialUiState: UiArticlesState
        get() = UiArticlesState(isLoading = true, adKey = testAdKey)


    @Before
    fun setUp() {
        // Initialize mocks
        mockIntentProcessorFactory = mockk()
        mockIntentProcessor = mockk(relaxed = true) // relaxed = true to ignore void functions/suspend functions

        // Stub the create method of the factory to return our mock processor
        every { mockIntentProcessorFactory.create(any()) } returns mockIntentProcessor

        // Initialize ViewModel
        viewModel = ArticlesViewModel(
            dispatcher = testDispatcher, // Use the testDispatcher
            intentProcessorFactory = mockIntentProcessorFactory,
            adKey = testAdKey
        )
    }

    @Test
    fun `initial UI state is correct`() = runTest(testDispatcher) {
        assertThat(viewModel.uiState.first()).isEqualTo(initialUiState)
    }

    @Test
    fun `sendIntent creates and uses correct IntentProcessor`() = runTest(testDispatcher) {
        val testIntent = Intent.FetchArticles

        // Act
        viewModel.sendIntent(testIntent)
        testDispatcher.scheduler.advanceUntilIdle() // Ensure coroutines launched by sendIntent complete

        // Assert
        verify { mockIntentProcessorFactory.create(testIntent) }
        coVerify { mockIntentProcessor.processIntent(any()) }
    }

    @Test
    fun `sendIntent - FetchArticles - updates state via processor invoking reducer`() = runTest(testDispatcher) {
        val fetchArticlesIntent = Intent.FetchArticles
        val fakeApiArticles = com.kesicollection.test.core.fake.FakeArticles.items
        val expectedUiArticles = fakeApiArticles.map { it.asUiArticle() }
        val successState = initialUiState.copy(articles = expectedUiArticles, isLoading = false)

        val reducerCallbackSlot = slot<(Reducer<UiArticlesState>) -> Unit>()
        coEvery { mockIntentProcessor.processIntent(capture(reducerCallbackSlot)) } coAnswers {
            val stateTransform: Reducer<UiArticlesState> = { successState }
            reducerCallbackSlot.captured.invoke(stateTransform)
        }

        viewModel.sendIntent(fetchArticlesIntent)
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value).isEqualTo(successState)
    }

    @Test
    fun `sendIntent - BookmarkClicked - updates state via processor invoking reducer`() = runTest(testDispatcher) {
        val articleIdToBookmark = "1"
        val bookmarkIntent = Intent.BookmarkClicked(articleIdToBookmark)

        val initialLoadedArticles = com.kesicollection.test.core.fake.FakeArticles.items.map { it.asUiArticle() }

        val reducerCallbackSlot = slot<(Reducer<UiArticlesState>) -> Unit>()
        coEvery { mockIntentProcessor.processIntent(capture(reducerCallbackSlot)) } coAnswers {
            // For the purpose of testing the ViewModel's reducer mechanism, we often prime the
            // ViewModel's state to a known value *before* the intent is processed.
            // But since the ViewModel's state is private and updated via this exact mechanism,
            // we'll set up the processor's reducer to correctly transform the *initialUiState*.
            val processorReducerLogic: Reducer<UiArticlesState> = {
                this.copy(
                    articles = initialLoadedArticles.map { uiArticle ->
                        if (uiArticle.articleId == articleIdToBookmark) {
                            uiArticle.copy(isBookmarked = !uiArticle.isBookmarked) // Toggle current bookmarked state
                        } else {
                            uiArticle
                        }
                    },
                    isLoading = false // Assume loading finishes
                )
            }
            reducerCallbackSlot.captured.invoke(processorReducerLogic)
        }

        // Act
        viewModel.sendIntent(bookmarkIntent)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        verify { mockIntentProcessorFactory.create(bookmarkIntent) }
        val finalState = viewModel.uiState.value
        val bookmarkedArticle = finalState.articles.find { it.articleId == articleIdToBookmark }

        assertThat(bookmarkedArticle).isNotNull()
        // The expected bookmark state depends on its state in `initialLoadedArticles`
        // Let's find the original state from fake data to confirm the toggle
        val originalArticle = com.kesicollection.test.core.fake.FakeArticles.items.find {it.id == articleIdToBookmark}
        val originalUiArticle = originalArticle?.asUiArticle() // isBookmarked is false by default in asUiArticle

        assertThat(bookmarkedArticle?.isBookmarked).isEqualTo(!originalUiArticle!!.isBookmarked) // It should be toggled
        assertThat(finalState.isLoading).isFalse()
    }

    @Test
    fun `sendIntent - FetchArticles - updates state to error when processor indicates failure`() = runTest(testDispatcher) {
        val fetchArticlesIntent = Intent.FetchArticles
        val specificErrorType = ArticlesErrors.NetworkError // Example error from your enum
        val errorMessage = "Failed to connect to the server"

        // Correctly create the ErrorState using your data class
        val expectedErrorObject = ErrorState(type = specificErrorType, message = errorMessage)
        val expectedErrorUiState = initialUiState.copy(
            isLoading = false,
            error = expectedErrorObject
        )

        val reducerCallbackSlot = slot<(Reducer<UiArticlesState>) -> Unit>()
        coEvery { mockIntentProcessor.processIntent(capture(reducerCallbackSlot)) } coAnswers {
            val stateTransform: Reducer<UiArticlesState> = { expectedErrorUiState }
            reducerCallbackSlot.captured.invoke(stateTransform)
        }

        viewModel.sendIntent(fetchArticlesIntent)
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value).isEqualTo(expectedErrorUiState)
        assertThat(viewModel.uiState.value.error).isNotNull()
        assertThat(viewModel.uiState.value.error?.type).isEqualTo(specificErrorType)
        assertThat(viewModel.uiState.value.error?.message).isEqualTo(errorMessage)
    }
}