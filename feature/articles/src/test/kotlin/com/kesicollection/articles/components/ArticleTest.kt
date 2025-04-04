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
import com.kesicollection.articles.LocalImageLoader
import com.kesicollection.articles.model.asUiArticle
import com.kesicollection.testing.ArticlesTestData
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
class ArticleTest {
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