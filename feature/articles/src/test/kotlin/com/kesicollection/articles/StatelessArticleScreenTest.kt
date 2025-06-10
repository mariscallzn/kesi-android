package com.kesicollection.articles

import android.app.Application
import android.os.Build
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToString
import androidx.test.core.app.ApplicationProvider
import coil3.ColorImage
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.test.FakeImageLoaderEngine
import com.google.common.truth.Truth.assertThat
import com.kesicollection.articles.model.asUiArticle
import com.kesicollection.articles.utils.OnArticleScreenArticleClick
import com.kesicollection.articles.utils.OnArticleScreenBookmarkClick
import com.kesicollection.articles.utils.OnNavigateUp
import com.kesicollection.articles.utils.OnTryAgain
import com.kesicollection.core.model.ErrorState
import com.kesicollection.core.uisystem.LocalApp
import com.kesicollection.core.uisystem.LocalImageLoader
import com.kesicollection.core.uisystem.PreviewAppManager
import com.kesicollection.core.uisystem.component.KAdView
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.test.core.fake.FakeArticles
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import com.kesicollection.core.uisystem.R as CoreUiSystemR


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.M, Build.VERSION_CODES.TIRAMISU])
/**
 * Tests for the [ArticlesScreen] composable, focusing on its stateless behavior and UI rendering
 * based on different [UiArticlesState] inputs.
 *
 * Test cases:
 * - `when loading, should display loading indicator`
 * - `when articles loaded, should display articles list and ad view`
 * - `when network error, should display error message and not ad view`
 * - `when generic error, should display error message and not ad view`
 * - `clicking an article should invoke onArticleClick callback`
 * - `clicking bookmark on an article should invoke onBookmarkClick callback`
 * - `clicking try again on error should invoke onTryAgain callback`
 * - `clicking navigation up should invoke onNavigateUp callback`
 * - `when articles list is empty and not loading and no error, should display ad view`
 */
class StatelessArticleScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeImageLoaderEngine: FakeImageLoaderEngine
    private val applicationContext: Application = ApplicationProvider.getApplicationContext()

    // Mock callback interfaces
    private val onArticleClick: OnArticleScreenArticleClick = mockk(relaxed = true)
    private val onBookmarkClick: OnArticleScreenBookmarkClick = mockk(relaxed = true)
    private val onTryAgain: OnTryAgain = mockk(relaxed = true)
    private val onNavigateUp: OnNavigateUp = mockk(relaxed = true)


    private val adViewTestTag = KAdView.TEST_TAG

    @OptIn(ExperimentalCoilApi::class)
    private fun setupEnvironment(uiState: UiArticlesState) {
        fakeImageLoaderEngine = FakeImageLoaderEngine.Builder()
            .default(ColorImage(Color.Blue.toArgb()))
            .build()

        val imageLoader =
            ImageLoader.Builder(applicationContext)
                .components { add(fakeImageLoaderEngine) }
                .build()

        val previewHandler = AsyncImagePreviewHandler {
            ColorImage(Color.Green.toArgb())
        }

        composeTestRule.setContent {
            CompositionLocalProvider(
                LocalAsyncImagePreviewHandler provides previewHandler,
                LocalImageLoader provides imageLoader,
                LocalApp provides PreviewAppManager
            ) {
                KesiTheme {
                    ArticlesScreen(
                        uiState = uiState,
                        adUnitId = "test-ad-unit-id", // Actual ad unit ID for KAdView
                        onArticleClick = onArticleClick::invoke,
                        onBookmarkClick = onBookmarkClick::invoke,
                        onTryAgain = onTryAgain::invoke,
                        onNavigateUp = onNavigateUp::invoke
                    )
                }
            }
        }
    }

    // --- Test Cases ---

    @Test
    fun `when loading, should display loading indicator`() {
        // Arrange
        val loadingState = UiArticlesState(isLoading = true, articles = emptyList(), error = null)
        setupEnvironment(loadingState)

        // Assert
        composeTestRule.onNodeWithTag(":feature:articles:loading").assertIsDisplayed()
        composeTestRule.onNodeWithTag(":feature:articles:articles").assertDoesNotExist()
        val expectedErrorMessage =
            applicationContext.getString(CoreUiSystemR.string.core_uisystem_network_error)
        composeTestRule.onNodeWithText(expectedErrorMessage).assertDoesNotExist()
        // AdView should not be displayed when loading and no articles
        composeTestRule.onNodeWithTag(adViewTestTag).assertDoesNotExist()
    }

    @Test
    fun `when articles loaded, should display articles list and ad view`() {
        // Arrange
        val fakeUiArticles =
            FakeArticles.items.take(2).map { it.asUiArticle().copy(isBookmarked = false) }
        val successState =
            UiArticlesState(isLoading = false, articles = fakeUiArticles, error = null)
        setupEnvironment(successState)

        println(composeTestRule.onRoot().printToString())

        // Assert
        composeTestRule.onNodeWithTag(":feature:articles:articles").assertIsDisplayed()
        fakeUiArticles.forEach { article ->
            composeTestRule.onNodeWithText(article.title).assertIsDisplayed()
            composeTestRule.onNodeWithText(article.description).assertIsDisplayed()
        }
        composeTestRule.onNodeWithTag(":feature:articles:loading").assertDoesNotExist()
        // AdView should be displayed
        composeTestRule.onNodeWithTag(adViewTestTag).assertExists()
    }

    @Test
    fun `when network error, should display error message and not ad view`() {
        // Arrange
        val networkError = ErrorState(ArticlesErrors.NetworkError)
        val errorState =
            UiArticlesState(isLoading = false, articles = emptyList(), error = networkError)
        setupEnvironment(errorState)

        // Assert
        val expectedErrorMessage =
            applicationContext.getString(CoreUiSystemR.string.core_uisystem_network_error)
        val expectedTryAgainText =
            applicationContext.getString(CoreUiSystemR.string.core_uisystem_try_again)

        composeTestRule.onNodeWithText("📡").assertIsDisplayed()
        composeTestRule.onNodeWithText(expectedErrorMessage).assertIsDisplayed()
        composeTestRule.onNodeWithText(expectedTryAgainText, useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag(":feature:articles:loading").assertDoesNotExist()
        composeTestRule.onNodeWithTag(":feature:articles:articles").assertDoesNotExist()
        // AdView should not be displayed on error
        composeTestRule.onNodeWithTag(adViewTestTag).assertDoesNotExist()
    }

    @Test
    fun `when generic error, should display error message and not ad view`() {
        // Arrange
        val genericError = ErrorState(ArticlesErrors.GenericError)
        val errorState =
            UiArticlesState(isLoading = false, articles = emptyList(), error = genericError)
        setupEnvironment(errorState)

        // Assert
        val expectedErrorMessage =
            applicationContext.getString(CoreUiSystemR.string.core_uisystem_network_error)
        val expectedTryAgainText =
            applicationContext.getString(CoreUiSystemR.string.core_uisystem_try_again)

        composeTestRule.onNodeWithText("📡").assertIsDisplayed()
        composeTestRule.onNodeWithText(expectedErrorMessage).assertIsDisplayed()
        composeTestRule.onNodeWithText(expectedTryAgainText, useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag(":feature:articles:loading").assertDoesNotExist()
        composeTestRule.onNodeWithTag(":feature:articles:articles").assertDoesNotExist()
        // AdView should not be displayed on error
        composeTestRule.onNodeWithTag(adViewTestTag).assertDoesNotExist()
    }


    @Test
    fun `clicking an article should invoke onArticleClick callback`() {
        // Arrange
        val articleIdToClick = FakeArticles.items.first().id
        val fakeUiArticles = listOf(
            FakeArticles.items.first().asUiArticle().copy(isBookmarked = false)
        )
        val successState =
            UiArticlesState(isLoading = false, articles = fakeUiArticles, error = null)
        setupEnvironment(successState)

        // Act
        composeTestRule.onNodeWithText(fakeUiArticles.first().title).performClick()

        // Assert
        verify { onArticleClick(articleIdToClick) }
    }

    @Test
    fun `clicking bookmark on an article should invoke onBookmarkClick callback`() {
        // Arrange
        val articleToBookmark = FakeArticles.items.first().asUiArticle().copy(isBookmarked = false)
        val successState =
            UiArticlesState(isLoading = false, articles = listOf(articleToBookmark), error = null)
        setupEnvironment(successState)

        // Act
        composeTestRule.onAllNodesWithTag(
            ":feature:articles:bookmarkButton",
            useUnmergedTree = true
        )[0].performClick()

        // Assert
        verify { onBookmarkClick(Intent.BookmarkClicked(articleToBookmark.articleId)) }
    }


    @Test
    fun `clicking try again on error should invoke onTryAgain callback`() {
        // Arrange
        val error = ErrorState(ArticlesErrors.NetworkError)
        val errorState = UiArticlesState(isLoading = false, articles = emptyList(), error = error)
        setupEnvironment(errorState)

        // Act
        val expectedTryAgainText =
            applicationContext.getString(CoreUiSystemR.string.core_uisystem_try_again)
        composeTestRule.onNodeWithText(expectedTryAgainText, useUnmergedTree = true).performClick()

        // Assert
        verify { onTryAgain(Intent.FetchArticles) }
    }

    @Test
    fun `clicking navigation up should invoke onNavigateUp callback`() {
        // Arrange
        val successState = UiArticlesState(isLoading = false, articles = emptyList(), error = null)
        setupEnvironment(successState)

        val application: Application = ApplicationProvider.getApplicationContext()

        // Act
        composeTestRule.onNodeWithContentDescription(
            application.getString(CoreUiSystemR.string.core_uisystem_navigate_up),
            useUnmergedTree = true
        ).performClick()

        // Assert
        verify { onNavigateUp() }
    }

    @Test
    fun `when articles list is empty and not loading and no error, should display ad view`() {
        // Arrange
        val emptyState = UiArticlesState(isLoading = false, articles = emptyList(), error = null)
        setupEnvironment(emptyState)

        println(composeTestRule.onRoot().printToString())

        // Assert
        composeTestRule.onNodeWithTag(":feature:articles:articles")
            .assertExists()
            .fetchSemanticsNode()
            .children
            .also { assertThat(it).isEmpty() }
        composeTestRule.onNodeWithTag(":feature:articles:loading").assertDoesNotExist()
        val expectedErrorMessage =
            applicationContext.getString(CoreUiSystemR.string.core_uisystem_network_error)
        composeTestRule.onNodeWithText(expectedErrorMessage).assertDoesNotExist()
        // AdView should be displayed even if the list is empty, as per ArticlesScreen logic
        composeTestRule.onNodeWithTag(adViewTestTag).assertExists()
    }
}