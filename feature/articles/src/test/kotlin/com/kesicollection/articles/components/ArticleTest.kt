package com.kesicollection.articles.components

import android.app.Application
import android.os.Build
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToString
import androidx.test.core.app.ApplicationProvider
import coil3.ColorImage
import com.kesicollection.feature.articles.R
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.test.FakeImageLoaderEngine
import com.kesicollection.articles.model.UiArticle
import com.kesicollection.articles.model.asUiArticle
import com.kesicollection.articles.utils.OnArticleClick
import com.kesicollection.articles.utils.OnBookmarkClick
import com.kesicollection.core.uisystem.LocalImageLoader
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.test.core.fake.FakeArticles
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.intArrayOf

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.M, Build.VERSION_CODES.TIRAMISU]) // API 23 and 33
/**
 * Test suite for the [Article] composable.
 * Test cases:
 * - `article information is displayed`: Verifies that the article title, description, and image are displayed.
 * - `when Article is Clicked then onArticleClick Is Called`: Verifies that the `onArticleClick` callback is invoked when the article is clicked.
 * - `when Bookmark is Clicked then onBookmarkClick Is Called`: Verifies that the `onBookmarkClick` callback is invoked when the bookmark button is clicked.
 * - `bookmarked content description when isBookmarked is false`: Verifies the content description of the bookmark button when the article is not bookmarked.
 * - `unbookmarked content description when isBookmarked is true`: Verifies the content description of the bookmark button when the article is bookmarked.
 */
class ArticleTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeImageLoaderEngine: FakeImageLoaderEngine

    private val sampleArticle = FakeArticles.items.first().asUiArticle()

    @OptIn(ExperimentalCoilApi::class)
    private fun setupComposable(
        article: UiArticle = sampleArticle, // The UI article to display. Defaults to `sampleArticle`.
        onArticleClick: (UiArticle) -> Unit = {}, // Callback invoked when the article is clicked. Defaults to an empty lambda.
        onBookmarkClick: (String) -> Unit = {} // Callback invoked when the bookmark button is clicked. Defaults to an empty lambda.
    ) {
        // Initialize a FakeImageLoaderEngine for Coil.
        // This allows us to control image loading behavior in tests
        // without making actual network requests.
        fakeImageLoaderEngine = FakeImageLoaderEngine.Builder()
            // Sets a default image to be returned for any image request.
            // In this case, it's a solid blue color.
            // This is useful to ensure that the image loading mechanism is working
            // and to provide a consistent visual for tests if specific images aren't needed.
            // Color.Blue.toArgb() converts the Jetpack Compose Color to an Android ARGB integer.
            .default(ColorImage(Color.Blue.toArgb())) // Default image for any request
            .build()

        composeTestRule.setContent {
            KesiTheme {
                CompositionLocalProvider(
                    LocalImageLoader provides ImageLoader.Builder(LocalContext.current)
                        // Add the fake image loader engine to the Coil components.
                        // This ensures that our fake engine is used for image loading
                        // within the scope of this test.
                        .components { add(fakeImageLoaderEngine) }
                        .build()
                ) {
                    // Render the Article composable with the provided or default parameters.
                    Article(
                        article = article,
                        onArticleClick = onArticleClick,
                        // Passes the onBookmarkClick lambda to the Article composable.
                        // This allows testing the interaction with the bookmark button.
                        onBookmarkClick = onBookmarkClick
                    )
                }
            }
        }
    }

    @Test
    fun `article information is displayed`() {
        setupComposable()

        composeTestRule.onNodeWithText(sampleArticle.title).assertIsDisplayed()
        composeTestRule.onNodeWithText(sampleArticle.description)
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag(":feature:articles:imageArticle", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun `when Article is Clicked then onArticleClick Is Called`() {
        val mockOnArticleClick = mockk<OnArticleClick>(relaxed = true)
        setupComposable(onArticleClick = mockOnArticleClick::invoke)

        composeTestRule.onNodeWithText(sampleArticle.title).performClick()

        verify(exactly = 1) { mockOnArticleClick(sampleArticle) }
    }

    @Test
    fun `when Bookmark is Clicked then onBookmarkClick Is Called`() {
        val mockOnBookmarkClick: OnBookmarkClick = mockk(relaxed = true)
        setupComposable(onBookmarkClick = mockOnBookmarkClick::invoke)

        composeTestRule.onNodeWithTag(":feature:articles:bookmarkButton")
            .assertIsDisplayed()
            .performClick()
        verify(exactly = 1) { mockOnBookmarkClick(sampleArticle.articleId) }
    }

    @Test
    fun `bookmarked content description when isBookmarked is false`() {
        // Get application context to resolve string resources
        val applicationContext: Application = ApplicationProvider.getApplicationContext()
        val bookmarkDescription = applicationContext
            .getString(R.string.feature_articles_bookmark)

        setupComposable(article = sampleArticle.copy(isBookmarked = false))

        println(composeTestRule.onRoot().printToString())

        composeTestRule.onNodeWithTag(":feature:articles:bookmarkButton")
            .assertContentDescriptionEquals(bookmarkDescription)
    }

    @Test
    fun `unbookmarked content description when isBookmarked is true`() {
        // Get application context to resolve string resources
        val applicationContext: Application = ApplicationProvider.getApplicationContext()
        val unbookmarkDescription = applicationContext
            .getString(R.string.feature_articles_unbookmark)

        setupComposable(article = sampleArticle.copy(isBookmarked = true))

        println(composeTestRule.onRoot().printToString())

        composeTestRule.onNodeWithTag(":feature:articles:bookmarkButton")
            .assertContentDescriptionEquals(unbookmarkDescription)
    }
}