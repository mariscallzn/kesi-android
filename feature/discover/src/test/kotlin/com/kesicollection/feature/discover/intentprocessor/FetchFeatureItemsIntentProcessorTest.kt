package com.kesicollection.feature.discover.intentprocessor

import com.google.common.truth.Truth.assertThat
import com.kesicollection.core.app.CrashlyticsWrapper
import com.kesicollection.core.app.Reducer
import com.kesicollection.core.model.Discover
import com.kesicollection.core.model.ErrorState
import com.kesicollection.domain.GetDiscoverContentUseCase
import com.kesicollection.feature.discover.DiscoverErrors
import com.kesicollection.feature.discover.UiState
import com.kesicollection.feature.discover.asDiscoverContent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Tests for [FetchFeatureItemsIntentProcessor].
 *
 * This test suite verifies the behavior of the `FetchFeatureItemsIntentProcessor` under various conditions,
 * ensuring it correctly handles:
 * - **Successful content fetching:**
 *     - Verifies that the UI state is correctly updated with the fetched content.
 *     - Ensures the `GetDiscoverContentUseCase` is called exactly once.
 *     - Confirms Crashlytics is not invoked.
 *     - Tests the case where the use case returns an empty but successful result.
 * - **Error handling (generic exceptions):**
 *     - Verifies that the UI state is updated to reflect a generic error.
 *     - Ensures the error message from the exception is propagated to the UI state.
 *     - Confirms the `GetDiscoverContentUseCase` is called exactly once.
 *     - Verifies that Crashlytics is called with the correct exception and parameters.
 *     - Tests the scenario where the exception message is null.
 * - **Error handling (CancellationException):**
 *     - Verifies that the reducer is not called when a `CancellationException` occurs.
 *     - Ensures the `GetDiscoverContentUseCase` is called exactly once.
 *     - Confirms Crashlytics is not invoked for `CancellationException`.
 *     - Tests coroutine cancellation during the execution of the `getDiscoverContentUseCase`.
 * - **Reducer invocation:**
 *     - Ensures the reducer is invoked exactly once upon successful content fetching.
 *     - Ensures the reducer is invoked exactly once when a generic error occurs.
 */
@ExperimentalCoroutinesApi
class FetchFeatureItemsIntentProcessorTest {

    private lateinit var getDiscoverContentUseCase: GetDiscoverContentUseCase
    private lateinit var crashlyticsWrapper: CrashlyticsWrapper
    private lateinit var crashlyticsParams: CrashlyticsWrapper.Params
    private lateinit var processor: FetchFeatureItemsIntentProcessor

    @Before
    fun setUp() {
        getDiscoverContentUseCase = mockk()
        crashlyticsWrapper = mockk(relaxed = true) // relaxed = true to avoid mocking every call
        crashlyticsParams = mockk()
        every { crashlyticsWrapper.params } returns crashlyticsParams
        every { crashlyticsParams.screenName } returns "DiscoverScreen"
        every { crashlyticsParams.className } returns "FetchFeatureItemsIntentProcessor"
        every { crashlyticsParams.action } returns "fetch"

        processor = FetchFeatureItemsIntentProcessor(getDiscoverContentUseCase, crashlyticsWrapper)
    }

    @Test
    fun `Successful content fetch and UI update`() = runTest {
        val mockDiscover = Discover(emptyList(), emptyList()) // Assuming Discover has a constructor
        val expectedUiState = mockDiscover.asDiscoverContent()
        coEvery { getDiscoverContentUseCase() } returns Result.success(mockDiscover)

        val reducerSlot = slot<Reducer<UiState>>()
        processor.processIntent { reducer -> reducerSlot.captured = reducer }

        val actualUiState =
            reducerSlot.captured.invoke(mockk(relaxed = true)) // pass a dummy state if needed

        assertThat(actualUiState).isEqualTo(expectedUiState)
        coVerify(exactly = 1) { getDiscoverContentUseCase() }
        verify(exactly = 0) { crashlyticsWrapper.recordException(any(), any()) }
    }

    @Test
    fun `Use case throws generic exception`() = runTest {
        val exceptionMessage = "Network error"
        val exception = Exception(exceptionMessage)
        coEvery { getDiscoverContentUseCase() } returns Result.failure(exception)

        val reducerSlot = slot<Reducer<UiState>>()
        processor.processIntent { reducer -> reducerSlot.captured = reducer }

        val actualUiState = reducerSlot.captured.invoke(mockk(relaxed = true))

        assertThat(actualUiState).isInstanceOf(UiState.Error::class.java)
        val errorState = (actualUiState as UiState.Error).error
        assertThat(errorState).isEqualTo(
            ErrorState(
                type = DiscoverErrors.GenericError,
                message = exceptionMessage
            )
        )
        assertThat(errorState.message).isEqualTo(exceptionMessage)
        coVerify(exactly = 1) { getDiscoverContentUseCase() }
    }

    @Test
    fun `Use case throws CancellationException`() = runTest {
        val cancellationException = CancellationException("Job cancelled")
        coEvery { getDiscoverContentUseCase() } throws cancellationException

        var reducerCalled = false
        processor.processIntent { reducerCalled = true }

        assertThat(reducerCalled).isFalse()
        coVerify(exactly = 1) { getDiscoverContentUseCase() }
        verify(exactly = 0) { crashlyticsWrapper.recordException(any(), any()) }
    }

    @Test
    fun `Crashlytics called on generic exception`() = runTest {
        val exceptionMessage = "Database error"
        val exception = Exception(exceptionMessage)
        coEvery { getDiscoverContentUseCase() } returns Result.failure(exception)

        val expectedParams = mapOf(
            crashlyticsParams.screenName to "DiscoverScreen",
            crashlyticsParams.className to "FetchFeatureItemsIntentProcessor",
            crashlyticsParams.action to "fetch"
        )

        processor.processIntent { /* no-op for this test */ }

        coVerify(exactly = 1) { getDiscoverContentUseCase() }
        verify(exactly = 1) { crashlyticsWrapper.recordException(exception, expectedParams) }
    }

    @Test
    fun `Crashlytics NOT called on CancellationException`() = runTest {
        val cancellationException = CancellationException("Cancelled")
        coEvery { getDiscoverContentUseCase() } throws cancellationException

        processor.processIntent { /* no-op */ }

        coVerify(exactly = 1) { getDiscoverContentUseCase() }
        verify(exactly = 0) { crashlyticsWrapper.recordException(any(), any()) }
    }

    @Test
    fun `Reducer is invoked exactly once on success`() = runTest {
        val mockDiscover = Discover(emptyList(), emptyList())
        coEvery { getDiscoverContentUseCase() } returns Result.success(mockDiscover)

        var reducerCallCount = 0
        processor.processIntent { reducerCallCount++ }

        assertThat(reducerCallCount).isEqualTo(1)
    }

    @Test
    fun `Reducer is invoked exactly once on generic error`() = runTest {
        val exception = Exception("Some error")
        coEvery { getDiscoverContentUseCase() } returns Result.failure(exception)

        var reducerCallCount = 0
        processor.processIntent { reducerCallCount++ }

        assertThat(reducerCallCount).isEqualTo(1)
    }

    @Test
    fun `Use case returns empty success result`() = runTest {
        val emptyDiscover = Discover(emptyList(), emptyList())
        val expectedUiState =
            emptyDiscover.asDiscoverContent() // Should be UiState.DiscoverContent with empty lists
        coEvery { getDiscoverContentUseCase() } returns Result.success(emptyDiscover)

        val reducerSlot = slot<Reducer<UiState>>()
        processor.processIntent { reducer -> reducerSlot.captured = reducer }

        val actualUiState = reducerSlot.captured.invoke(mockk(relaxed = true))

        assertThat(actualUiState).isEqualTo(expectedUiState)
        assertThat((actualUiState as UiState.DiscoverContent).featuredContent).isEmpty()
        assertThat(actualUiState.promotedContent).isEmpty()
        coVerify(exactly = 1) { getDiscoverContentUseCase() }
        verify(exactly = 0) { crashlyticsWrapper.recordException(any(), any()) }
    }

    @Test
    fun `Exception message is null for generic error`() = runTest {
        val exceptionWithNullMessage = Exception(null as String?)
        coEvery { getDiscoverContentUseCase() } returns Result.failure(exceptionWithNullMessage)

        val reducerSlot = slot<Reducer<UiState>>()
        processor.processIntent { reducer -> reducerSlot.captured = reducer }

        val actualUiState = reducerSlot.captured.invoke(mockk(relaxed = true))

        assertThat(actualUiState).isInstanceOf(UiState.Error::class.java)
        val errorState = (actualUiState as UiState.Error).error
        assertThat(errorState).isEqualTo(
            ErrorState(
                type = DiscoverErrors.GenericError,
                message = null
            )
        )
        assertThat(errorState.message).isNull() // ErrorState should handle null message gracefully

        verify(exactly = 1) { crashlyticsWrapper.recordException(exceptionWithNullMessage, any()) }
    }

    @Test
    fun `Coroutine cancellation during getDiscoverContentUseCase execution`() = runTest {
        val cancellationException = CancellationException("Cancelled during use case")
        coEvery { getDiscoverContentUseCase() } coAnswers {
            throw cancellationException
        }

        var reducerCalled = false
        val job = launch { // Launch in a separate job to allow cancellation propagation
            processor.processIntent { reducerCalled = true }
        }

        job.join() // Wait for the launched coroutine to complete or be cancelled

        assertThat(reducerCalled).isFalse()
        coVerify(exactly = 1) { getDiscoverContentUseCase() }
        verify(exactly = 0) { crashlyticsWrapper.recordException(any(), any()) }
    }
}