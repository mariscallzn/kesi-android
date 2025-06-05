package com.kesicollection.feature.discover

import com.google.common.truth.Truth.assertThat
import com.kesicollection.core.app.IntentProcessor
import com.kesicollection.core.app.IntentProcessorFactory
import com.kesicollection.core.app.Reducer
import com.kesicollection.feature.discover.fake.FakePromotedContent
import com.kesicollection.feature.discover.fake.FakeUIContent
import io.mockk.every
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Tests for [DiscoverViewModel].
 *
 * This test suite verifies the behavior of the [DiscoverViewModel] under various conditions.
 * It focuses on:
 *
 * 1.  **Initial UI State**: Ensures that the ViewModel initializes with the correct loading state.
 * 2.  **Intent Processing and State Updates**:
 *     *   Verifies that when an intent (e.g., `FetchFeatureItems`) is sent to the ViewModel,
 *         the `IntentProcessor` is correctly invoked.
 *     *   Ensures that the `IntentProcessor`'s resulting state update (simulated via a callback
 *         and a reducer function) is correctly reflected in the ViewModel's `uiState` Flow.
 */
class DiscoverViewModelTest {

    // Test dispatcher for coroutines
    private val testDispatcher = StandardTestDispatcher()

    // Mocks
    private lateinit var intentProcessorFactory: IntentProcessorFactory<UiState, Intent>
    private lateinit var intentProcessor: IntentProcessor<UiState>

    // Mock UiState flow for the intent processor
    private lateinit var mockUiStateFlow: MutableStateFlow<UiState>

    // ViewModel instance
    private lateinit var viewModel: DiscoverViewModel

    @Before
    fun setUp() {

        // Initialize mocks
        intentProcessorFactory = mockk()
        intentProcessor = mockk(relaxed = true) // relaxed = true to ignore void functions

        // Initialize mockUiStateFlow with the initial state
        mockUiStateFlow = MutableStateFlow(UiState.Loading)

        // Stub the create method of the factory to return our mock processor
        every { intentProcessorFactory.create(any()) } returns intentProcessor
        // Stub the processIntent method of the processor to update the mockUiStateFlow
        coEvery { intentProcessor.processIntent(any()) } coAnswers {
            mockUiStateFlow.update { arg(1) }
        }

        // Initialize ViewModel
        viewModel = DiscoverViewModel(testDispatcher, intentProcessorFactory)
    }

    @Test
    fun `Initial UI state verification`() = runTest {
        assertThat(viewModel.uiState.first()).isEqualTo(UiState.Loading)
    }

    @Test
    fun `Send single intent and verify UI state update`() = runTest {
        val testIntent = Intent.FetchFeatureItems
        val expectedState = UiState.DiscoverContent(
            featuredContent = FakeUIContent.items,
            promotedContent = FakePromotedContent.items
        )

        // This will hold the (Reducer<UiState>) -> Unit function that the ViewModel passes
        // to processor.processIntent()
        val reducerCallbackSlot = slot<(Reducer<UiState>) -> Unit>()

        // 1. Configure the mockProcessor:
        //    When mockProcessor.processIntent(...) is called by the ViewModel,
        //    we capture the callback.
        //    Then, we simulate the processor doing its work and invoking that callback
        //    with a function that produces the `expectedState`.
        coEvery { intentProcessor.processIntent(capture(reducerCallbackSlot)) } coAnswers {
            // Simulate the processor having done its work and now providing the
            // state transformation function (Reducer<UiState>) to the callback.
            // This Reducer<UiState> is: ` { expectedState } `
            // which means "current state becomes expectedState".
            val stateTransform: Reducer<UiState> = { expectedState }
            reducerCallbackSlot.captured.invoke(stateTransform)
        }

        // Act: Send the intent.
        viewModel.sendIntent(testIntent)

        // Assert: Advance dispatcher and check state.
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value).isEqualTo(expectedState)
    }
}