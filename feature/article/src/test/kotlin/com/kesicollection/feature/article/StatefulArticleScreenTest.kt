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
import com.kesicollection.core.uisystem.component.KMarkdown
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.article.utils.OnNavigateUpLambda
import com.kesicollection.feature.article.utils.OnPodcastIdClickLambda
import com.kesicollection.feature.article.utils.OnTryAgainLambda
import com.kesicollection.test.core.fake.FakeArticles
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
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
 * Stateful tests for the [ArticleScreen] composable.
 *
 * Test cases:
 * - `when content is loading, then show loading indicator`
 * - `when content is loaded successfully, then show article content`
 * - `when content loading fails, then show error message`
 * - `when podcast is available, then show podcast card`
 * - `when try again is clicked on error, then fetch article intent is sent`
 * - `when navigate up is clicked, then onNavigateUp is invoked`
 * - `when podcast card is clicked, then onPodcastClick is invoked`
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.M, Build.VERSION_CODES.TIRAMISU])
class StatefulArticleScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeImageLoaderEngine: FakeImageLoaderEngine
    private lateinit var mockAppManager: AppManager
    private lateinit var mockAnalyticsWrapper: AnalyticsWrapper
    private lateinit var mockLogger: Logger
    private lateinit var mockCrashlyticsWrapper: CrashlyticsWrapper
    private lateinit var mockOnNavigateUp: OnNavigateUpLambda
    private lateinit var mockOnPodcastClick: OnPodcastIdClickLambda
    private lateinit var mockOnTryAgain: OnTryAgainLambda

    private val applicationContext: Application = ApplicationProvider.getApplicationContext()

    private val fakeArticle = FakeArticles.items.first()
    private val fakeAdUnitId = "test-ad-unit-id"

    @OptIn(ExperimentalCoilApi::class)
    @Before
    fun setUp() {
        fakeImageLoaderEngine = FakeImageLoaderEngine.Builder()
            .default(coil3.ColorImage(Color.Blue.toArgb()))
            .build()

        mockAnalyticsWrapper = mockk {
            every { logEvent(any(), any()) } just Runs
        }
        mockLogger = mockk(relaxed = true)
        mockCrashlyticsWrapper = mockk(relaxed = true)
        mockAppManager = mockk {
            every { analytics } returns mockAnalyticsWrapper
            every { logger } returns mockLogger
            every { crashlytics } returns mockCrashlyticsWrapper
        }
        mockOnNavigateUp = mockk(relaxed = true)
        mockOnPodcastClick = mockk(relaxed = true)
        mockOnTryAgain = mockk(relaxed = true)
    }

    @OptIn(ExperimentalCoilApi::class)
    private fun setContent(uiState: UiArticleState) {
        composeTestRule.setContent {
            CompositionLocalProvider(
                LocalApp provides mockAppManager,
                LocalImageLoader provides ImageLoader.Builder(applicationContext)
                    .components { add(fakeImageLoaderEngine) }
                    .build(),
            ) {
                KesiTheme {
                    ArticleScreen(
                        uiState = uiState,
                        adUnitId = fakeAdUnitId,
                        onNavigateUp = mockOnNavigateUp::invoke,
                        onPodcastClick = mockOnPodcastClick::invoke,
                        onTryAgain = mockOnTryAgain::invoke
                    )
                }
            }
        }
    }

    @Test
    fun `when content is loading, then show loading indicator`() {
        setContent(initialState.copy(isLoading = true, adKey = fakeAdUnitId))
        composeTestRule.onNodeWithTag(":feature:article:LoadingArticle").assertIsDisplayed()
    }

    @Test
    fun `when content is loaded successfully, then show article content`() {
        val loadedState = UiArticleState(
            isLoading = false,
            title = fakeArticle.title,
            imageUrl = fakeArticle.img,
            content = fakeArticle.markdown,
            adKey = fakeAdUnitId
        )
        setContent(loadedState)

        composeTestRule.onNodeWithTag(KMarkdown.TEST_TAG).assertExists()
    }

    @Test
    fun `when content loading fails, then show error message`() {
        val errorState = UiArticleState(
            isLoading = false,
            error = ErrorState(ArticleErrors.NetworkError, "Network error occurred"), // Message can be anything for this test
            adKey = fakeAdUnitId
        )
        setContent(errorState)

        // Use the string resource defined in ShowError.kt (core.uisystem.R)
        composeTestRule.onNodeWithText(applicationContext.getString(UiSystemR.string.core_uisystem_network_error)).assertIsDisplayed()
        composeTestRule.onNodeWithText(applicationContext.getString(UiSystemR.string.core_uisystem_try_again)).assertIsDisplayed()
    }

    @Test
    fun `when podcast is available, then show podcast card`() {
        val articleWithPodcast = FakeArticles.items.first { it.podcast != null }
        val podcastUi = articleWithPodcast.podcast?.let {
            com.kesicollection.feature.article.uimodel.UiPodcast(
                id = it.id,
                title = it.title,
                audio = it.audio
            )
        }
        val stateWithPodcast = UiArticleState(
            isLoading = false,
            title = articleWithPodcast.title,
            imageUrl = articleWithPodcast.img,
            content = articleWithPodcast.markdown,
            podcast = podcastUi,
            adKey = fakeAdUnitId
        )
        setContent(stateWithPodcast)

        composeTestRule.onNodeWithText(podcastUi!!.title).assertIsDisplayed()
    }

    @Test
    fun `when try again is clicked on error, then fetch article intent is sent`() {
        val errorState = UiArticleState(
            isLoading = false,
            error = ErrorState(ArticleErrors.NetworkError), // No specific message needed for this test
            adKey = fakeAdUnitId
        )
        setContent(errorState)

        // Use the string resource for the button defined in ShowError.kt
        composeTestRule.onNodeWithText(applicationContext.getString(UiSystemR.string.core_uisystem_try_again)).performClick()
        verify(exactly = 1) { mockOnTryAgain.invoke() }
    }

    @Test
    fun `when navigate up is clicked, then onNavigateUp is invoked`() {
        val loadedState = UiArticleState(
            isLoading = false,
            title = fakeArticle.title,
            imageUrl = fakeArticle.img,
            content = fakeArticle.markdown,
            adKey = fakeAdUnitId
        )
        setContent(loadedState)
        composeTestRule.onNodeWithContentDescription(applicationContext.getString(UiSystemR.string.core_uisystem_navigate_up)).performClick()
        verify(exactly = 1) { mockOnNavigateUp.invoke() }
    }

    @Test
    fun `when podcast card is clicked, then onPodcastClick is invoked`() {
        val articleWithPodcast = FakeArticles.items.first { it.podcast != null }
        val podcastUi = articleWithPodcast.podcast?.let {
            com.kesicollection.feature.article.uimodel.UiPodcast(
                id = it.id,
                title = it.title,
                audio = it.audio
            )
        }
        val stateWithPodcast = UiArticleState(
            isLoading = false,
            title = articleWithPodcast.title,
            imageUrl = articleWithPodcast.img,
            content = articleWithPodcast.markdown,
            podcast = podcastUi,
            adKey = fakeAdUnitId
        )
        setContent(stateWithPodcast)

        composeTestRule.onNodeWithText(podcastUi!!.title).performClick()
        verify(exactly = 1) { mockOnPodcastClick.invoke(podcastUi.id) }
    }
}