package com.kesicollection.domain

import com.google.common.truth.Truth.assertThat
import com.kesicollection.data.repository.MarkdownRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

/**
 * Unit tests for the [GetMarkdownAsString] use case.
 *
 * This class tests the behavior of [GetMarkdownAsString] when interacting with a mocked [MarkdownRepository].
 * It covers scenarios like successful markdown retrieval, empty markdown content, and repository failures.
 */
class GetMarkdownAsStringTest {

    /**
     * A mock instance of [MarkdownRepository] used to simulate repository behavior.
     */
    private lateinit var mockMarkdownRepository: MarkdownRepository

    /**
     * The instance of [GetMarkdownAsString] being tested.
     */
    private lateinit var getMarkdownAsString: GetMarkdownAsString

    /**
     * Sets up the test environment before each test case.
     * This method initializes the [mockMarkdownRepository] and [getMarkdownAsString] instances.
     */
    @Before
    fun setUp() {
        mockMarkdownRepository = mockk()
        getMarkdownAsString = GetMarkdownAsString(mockMarkdownRepository)
    }

    /**
     * Tests the `invoke` method of [GetMarkdownAsString] when a valid file name is provided
     * and the repository successfully returns the markdown content as a string.
     *
     * It verifies that the result is a [Result.success] and contains the expected markdown string.
     */
    @Test
    fun `invoke with valid fileName when repository returns markdown successfully returns success with markdown string`() = runTest {
        // Given
        // A valid file name for the markdown file.
        val fileName = "test.md"
        val fakeMarkdownContent = "# Hello Markdown"
        coEvery { mockMarkdownRepository.downloadAsString(fileName) } returns Result.success(fakeMarkdownContent)

        // When
        val result = getMarkdownAsString(fileName)

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(fakeMarkdownContent)
    }

    /**
     * Tests the `invoke` method of [GetMarkdownAsString] when a valid file name is provided
     * and the repository successfully returns an empty string (representing empty markdown content).
     *
     * It verifies that the result is a [Result.success] and contains an empty string.
     */
    @Test
    fun `invoke with valid fileName when repository returns empty string successfully returns success with empty string`() = runTest {
        // Given
        // A file name for an empty markdown file.
        val fileName = "empty.md"
        val emptyMarkdownContent = ""
        coEvery { mockMarkdownRepository.downloadAsString(fileName) } returns Result.success(emptyMarkdownContent)

        // When
        val result = getMarkdownAsString(fileName)

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(emptyMarkdownContent)
    }

    /**
     * Tests the `invoke` method of [GetMarkdownAsString] when the repository returns a failure
     * (e.g., due to an [IOException] during download).
     *
     * It verifies that the result is a [Result.failure] and contains the same exception that the repository returned.
     */
    @Test
    fun `invoke when repository returns failure returns failure with the same exception`() = runTest {
        // Given
        // A file name that will cause a repository error.
        val fileName = "error.md"
        val expectedException = IOException("Failed to download markdown file")
        coEvery { mockMarkdownRepository.downloadAsString(fileName) } returns Result.failure(expectedException)

        // When
        val result = getMarkdownAsString(fileName)

        // Then
        assertThat(result.isFailure).isTrue()
        val actualException = result.exceptionOrNull()
        assertThat(actualException).isEqualTo(expectedException)
        assertThat(actualException).isInstanceOf(IOException::class.java)
        assertThat(actualException?.message).isEqualTo("Failed to download markdown file")
    }

    /**
     * Tests the `invoke` method of [GetMarkdownAsString] when an empty file name is provided
     * and the repository is expected to handle this by returning a failure (e.g., with an [IllegalArgumentException]).
     *
     * This test assumes the repository itself performs validation for empty file names.
     * It verifies that the result is a [Result.failure] and contains the expected exception.
     */
    @Test
    fun `invoke with empty fileName when repository handles it (e_g_ returns failure)`() = runTest {
        // Given
        // An empty file name.
        val fileName = ""
        // How your repository is expected to behave with an empty fileName
        // Option 1: Repository itself validates and returns a failure
        val expectedException = IllegalArgumentException("File name cannot be empty")
        coEvery { mockMarkdownRepository.downloadAsString(fileName) } returns Result.failure(expectedException)
        // Option 2: Repository throws (less common for simple passthrough use cases like this)
        // coEvery { mockMarkdownRepository.downloadAsString(fileName) } throws IllegalArgumentException("File name cannot be empty")


        // When
        val result = getMarkdownAsString(fileName)

        // Then
        assertThat(result.isFailure).isTrue()
        val actualException = result.exceptionOrNull()
        assertThat(actualException).isInstanceOf(IllegalArgumentException::class.java)
        assertThat(actualException?.message).isEqualTo("File name cannot be empty")
    }
}