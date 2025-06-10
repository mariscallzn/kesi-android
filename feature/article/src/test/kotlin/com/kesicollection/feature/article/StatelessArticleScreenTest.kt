package com.kesicollection.feature.article

import android.app.Application
import android.os.Build
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import coil3.ColorImage
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.test.FakeImageLoaderEngine
import com.kesicollection.core.app.AnalyticsWrapper
import com.kesicollection.core.app.AppManager
import com.kesicollection.core.app.CrashlyticsWrapper
import com.kesicollection.core.app.Logger
import com.kesicollection.core.model.ErrorState
import com.kesicollection.core.uisystem.LocalApp
import com.kesicollection.core.uisystem.LocalImageLoader
import com.kesicollection.core.uisystem.component.KAdView
import com.kesicollection.core.uisystem.component.KMarkdown
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.article.uimodel.UiPodcast
import com.kesicollection.feature.article.utils.OnNavigateUpLambda
import com.kesicollection.feature.article.utils.OnPodcastIdClickLambda
import com.kesicollection.feature.article.utils.OnTryAgainLambda
import com.kesicollection.test.core.fake.FakeArticles
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import com.kesicollection.core.uisystem.R as UiSystemR

/**
 * UI tests for the [ArticleScreen] composable, focusing on its stateless behavior.
 *
 * This test class verifies that the [ArticleScreen] correctly displays different UI states
 * (loading, error, content) and handles user interactions (back navigation, podcast click,
 * try again) by invoking the appropriate callbacks.
 *
 * Tests included:
 * - `when loading, then shows loading indicator`
 * - `when error, then shows error content and try again button`
 * - `when Error, click Try Again then invokes Callback`
 * - `when content loaded without podcast, then shows article details and ad`
 * - `when content loaded with podcast, then shows podcast card`
 * - `when back button clicked, then invokes onNavigateUp`
 * - `when podcast card clicked, then invokes onPodcastClick`
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.M, Build.VERSION_CODES.TIRAMISU])
class StatelessArticleScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeImageLoaderEngine: FakeImageLoaderEngine
    /** Application context obtained from Robolectric. */
    private val applicationContext: Application = ApplicationProvider.getApplicationContext()

    /** Mock for the navigation up callback. */
    private val mockOnNavigateUp: OnNavigateUpLambda = mockk(relaxed = true)
    /** Mock for the podcast click callback. */
    private val mockOnPodcastClick: OnPodcastIdClickLambda = mockk(relaxed = true)
    /** Mock for the try again callback. */
    private val mockOnTryAgain: OnTryAgainLambda = mockk(relaxed = true)

    /** A fake article used for testing content display. */
    private val fakeArticle = FakeArticles.items.first()
    /** A fake podcast associated with the [fakeArticle]. */
    private val fakePodcast = fakeArticle.podcast!!

    @OptIn(ExperimentalCoilApi::class)
    /**
     * Sets up the test environment before each test.
     * Initializes the [fakeImageLoaderEngine] for Coil.
     */
    @Before
    fun setUp() {
        fakeImageLoaderEngine = FakeImageLoaderEngine.Builder()
            .default(ColorImage(Color.Blue.toArgb()))
            .build()
    }

    /**
     * Sets the content for the Compose test rule with the given [uiState].
     * This function provides the necessary dependencies like [ImageLoader] and [AppManager]
     * to the [ArticleScreen].
     *
     * @param uiState The [UiArticleState] to be rendered by the [ArticleScreen].
     */
    private fun setContent(uiState: UiArticleState) {
        val mockAnalyticsWrapper: AnalyticsWrapper = mockk(relaxed = true)
        val mockLogger: Logger = mockk(relaxed = true)
        val mockCrashlyticsWrapper: CrashlyticsWrapper = mockk(relaxed = true)

        val mockAppManager = object : AppManager {
            override val logger: Logger = mockLogger
            override val analytics: AnalyticsWrapper = mockAnalyticsWrapper
            override val crashlytics: CrashlyticsWrapper = mockCrashlyticsWrapper
        }

        composeTestRule.setContent {
            KesiTheme {
                CompositionLocalProvider(
                    LocalImageLoader provides ImageLoader.Builder(applicationContext)
                        .components { add(fakeImageLoaderEngine) }
                        .build(),
                    LocalApp provides mockAppManager
                ) {
                    ArticleScreen(
                        uiState = uiState,
                        adUnitId = "test-ad-unit-id",
                        onNavigateUp = mockOnNavigateUp::invoke,
                        onPodcastClick = mockOnPodcastClick::invoke,
                        onTryAgain = mockOnTryAgain::invoke
                    )
                }
            }
        }
    }

    /**
     * Verifies that the loading indicator is displayed when the UI state indicates loading.
     */
    @Test
    fun `when loading, then shows loading indicator`() {
        setContent(UiArticleState(isLoading = true))
        // Use the test tag defined in ArticleScreen.kt for LoadingArticle
        composeTestRule.onNodeWithTag(":feature:article:LoadingArticle").assertIsDisplayed()
    }

    /**
     * Verifies that the error message and "Try Again" button are displayed when the UI state
     * indicates an error.
     */
    @Test
    fun `when error, then shows error content and try again button`() {
        setContent(
            UiArticleState(
                isLoading = false,
                error = ErrorState(ArticleErrors.NetworkError)
            )
        )

        composeTestRule.onNodeWithText(applicationContext.getString(UiSystemR.string.core_uisystem_network_error))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(applicationContext.getString(UiSystemR.string.core_uisystem_try_again))
            .assertIsDisplayed()
    }

    /**
     * Verifies that the `onTryAgain` callback is invoked when the "Try Again" button is clicked
     * in the error state.
     */
    @Test
    fun `when Error, click Try Again then invokes Callback`() {
        setContent(
            UiArticleState(
                isLoading = false,
                error = ErrorState(ArticleErrors.NetworkError)
            )
        )

        composeTestRule.onNodeWithText(applicationContext.getString(UiSystemR.string.core_uisystem_try_again))
            .performClick()

        verify { mockOnTryAgain.invoke() }
    }

    /**
     * Verifies that the article details (title, content) and an ad view are displayed
     * when the content is loaded successfully and there is no podcast.
     */
    @Test
    fun `when content loaded without podcast, then shows article details and ad`() {
        val uiState = UiArticleState(
            isLoading = false,
            title = fakeArticle.title,
            imageUrl = fakeArticle.img,
            content = fakeArticle.markdown,
            podcast = null // Test without podcast first
        )
        setContent(uiState)

        // Check for Markdown content (KMarkdown renders text)
        // This assertion might be broad; consider more specific checks if needed.
        composeTestRule.onNodeWithTag(KMarkdown.TEST_TAG).assertExists()
        // Check for AdView
        composeTestRule.onNodeWithTag(KAdView.TEST_TAG).assertExists()
        // Check back button
        composeTestRule.onNodeWithContentDescription(
            applicationContext.getString(
                UiSystemR.string.core_uisystem_navigate_up
            )
        ).assertIsDisplayed() // Assuming empty content description for back arrow
    }

    /**
     * Verifies that the podcast card (title and play icon) is displayed when the content
     * is loaded successfully and a podcast is available.
     */
    @Test
    fun `when content loaded with podcast, then shows podcast card`() {
        val uiState = UiArticleState(
            isLoading = false,
            title = fakeArticle.title,
            imageUrl = fakeArticle.img,
            content = fakeArticle.markdown,
            podcast = UiPodcast(
                id = fakePodcast.id,
                title = fakePodcast.title,
                audio = fakePodcast.audio
            )
        )
        setContent(uiState)

        composeTestRule.onNodeWithText(fakePodcast.title).assertIsDisplayed()
        // Check for play icon in podcast card
        composeTestRule.onNodeWithContentDescription(applicationContext.getString(R.string.feature_article_play_podcast))
            .assertIsDisplayed()
    }

    /**
     * Verifies that the `onNavigateUp` callback is invoked when the back button is clicked.
     */
    @Test
    fun `when back button clicked, then invokes onNavigateUp`() {
        setContent(UiArticleState(isLoading = false, content = "Some content"))

        composeTestRule.onNodeWithContentDescription(applicationContext.getString(UiSystemR.string.core_uisystem_navigate_up))
            .performClick() // Assuming empty content description

        verify { mockOnNavigateUp.invoke() }
    }

    /**
     * Verifies that the `onPodcastClick` callback is invoked with the correct podcast ID
     * when the podcast card is clicked.
     */
    @Test
    fun `when podcast card clicked, then invokes onPodcastClick`() {
        val podcastId = fakePodcast.id
        val uiState = UiArticleState(
            isLoading = false,
            title = fakeArticle.title,
            imageUrl = fakeArticle.img,
            content = fakeArticle.markdown,
            podcast = UiPodcast(
                id = podcastId,
                title = fakePodcast.title,
                audio = fakePodcast.audio
            )
        )
        setContent(uiState)

        composeTestRule.onNodeWithText(fakePodcast.title).performClick()

        verify { mockOnPodcastClick.invoke(podcastId) }
    }
}