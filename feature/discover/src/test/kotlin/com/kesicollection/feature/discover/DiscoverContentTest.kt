package com.kesicollection.feature.discover

import android.os.Build
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import com.kesicollection.core.app.AppManager
import com.kesicollection.core.uisystem.LocalApp
import com.kesicollection.core.uisystem.LocalImageLoader
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.discover.fake.FakePromotedContent
import com.kesicollection.feature.discover.fake.FakeUIContent
import com.kesicollection.feature.discover.utils.OnContentInvoker
import com.kesicollection.feature.discover.utils.OnSeeAllInvoker
import io.mockk.mockk
import io.mockk.verify
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoilApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.M, Build.VERSION_CODES.TIRAMISU]) // API 23 and 33
class DiscoverContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val appManager: AppManager = mockk(relaxed = true)
    private val mockOnSeeAllClick: OnSeeAllInvoker = mockk(relaxed = true)
    private val mockOnContentClick: OnContentInvoker = mockk(relaxed = true)

    private fun setDiscoverContent(uiState: UiState.DiscoverContent) {
        composeTestRule.setContent {
            KesiTheme {
                // Image loader for preview.
                val imageLoader = ImageLoader.Builder(LocalContext.current)
                    .build()

                CompositionLocalProvider(LocalImageLoader provides imageLoader) {
                    CompositionLocalProvider(LocalApp provides appManager) {
                        DiscoverContent(
                            uiState = uiState,
                            onSeeAllClick = mockOnSeeAllClick::invoke,
                            onContentClick = mockOnContentClick::invoke,
                        )
                    }
                }
            }
        }
    }

    @Test
    fun `DiscoverContent displays featured content correctly`() {
        val testUiState = UiState.DiscoverContent(
            featuredContent = FakeUIContent.items,
            promotedContent = FakePromotedContent.items
        )

        setDiscoverContent(testUiState)

        // Assert that featured content items are displayed
        composeTestRule.onNodeWithText(FakeUIContent.items.first().title).assertIsDisplayed()
        composeTestRule.onNodeWithTag(":feature:discover:featuredLazyRow")
            .performScrollToNode(hasText(FakeUIContent.items.last().title))
        composeTestRule.onNodeWithText(FakeUIContent.items.last().title).assertIsDisplayed()
    }

    @Test
    fun `DiscoverContent displays promoted content correctly`() {
        val testUiState = UiState.DiscoverContent(
            featuredContent = FakeUIContent.items,
            promotedContent = FakePromotedContent.items
        )

        setDiscoverContent(testUiState)

        val firstSection = FakePromotedContent.items[FakePromotedContent.items.keys.first()]!!

        // Assert that promoted category and content are displayed
        composeTestRule.onNodeWithText(firstSection.first().title).assertIsDisplayed()
        composeTestRule.onNodeWithTag(":feature:discover:promotedLazyRowModifier")
            .performScrollToNode(hasText(firstSection.last().title))
        composeTestRule.onNodeWithText(firstSection.last().title).assertIsDisplayed()
    }

    @Test
    fun `DiscoverContent calls onContentClick for featured item and logs analytics`() {
        val testUiState = UiState.DiscoverContent(
            featuredContent = FakeUIContent.items,
            promotedContent = FakePromotedContent.items
        )
        setDiscoverContent(testUiState)
        composeTestRule.onNodeWithText(FakeUIContent.items.first().title).performClick()

        // Verify that the onContentClick callback was invoked with the correct item
        verify { mockOnContentClick(FakeUIContent.items.first()) }

        //For now we will validate that "logEvent" is called only but in the integration tests we have to
        //verify actual parameters since they depend in DI
        verify(exactly = 1) { appManager.analytics.logEvent(any(), any()) }
    }

    @Test
    fun `DiscoverContent calls onSeeAllClick for promoted category and logs analytics`() {
        val testUiState = UiState.DiscoverContent(
            featuredContent = FakeUIContent.items,
            promotedContent = FakePromotedContent.items
        )

        setDiscoverContent(testUiState)

        composeTestRule.onAllNodesWithTag(":feature:discover:seeAllButton")[0]
            .performClick()

        verify { mockOnSeeAllClick(FakePromotedContent.items.keys.first()) }

        //For now we will validate that "logEvent" is called only but in the integration tests we have to
        //verify actual parameters since they depend in DI
        verify(exactly = 1) { appManager.analytics.logEvent(any(), any()) }
    }

    @Test
    fun `DiscoverContent displays correctly when featured content is empty`() {
        val testUiState = UiState.DiscoverContent(
            featuredContent = persistentListOf(),
            promotedContent = FakePromotedContent.items
        )

        setDiscoverContent(testUiState)

        composeTestRule.onNodeWithTag(":feature:discover:featuredLazyRow").assertDoesNotExist()
        composeTestRule.onNodeWithText(FakeUIContent.items.first().title).assertDoesNotExist()

        val firstPromotedCategory = FakePromotedContent.items.keys.first()
        val firstPromotedItemInFirstCategory =
            FakePromotedContent.items[firstPromotedCategory]!!.first()

        composeTestRule.onNodeWithText(firstPromotedCategory.name)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(firstPromotedItemInFirstCategory.title)
            .assertIsDisplayed()
    }

    @Test
    fun `DiscoverContent displays correctly when promoted content is empty`() {
        val testUiState = UiState.DiscoverContent(
            featuredContent = FakeUIContent.items,
            promotedContent = persistentMapOf()
        )

        setDiscoverContent(testUiState)

        // Assert featured content is still displayed
        composeTestRule.onNodeWithText(FakeUIContent.items.first().title).assertIsDisplayed()

        // Assert promoted content section is not displayed
        composeTestRule.onNodeWithTag(":feature:discover:promotedLazyRowModifier")
            .assertDoesNotExist()
        // Try to find a promoted category name - should not exist
        val firstPromotedCategory = FakePromotedContent.items.keys.first()
        composeTestRule.onNodeWithText(firstPromotedCategory.name).assertDoesNotExist()
    }
}