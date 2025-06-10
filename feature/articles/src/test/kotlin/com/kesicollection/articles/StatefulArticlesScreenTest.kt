package com.kesicollection.articles

import android.app.Application
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import coil3.ColorImage
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.test.FakeImageLoaderEngine
import com.kesicollection.articles.model.asUiArticle
import com.kesicollection.articles.utils.OnArticleScreenArticleClick
import com.kesicollection.articles.utils.OnNavigateUp
import com.kesicollection.core.app.AppManager
import com.kesicollection.core.app.IntentProcessor
import com.kesicollection.core.app.IntentProcessorFactory
import com.kesicollection.core.model.ErrorState
import com.kesicollection.core.uisystem.LocalApp
import com.kesicollection.core.uisystem.LocalImageLoader
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.test.core.fake.FakeArticles
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import com.kesicollection.core.uisystem.R as CoreUiSystemR

/**
 * Test suite for [ArticlesScreen] when it's stateful, meaning it interacts with a [ArticlesViewModel].
 *
 * This class tests the UI behavior of the Articles screen under various states provided by the ViewModel,
 * such as loading, displaying articles, showing errors, and handling user interactions like
 * clicking an article, navigating up, retrying after an error, and bookmarking articles.
 *
 * Tests included:
 * - `when loading, shows loading indicator`
 * - `when articles loaded, displays articles list`
 * - `when error, displays error message and retry button`
 * - `clicking an article invokes onArticleClick`
 * - `clicking navigate up invokes onNavigateUp`
 * - `when screen is viewed, analytics event is logged`
 * - `when retry is clicked after error, FetchArticles intent is sent and analytics logged`
 * - `clicking bookmark on an article sends BookmarkClicked intent and logs analytics`
 */
@OptIn(ExperimentalCoroutinesApi::class, ExperimentalCoilApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.M, Build.VERSION_CODES.TIRAMISU])
class StatefulArticlesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeImageLoaderEngine: FakeImageLoaderEngine
    private lateinit var imageLoader: ImageLoader

    private val applicationContext: Application = ApplicationProvider.getApplicationContext()
    private val mockAppManager: AppManager = mockk(relaxed = true)
    private val mockIntentProcessorFactory: IntentProcessorFactory<UiArticlesState, Intent> =
        mockk()
    private val mockFetchArticlesProcessor: IntentProcessor<UiArticlesState> = mockk()
    private val mockBookmarkClickedProcessor: IntentProcessor<UiArticlesState> = mockk()
    private val viewModel: ArticlesViewModel = mockk(relaxed = true)
    private val onArticleClick: OnArticleScreenArticleClick = mockk(relaxed = true)
    private val onNavigateUp: OnNavigateUp = mockk(relaxed = true)

    /**
     * Sets up the testing environment before each test.
     * Initializes the fake image loader and mocks for ViewModel dependencies.
     */
    @Before
    fun setUp() {
        fakeImageLoaderEngine = FakeImageLoaderEngine.Builder()
            .default(ColorImage(Color.Blue.toArgb()))
            .build()
        imageLoader = ImageLoader.Builder(applicationContext)
            .components { add(fakeImageLoaderEngine) }
            .build()

        // Mock processor factory behavior if needed by setupViewModelAndState
        every { mockIntentProcessorFactory.create(any<Intent.FetchArticles>()) } returns mockFetchArticlesProcessor
        every { mockIntentProcessorFactory.create(any<Intent.BookmarkClicked>()) } returns mockBookmarkClickedProcessor

        // Basic behavior for mocked processors
        coEvery { mockFetchArticlesProcessor.processIntent(any()) } just Runs
        coEvery { mockBookmarkClickedProcessor.processIntent(any()) } just Runs
    }

    /**
     * Configures the mocked [ArticlesViewModel] with a specific initial [UiArticlesState].
     * It also mocks the `sendIntent` method to simulate state changes based on the dispatched [Intent].
     *
     * @param initialUiState The initial state to set for the ViewModel's UI state flow.
     */
    private fun setupViewModelAndState(initialUiState: UiArticlesState) {
        // Mock the uiState flow directly on the mocked ViewModel
        val mutableUiStateFlow = MutableStateFlow(initialUiState)
        every { viewModel.uiState } returns mutableUiStateFlow

        // If your screen calls sendIntent, mock its behavior.
        // This is crucial for tests that verify intents are sent or that state changes upon intent.
        val capturedIntent = slot<Intent>()
        every { viewModel.sendIntent(capture(capturedIntent)) } answers {
            // Simulate processing based on the captured intent
            // This allows tests to verify that sending an intent leads to an expected state change
            // or that a specific processor is (indirectly) called.
            val currentIntent = capturedIntent.captured
            when (currentIntent) {
                is Intent.FetchArticles -> {
                    // Example: Simulate FetchArticles intent leading to a loading state
                    // You might also invoke a mocked processor here if its side effects are complex
                    // For instance: coEvery { mockFetchArticlesProcessor.processIntent(...) }
                    // Here, we directly update the state for simplicity in this example.
                    mutableUiStateFlow.value =
                        mutableUiStateFlow.value.copy(isLoading = true, error = null)
                    // If you want to verify the processor itself was created and called:
                    // mockFetchArticlesProcessor.processIntent { reducer -> mutableUiStateFlow.value = reducer(mutableUiStateFlow.value) }
                }

                is Intent.BookmarkClicked -> {
                    // Example: Simulate BookmarkClicked intent toggling a bookmark
                    val articleId = currentIntent.articleId
                    val currentArticles = mutableUiStateFlow.value.articles
                    val updatedArticles = currentArticles.map {
                        if (it.articleId == articleId) it.copy(isBookmarked = !it.isBookmarked) else it
                    }
                    mutableUiStateFlow.value =
                        mutableUiStateFlow.value.copy(articles = updatedArticles)
                }
            }
        }
    }

    /**
     * Verifies that the loading indicator is displayed when the UI state indicates loading.
     */
    @Test
    fun `when loading, shows loading indicator`() {
        setupViewModelAndState(
            initialUiState = UiArticlesState(
                isLoading = true,
                articles = emptyList()
            )
        )
        composeTestRule.setContent {
            TestAppProviders {
                ArticlesScreen(
                    onArticleClick = onArticleClick::invoke,
                    onNavigateUp = onNavigateUp::invoke,
                    viewModel = viewModel // Pass the mocked ViewModel
                )
            }
        }
        composeTestRule.onNodeWithTag(":feature:articles:loading").assertIsDisplayed()
    }

    /**
     * Verifies that the list of articles is displayed when the UI state contains articles.
     */
    @Test
    fun `when articles loaded, displays articles list`() {
        val articles = FakeArticles.items.take(2).map { it.asUiArticle() }
        setupViewModelAndState(
            initialUiState = UiArticlesState(
                isLoading = false,
                articles = articles
            )
        )

        composeTestRule.setContent {
            TestAppProviders {
                ArticlesScreen(
                    onArticleClick = onArticleClick::invoke,
                    onNavigateUp = onNavigateUp::invoke,
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onNodeWithTag(":feature:articles:articles").assertIsDisplayed()
        articles.forEach { article ->
            composeTestRule.onNodeWithText(article.title).assertIsDisplayed()
            composeTestRule.onNodeWithText(article.description).assertIsDisplayed()
        }
    }

    /**
     * Verifies that an error message and a retry button are displayed when the UI state indicates an error.
     * It checks for specific error text and the presence of the "Try Again" button.
     */
    @Test
    fun `when error, displays error message and retry button`() {
        setupViewModelAndState(
            initialUiState = UiArticlesState(
                isLoading = false,
                error = ErrorState(ArticlesErrors.NetworkError)
            )
        )

        composeTestRule.setContent {
            TestAppProviders {
                ArticlesScreen(
                    onArticleClick = onArticleClick::invoke,
                    onNavigateUp = onNavigateUp::invoke,
                    viewModel = viewModel
                )
            }
        }
        composeTestRule.onNodeWithText(applicationContext.getString(CoreUiSystemR.string.core_uisystem_network_error))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(applicationContext.getString(CoreUiSystemR.string.core_uisystem_try_again))
            .assertIsDisplayed()
    }

    /**
     * Verifies that clicking on an article in the list triggers the `onArticleClick` callback
     * with the correct article ID.
     */
    @Test
    fun `clicking an article invokes onArticleClick`() {
        val article = FakeArticles.items.first().asUiArticle()
        setupViewModelAndState(
            initialUiState = UiArticlesState(
                isLoading = false,
                articles = listOf(article)
            )
        )

        composeTestRule.setContent {
            TestAppProviders {
                ArticlesScreen(
                    onArticleClick = onArticleClick::invoke,
                    onNavigateUp = onNavigateUp::invoke,
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onNodeWithText(article.title).performClick()
        verify(exactly = 1) { onArticleClick.invoke(article.articleId) }
    }

    /**
     * Verifies that clicking the navigate up button triggers the `onNavigateUp` callback.
     */
    @Test
    fun `clicking navigate up invokes onNavigateUp`() {
        setupViewModelAndState(initialUiState = UiArticlesState(isLoading = false))
        composeTestRule.setContent {
            TestAppProviders {
                ArticlesScreen(
                    onArticleClick = onArticleClick::invoke,
                    onNavigateUp = onNavigateUp::invoke,
                    viewModel = viewModel
                )
            }
        }

        val navigateUpDescription =
            applicationContext.getString(CoreUiSystemR.string.core_uisystem_navigate_up)
        composeTestRule.onNodeWithContentDescription(navigateUpDescription).performClick()

        verify(exactly = 1) { onNavigateUp.invoke() }
    }

    /**
     * Verifies that an analytics event is logged when the [ArticlesScreen] is displayed.
     * This relies on a `SideEffect` within the `ArticlesScreen` composable.
     */
    @Test
    fun `when screen is viewed, analytics event is logged`() {
        setupViewModelAndState(initialUiState = UiArticlesState(isLoading = false))
        composeTestRule.setContent {
            TestAppProviders {
                ArticlesScreen(
                    onArticleClick = onArticleClick::invoke,
                    onNavigateUp = onNavigateUp::invoke,
                    viewModel = viewModel
                )
            }
        }
        // Analytics are logged in a SideEffect in ArticlesScreen,
        // so this should still work as expected.
        verify {
            mockAppManager.analytics.logEvent(any(), any())
        }
    }

    /**
     * Verifies that clicking the "Try Again" button after an error:
     * 1. Sends a `FetchArticles` intent to the ViewModel.
     * 2. Logs an analytics event.
     */
    @Test
    fun `when retry is clicked after error, FetchArticles intent is sent and analytics logged`() {
        // Initial state is error
        setupViewModelAndState(
            initialUiState = UiArticlesState(
                isLoading = false,
                error = ErrorState(ArticlesErrors.NetworkError)
            )
        )

        // Specific mock for sendIntent for this test to verify it's called with FetchArticles
        // and to simulate the subsequent state change for this specific interaction.
        // This overrides the general sendIntent mock in setupViewModelAndState for this test case if needed,
        // or you can rely on the general one if its simulation is sufficient.
        // For clarity, we can ensure the general setup handles the FetchArticles state change.
        // The `setupViewModelAndState` already prepares `viewModel.sendIntent` to change state for FetchArticles.

        composeTestRule.setContent {
            TestAppProviders {
                ArticlesScreen(
                    onArticleClick = onArticleClick::invoke,
                    onNavigateUp = onNavigateUp::invoke,
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onNodeWithText(applicationContext.getString(CoreUiSystemR.string.core_uisystem_try_again))
            .performClick()

        // Verify that the mocked ViewModel's sendIntent was called with FetchArticles
        verify { viewModel.sendIntent(Intent.FetchArticles) }

        // Verify analytics
        verify {
            mockAppManager.analytics.logEvent(any(), any())
        }

        // Optionally, assert state change if your sendIntent mock updates the state
        // This depends on the setupViewModelAndState's sendIntent mock.
        // For example, if it sets isLoading to true:
        // assertThat(viewModel.uiState.value.isLoading).isTrue()
        // assertThat(viewModel.uiState.value.error).isNull()
    }

    /**
     * Verifies that clicking the bookmark icon on an article:
     * 1. Sends a `BookmarkClicked` intent to the ViewModel with the correct article ID.
     * 2. Logs an analytics event.
     */
    @Test
    fun `clicking bookmark on an article sends BookmarkClicked intent and logs analytics`() {
        val articleToBookmark = FakeArticles.items.first().asUiArticle().copy(isBookmarked = false)
        setupViewModelAndState(
            initialUiState = UiArticlesState(
                isLoading = false,
                articles = listOf(articleToBookmark)
            )
        )

        // The general setupViewModelAndState's sendIntent mock should handle state changes for BookmarkClicked.

        composeTestRule.setContent {
            TestAppProviders {
                ArticlesScreen(
                    onArticleClick = onArticleClick::invoke,
                    onNavigateUp = onNavigateUp::invoke,
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onAllNodesWithTag(
            ":feature:articles:bookmarkButton",
            useUnmergedTree = true
        )[0].performClick()

        // Verify that the mocked ViewModel's sendIntent was called with the correct BookmarkClicked intent
        verify { viewModel.sendIntent(Intent.BookmarkClicked(articleToBookmark.articleId)) }

        // Verify analytics
        verify {
            mockAppManager.analytics.logEvent(any(), any())
        }
        // Optionally, verify state change if your sendIntent mock in setupViewModelAndState updates the state correctly.
        // For example, to check if the article is now bookmarked:
        // val updatedArticle = viewModel.uiState.value.articles.find { it.articleId == articleToBookmark.articleId }
        // assertThat(updatedArticle?.isBookmarked).isTrue()
    }

    /**
     * A utility composable function that provides the necessary [CompositionLocalProvider]s
     * for testing, such as [LocalApp] for `AppManager` and [LocalImageLoader] for Coil.
     *
     * @param content The composable content to be rendered within these providers.
     */
    @Composable
    private fun TestAppProviders(content: @Composable () -> Unit) {
        CompositionLocalProvider(
            LocalApp provides mockAppManager,
            LocalImageLoader provides imageLoader,
        ) {
            KesiTheme {
                content()
            }
        }
    }
}