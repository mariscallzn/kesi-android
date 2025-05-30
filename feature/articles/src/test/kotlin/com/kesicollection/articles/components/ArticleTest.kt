package com.kesicollection.articles.components

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import coil3.ColorImage
import coil3.ImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.annotation.ExperimentalCoilApi
import coil3.test.FakeImageLoaderEngine
import com.kesicollection.articles.model.asUiArticle
import com.kesicollection.core.uisystem.LocalImageLoader
import com.kesicollection.testing.testdata.ArticlesTestData
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test

/**
 * Test class for the [Article] composable.
 *
 * This class tests the rendering of the [Article] composable,
 * ensuring that all its main components (title, description, and image) are correctly displayed.
 *
 * It uses Robolectric for running tests in a simulated Android environment, Compose testing library to interact with the UI and FakeImageLoader to simulate loading of images.
 */
@RunWith(RobolectricTestRunner::class)
class ArticleTest {
    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var fakeImageLoaderEngine: FakeImageLoaderEngine

    /**
     * Sets up the test environment before each test.
     *
     * This function initializes a [FakeImageLoaderEngine] to simulate image loading during tests.
     * It configures the engine to return a default blue color image for any image request.
     * This allows testing the UI's response to image loading without actually loading images from a network or file.
     */
    @OptIn(DelicateCoilApi::class, ExperimentalCoilApi::class)
    @Before
    fun before() {
        fakeImageLoaderEngine = FakeImageLoaderEngine.Builder()
            .default(ColorImage(Color.Blue.toArgb()))
            .build()
    }

    /**
     * This test verifies that all the expected UI components are present within the [Article] composable.
     * It checks for the presence of:
     * - The article's title.
     * - The article's description.
     * - The article's image.
     *
     * It uses a [FakeImageLoaderEngine] to simulate image loading during the test and
     * avoids making actual network requests.
     * It set the image to blue for testing purposes.
     */
    @OptIn(ExperimentalCoilApi::class, DelicateCoilApi::class)
    @Test
    fun `validate that all components are present`() {
        val article = ArticlesTestData.items.first().asUiArticle()
        with(composeRule) {
            setContent {
                val imageLoader = ImageLoader.Builder(LocalContext.current)
                    .components { add(fakeImageLoaderEngine) }
                    .build()

                CompositionLocalProvider(
                    LocalImageLoader provides imageLoader
                ) {
                    Article(
                        article = article
                    )
                }
            }

            onNode(hasText(article.title))
                .assertExists()
            onNode(hasText(article.description))
                .assertExists()
            onNode(hasTestTag(":feature:articles:imageArticle"), true)
                .assertExists()
        }
    }
}