package com.kesicollection.articles

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import coil3.ColorImage
import coil3.ImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.annotation.ExperimentalCoilApi
import coil3.test.FakeImageLoaderEngine
import com.kesicollection.articles.model.toUiModel
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.testing.ArticlesTestData
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ArticlesScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var fakeImageLoaderEngine: FakeImageLoaderEngine

    @OptIn(DelicateCoilApi::class, ExperimentalCoilApi::class)
    @Before
    fun before() {
        fakeImageLoaderEngine = FakeImageLoaderEngine.Builder()
            .default(ColorImage(Color.Blue.toArgb()))
            .build()
    }

    @Test
    fun `validate loading is showing when isLoading is true`() {
        with(composeRule) {
            setContent {
                KesiTheme {
                    ArticlesScreen(
                        uiState = UiArticlesState(isLoading = true),
                        onArticleClick = { })
                }
            }
            onNode(hasTestTag(":feature:articles:articles"))
                .assertDoesNotExist()
            onNode(hasTestTag(":feature:articles:loading"))
                .assertExists()
        }
    }

    @Test
    fun `validate items are displayed`() {
        with(composeRule) {
            setContent {
                val imageLoader = ImageLoader.Builder(LocalContext.current)
                    .components { add(fakeImageLoaderEngine) }
                    .build()

                CompositionLocalProvider(
                    LocalImageLoader provides imageLoader
                ) {
                    KesiTheme {
                        ArticlesScreen(
                            uiState = UiArticlesState(
                                isLoading = false,
                                articles = ArticlesTestData.items.map { it.toUiModel() }),
                            onArticleClick = { })
                    }
                }
            }
            onNode(hasTestTag(":feature:articles:articles"))
                .assertExists()
            onNode(hasTestTag(":feature:articles:loading"))
                .assertDoesNotExist()
        }
    }
}