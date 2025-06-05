package com.kesicollection.feature.discover

import android.os.Build
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import com.kesicollection.core.app.AppManager
import com.kesicollection.core.model.ErrorState
import com.kesicollection.core.uisystem.LocalApp
import com.kesicollection.core.uisystem.LocalImageLoader
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.discover.fake.FakePromotedContent
import com.kesicollection.feature.discover.fake.FakeUIContent
import com.kesicollection.feature.discover.utils.OnContentInvoker
import com.kesicollection.feature.discover.utils.OnRetryInvoker
import com.kesicollection.feature.discover.utils.OnSeeAllInvoker
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoilApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.M, Build.VERSION_CODES.TIRAMISU]) // API 23 and 33
class StatelessDiscoverScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val appManager: AppManager = mockk(relaxed = true)
    private val mockOnSeeAllClick: OnSeeAllInvoker = mockk(relaxed = true)
    private val mockOnContentClick: OnContentInvoker = mockk(relaxed = true)
    private val mockOnTryAgain: OnRetryInvoker = mockk(relaxed = true)

    private fun setStatelessDiscoverScreen(uiState: UiState) {
        composeTestRule.setContent {
            KesiTheme { // Apply your theme
                // Image loader for preview.
                val imageLoader = ImageLoader.Builder(LocalContext.current)
                    .build()

                CompositionLocalProvider(LocalImageLoader provides imageLoader) {
                    CompositionLocalProvider(LocalApp provides appManager) {
                        DiscoverScreen(
                            uiState = uiState,
                            onSeeAllClick = mockOnSeeAllClick::invoke,
                            onContentClick = mockOnContentClick::invoke,
                            onTryAgain = mockOnTryAgain::invoke
                        )
                    }
                }
            }
        }
    }

    @Test
    fun `Display loading component when UiState is Loading`() {
        val uiState = UiState.Loading
        setStatelessDiscoverScreen(uiState)
        composeTestRule.onNodeWithTag(":feature:discover:LoadingDiscover").assertExists()
        composeTestRule.onNodeWithTag(":feature:discover:DiscoverContent").assertDoesNotExist()
        composeTestRule.onNodeWithTag(":feature:discover:ShowError").assertDoesNotExist()
    }

    @Test
    fun `Display DiscoverContent when UiState is DiscoverContent`() {
        val uiState = UiState.DiscoverContent(
            featuredContent = FakeUIContent.items,
            promotedContent = FakePromotedContent.items
        )
        setStatelessDiscoverScreen(uiState)
        composeTestRule.onNodeWithTag(":feature:discover:LoadingDiscover").assertDoesNotExist()
        composeTestRule.onNodeWithTag(":feature:discover:DiscoverContent").assertExists()
        composeTestRule.onNodeWithTag(":feature:discover:ShowError").assertDoesNotExist()

        // Verify category names are displayed
        composeTestRule.onNodeWithText(FakeUIContent.items.first().title).assertIsDisplayed()
        val firstSection = FakePromotedContent.items[FakePromotedContent.items.keys.first()]!!

        // Assert that promoted category and content are displayed
        composeTestRule.onNodeWithText(FakePromotedContent.items.keys.first().name).assertIsDisplayed()
    }

    @Test
    fun `Display ShowError when UiState is Error`() {
        val uiState = UiState.Error(error = ErrorState(DiscoverErrors.GenericError))
        setStatelessDiscoverScreen(uiState)
        composeTestRule.onNodeWithTag(":feature:discover:LoadingDiscover").assertDoesNotExist()
        composeTestRule.onNodeWithTag(":feature:discover:DiscoverContent").assertDoesNotExist()
        composeTestRule.onNodeWithTag(":feature:discover:ShowError").assertExists()
    }
}