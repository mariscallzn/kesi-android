/**
 * This file contains unit tests for the GetDiscoverContentUseCase.
 */
package com.kesicollection.domain
// Necessary imports for testing, including Truth for assertions, model classes, repository, fake data, and MockK for mocking.
import com.google.common.truth.Truth.assertThat
import com.kesicollection.core.model.Discover
import com.kesicollection.data.repository.DiscoverRepository
import com.kesicollection.test.core.fake.FakeDiscover
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

/**
 * Test suite for [GetDiscoverContentUseCase].
 * This class tests the behavior of the use case under various conditions, such as successful data retrieval,
 * handling of failures (like network errors), and scenarios with empty data.
 */
class GetDiscoverContentUseCaseTest {

    // Mocked DiscoverRepository to simulate data layer interactions.
    private lateinit var mockDiscoverRepository: DiscoverRepository
    // Instance of the use case being tested.
    private lateinit var getDiscoverContentUseCase: GetDiscoverContentUseCase

    /**
     * Sets up the test environment before each test.
     * Initializes the mock repository and the use case with the mock repository.
     */
    @Before
    fun setUp() {
        mockDiscoverRepository = mockk()
        getDiscoverContentUseCase = GetDiscoverContentUseCase(mockDiscoverRepository)
    }

    /**
     * Tests that the use case returns a success result with discover data
     * when the repository successfully fetches the discover content.
     */
    @Test
    fun `invoke when repository returns discover content successfully returns success with discover data`() = runTest {
        val fakeDiscoverData = FakeDiscover.items.firstOrNull() // Arrange: Prepare fake data.
        assertThat(fakeDiscoverData).isNotNull() // Assert that fake data is not null.

        coEvery { mockDiscoverRepository.getDiscoverContent() } returns Result.success(fakeDiscoverData!!)

        // When
        val result = getDiscoverContentUseCase()

        // Then
        assertThat(result.isSuccess).isTrue() // Assert: Verify the operation was successful.
        assertThat(result.getOrNull()).isEqualTo(fakeDiscoverData) // Assert: Verify the returned data matches the expected data.
        assertThat(result.getOrNull()).isNotNull() // Assert: Ensure data is not null.
    }

    /**
     * Tests that the use case returns a failure result with the same exception
     * when the repository fails to fetch the discover content.
     */
    @Test
    fun `invoke when repository returns failure returns failure with the same exception`() = runTest {
        // Arrange: Simulate a network error.
        val expectedException = IOException("Network error fetching discover content")
        coEvery { mockDiscoverRepository.getDiscoverContent() } returns Result.failure(expectedException)

        // When
        val result = getDiscoverContentUseCase()

        // Then
        assertThat(result.isFailure).isTrue() // Assert: Verify the operation failed.
        val actualException = result.exceptionOrNull() // Assert: Retrieve the exception.
        assertThat(actualException).isEqualTo(expectedException) // Assert: Verify the exception is as expected.
        assertThat(actualException).isInstanceOf(IOException::class.java) // Assert: Verify the type of exception.
        assertThat(actualException?.message).isEqualTo("Network error fetching discover content") // Assert: Verify the exception message.
    }

    /**
     * Tests that the use case handles gracefully and returns a success result with empty discover data
     * when the repository successfully fetches 'empty' discover content.
     */
    @Test
    fun `invoke when repository returns success with 'empty' discover data handles it gracefully`() = runTest {
        // Arrange: Prepare empty discover data.
        val emptyDiscoverData = Discover(featured = emptyList(), promotedContent = emptyList())
        coEvery { mockDiscoverRepository.getDiscoverContent() } returns Result.success(emptyDiscoverData)

        // Act: Execute the use case.
        val result = getDiscoverContentUseCase()

        // Then
        assertThat(result.isSuccess).isTrue() // Assert: Verify the operation was successful.
        val discoverContent = result.getOrNull() // Assert: Retrieve the content.
        assertThat(discoverContent).isNotNull() // Assert: Ensure content is not null.
        assertThat(discoverContent).isEqualTo(emptyDiscoverData) // Assert: Verify the content matches the empty data.
        assertThat(discoverContent?.featured).isEmpty() // Assert: Verify 'featured' list is empty.
        assertThat(discoverContent?.promotedContent).isEmpty() // Assert: Verify 'promotedContent' list is empty.
    }
}