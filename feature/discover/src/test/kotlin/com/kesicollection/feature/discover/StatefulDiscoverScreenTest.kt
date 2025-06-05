package com.kesicollection.feature.discover

import android.os.Build
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import com.kesicollection.core.app.AppManager
import com.kesicollection.core.model.ErrorState
import com.kesicollection.core.uisystem.LocalApp
import com.kesicollection.core.uisystem.LocalImageLoader
import com.kesicollection.feature.discover.utils.OnContentInvoker
import com.kesicollection.feature.discover.utils.OnRetryInvoker
import com.kesicollection.feature.discover.utils.OnSeeAllInvoker
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
/**
 * Test suite for [DiscoverScreen] which is a stateful composable.
 * This class focuses on testing the UI behavior based on different [UiState] values
 * and interactions with the screen.
 *
 * Tests cover:
 * - Display of Loading state.
 * - Display of Content state.
 * - Display of Error state.
 * - Invocation of `onContentClick` callback when a content item is clicked.
 * - Invocation of `onSeeAllClick` callback when a "See All" button is clicked.
 * - Invocation of ViewModel's `sendIntent` when "Try Again" is clicked in Error state.
 * - Logging of analytics screen view event on composition.
 */

@OptIn(ExperimentalCoilApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU]) // Using a single SDK for simplicity here
class StatefulDiscoverScreenTest {
    /**
     * Rule for managing and interacting with Compose UI components in tests.
     */
    @get:Rule
    val composeTestRule = createComposeRule()

    // Mocks for dependencies of DiscoverScreen
    /**
     * Mocked [DiscoverViewModel] to control its state and verify interactions.
     */
    private lateinit var mockViewModel: DiscoverViewModel
    /**
     * Mocked [AppManager] to verify analytics events.
     */
    private lateinit var appManager: AppManager
    /**
     * Mocked callback for "See All" clicks.
     */
    private lateinit var mockOnSeeAllClick: OnSeeAllInvoker
    private lateinit var mockOnContentClick: OnContentInvoker
    private lateinit var mockOnTryAgain: OnRetryInvoker

    // StateFlow to control the ViewModel's state in tests
    private lateinit var uiStateFlow: MutableStateFlow<UiState>

    /**
     * Sets up the test environment before each test case.
     */
    @Before
    fun setUp() {
        uiStateFlow = MutableStateFlow(UiState.Loading) // Initial state for tests
        appManager = mockk(relaxed = true)
        mockOnSeeAllClick = mockk(relaxed = true)
        mockOnContentClick = mockk(relaxed = true)
        mockOnTryAgain = mockk(relaxed = true)
        mockViewModel = mockk {
            every { uiState } returns uiStateFlow
            every { sendIntent(any()) } just Runs // Mock sendIntent if it's called
        }
    }

    /**
     * Helper function to set the content of the test rule with [DiscoverScreen]
     * and necessary CompositionLocalProviders.
     */
    private fun setStatefulDiscoverScreen() {
        composeTestRule.setContent {
            // Image loader for preview.
            val imageLoader = ImageLoader.Builder(LocalContext.current)
                .build()

            CompositionLocalProvider(LocalImageLoader provides imageLoader) {
                CompositionLocalProvider(LocalApp provides appManager) {
                    DiscoverScreen(
                        viewModel = mockViewModel,
                        onSeeAllClick = mockOnSeeAllClick::invoke,
                        onContentClick = mockOnContentClick::invoke
                    )
                }
            }
        }
    }

    /**
     * Verifies that the loading indicator is displayed when the [UiState] is `Loading`.
     * It also ensures that content and error views are not present.
     */
    @Test
    fun `when state is Loading, LoadingDiscover is displayed`() {
        uiStateFlow.value = UiState.Loading
        setStatefulDiscoverScreen()

        composeTestRule.onNodeWithTag(":feature:discover:LoadingDiscover").assertIsDisplayed()
        composeTestRule.onNodeWithTag(":feature:discover:DiscoverContent", useUnmergedTree = true)
            .assertDoesNotExist()
        composeTestRule.onNodeWithTag(":feature:discover:ShowError", useUnmergedTree = true)
            .assertDoesNotExist()
    }

    /**
     * Verifies that the discover content is displayed when the [UiState] is `DiscoverContent`.
     * It also checks for the visibility of specific content items and ensures that loading
     * and error views are not present.
     */
    @Test
    fun `when state is DiscoverContent, DiscoverContent is displayed`() {
        val testContent = contentSample // From DiscoverScreen.kt previews
        uiStateFlow.value = testContent
        setStatefulDiscoverScreen()

        composeTestRule.onNodeWithTag(":feature:discover:DiscoverContent").assertIsDisplayed()
        // Optionally, assert specific content items are visible
        composeTestRule.onNodeWithText(testContent.featuredContent.first().title)
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag(":feature:discover:LoadingDiscover", useUnmergedTree = true)
            .assertDoesNotExist()
        composeTestRule.onNodeWithTag(":feature:discover:ShowError", useUnmergedTree = true)
            .assertDoesNotExist()
    }

    /**
     * Verifies that the error view is displayed when the [UiState] is `Error`.
     * It also ensures that loading and content views are not present.
     */
    @Test
    fun `when state is Error, ShowError is displayed`() {
        uiStateFlow.value = UiState.Error(ErrorState(DiscoverErrors.NetworkError))
        setStatefulDiscoverScreen()

        composeTestRule.onNodeWithTag(":feature:discover:ShowError").assertIsDisplayed()
        composeTestRule.onNodeWithTag(":feature:discover:LoadingDiscover", useUnmergedTree = true)
            .assertDoesNotExist()
        composeTestRule.onNodeWithTag(":feature:discover:DiscoverContent", useUnmergedTree = true)
            .assertDoesNotExist()
    }

    /**
     * Verifies that the `onContentClick` callback is invoked with the correct data
     * when a content item is clicked. It also checks if an analytics event is logged.
     */
    @Test
    fun `when content item is clicked, onContentClick is called with correct data`() {
        val testContentState = contentSample
        val firstFeaturedItem = testContentState.featuredContent.first()
        uiStateFlow.value = testContentState
        setStatefulDiscoverScreen()

        // Find the specific content item and click it.
        // You might need more specific finders if titles are not unique.
        // Using a testTag on individual items in your actual Composable is best.
        composeTestRule.onNodeWithText(firstFeaturedItem.title, useUnmergedTree = true)
            .performClick()

        verify { mockOnContentClick.invoke(firstFeaturedItem) }
        // Verify analytics event (if directly logged by the clickable modifier,
        // or ensure the remembered lambda in DiscoverContent is correctly structured)
        verify {
            appManager.analytics.logEvent(any(), any())
        }
    }

    /**
     * Verifies that the `onSeeAllClick` callback is invoked with the correct category
     * when a "See All" button is clicked. It also checks if an analytics event is logged.
     */
    @Test
    fun `when See All is clicked, onSeeAllClick is called with correct category`() {
        val testContentState = contentSample
        val firstCategory = testContentState.promotedContent.keys.first()
        uiStateFlow.value = testContentState
        setStatefulDiscoverScreen()

        composeTestRule.onAllNodesWithTag(":feature:discover:seeAllButton")[0]
            .performClick()

        verify { mockOnSeeAllClick(firstCategory) }
        verify { appManager.analytics.logEvent(any(), any()) }
    }

    /**
     * Verifies that the ViewModel's `sendIntent` method is called with `Intent.FetchFeatureItems`
     * when the "Try Again" button is clicked in the error state. It also checks if an analytics
     * event is logged.
     */
    @Test
    fun `when state is Error and Try Again is clicked, ViewModel's sendIntent is called`() {
        uiStateFlow.value = UiState.Error(ErrorState(DiscoverErrors.GenericError))
        setStatefulDiscoverScreen()

        // Find the "Try Again" button (assuming it has this text or a specific testTag)
        composeTestRule.onNodeWithText("Try Again", ignoreCase = true).performClick()

        // Verify that the ViewModel's intent to refetch is sent
        verify { mockViewModel.sendIntent(Intent.FetchFeatureItems) }
        verify {
            appManager.analytics.logEvent(any(), any())
        }
    }

    /**
     * Verifies that an analytics screen view event is logged when the [DiscoverScreen]
     * is composed.
     */
    @Test
    fun `analytics screenView is logged on composition`() {
        uiStateFlow.value = UiState.Loading // Any initial state will do
        setStatefulDiscoverScreen() // Composition happens here

        // SideEffect runs after successful composition
        composeTestRule.waitForIdle() // Ensure SideEffect has a chance to run

        verify {
            appManager.analytics.logEvent(any(), any())
        }
    }
}