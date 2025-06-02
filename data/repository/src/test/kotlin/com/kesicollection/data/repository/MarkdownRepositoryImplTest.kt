package com.kesicollection.data.repository

import com.google.common.truth.Truth.assertThat
import com.kesicollection.data.api.MarkdownApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException
/**
 * Unit tests for [MarkdownRepositoryImpl].
 *
 * This class tests the functionality of [MarkdownRepositoryImpl], ensuring that it correctly
 * interacts with the [MarkdownApi] to download markdown content as a string.
 */
class MarkdownRepositoryImplTest {

    // Mock instance of MarkdownApi to simulate network responses.
    private val markdownApi: MarkdownApi = mockk()
    // Instance of MarkdownRepositoryImpl being tested.
    private lateinit var markdownRepository: MarkdownRepositoryImpl

    /**
     * Sets up the test environment before each test case.
     * Initializes [markdownRepository] with the mocked [markdownApi].
     */
    @Before
    fun setUp() {
        // Initialize MarkdownRepositoryImpl with the mocked remote API.
        markdownRepository = MarkdownRepositoryImpl(
            remoteMarkdownApi = markdownApi
        )
    }

    /**
     * Tests the `downloadAsString` method for a successful API call.
     *
     * Verifies that:
     * - The method returns a [Result.success] when the API call is successful.
     * - The API's `downloadAsString` method is called exactly once.
     * - The returned success result contains the expected markdown content.
     */
    @Test
    fun `downloadAsString returns success when remote api call is successful`() = runTest {
        // Given: A test file name and expected markdown content.
        val testFileName = "my-document.md"
        val expectedMarkdownContent = "# Test Title\n\nThis is test markdown content."
        coEvery { markdownApi.downloadAsString(testFileName) } returns Result.success(
            expectedMarkdownContent
        )

        // When: The downloadAsString method is called.
        val result = markdownRepository.downloadAsString(testFileName)

        // Then: Verify the API call and the successful result.
        coVerify(exactly = 1) { markdownApi.downloadAsString(testFileName) }
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(expectedMarkdownContent)
    }

    /**
     * Tests the `downloadAsString` method for a failed API call.
     *
     * Verifies that:
     * - The method returns a [Result.failure] when the API call fails.
     * - The API's `downloadAsString` method is called exactly once.
     * - The returned failure result contains the expected exception.
     * - The exception is an instance of [IOException].
     * - The exception message matches the expected error message.
     */
    @Test
    fun `downloadAsString returns failure when remote api call fails`() = runTest {
        // Given: A non-existent file name and an expected IOException.
        val testFileName = "non-existent-document.md"
        val expectedException = IOException("Failed to fetch markdown from API")
        // Mock the API call to return a failure result.
        coEvery { markdownApi.downloadAsString(testFileName) } returns Result.failure(
            expectedException
        )

        // When
        val result = markdownRepository.downloadAsString(testFileName)

        // Then: Verify the API call and the failure result.
        coVerify(exactly = 1) { markdownApi.downloadAsString(testFileName) }
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(expectedException)
        assertThat(result.exceptionOrNull()).isInstanceOf(IOException::class.java)
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Failed to fetch markdown from API")
    }
}