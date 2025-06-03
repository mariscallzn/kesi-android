package com.kesicollection.domain

import com.google.common.truth.Truth.assertThat
import com.kesicollection.data.repository.BookmarkRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

/**
 * Test class for [BookmarkArticleByIdUseCase].
 * This class contains unit tests to verify the behavior of the `BookmarkArticleByIdUseCase` class,
 * ensuring it correctly interacts with the [BookmarkRepository] and handles various scenarios.
 */
class BookmarkArticleByIdUseCaseTest {

    private lateinit var mockBookmarkRepository: BookmarkRepository
    private lateinit var useCase: BookmarkArticleByIdUseCase // This is the class under test

    /**
     * Sets up the test environment before each test.
     * Initializes the mock [BookmarkRepository] and the [BookmarkArticleByIdUseCase] instance.
     */
    @Before
    fun setUp() {
        mockBookmarkRepository = mockk<BookmarkRepository>()
        // Instantiating the actual UseCase with the mock repository
        useCase = BookmarkArticleByIdUseCase(mockBookmarkRepository)
    }

    /**
     * Tests bookmarking an article with a valid ID.
     * Verifies that the `bookmarkArticleById` method of the repository is called exactly once
     * with the correct ID.
     */
    @Test
    fun `Test bookmarking with a valid ID`() = runTest {
        val testId = "validId123"
        // Stub the repository method to do nothing when called with testId
        coEvery { mockBookmarkRepository.bookmarkArticleById(testId) } just runs

        // Invoke the use case with the test ID
        // This internally calls the suspend operator fun invoke(id: String) of the use case.
        useCase.invoke(testId) // Calling the suspend operator fun invoke(id: String)

        // Verifying the interaction with the repository, as per the UseCase's implementation
        coVerify(exactly = 1) { mockBookmarkRepository.bookmarkArticleById(testId) }
    }

    /**
     * Tests bookmarking an article with an empty ID.
     * Verifies that the repository's `bookmarkArticleById` method is called with the empty ID.
     * This tests how the use case handles potentially invalid but technically acceptable input.
     */
    @Test
    fun `Test bookmarking with an empty ID`() = runTest {
        val testId = ""
        coEvery { mockBookmarkRepository.bookmarkArticleById(testId) } just runs

        useCase.invoke(testId)

        coVerify(exactly = 1) { mockBookmarkRepository.bookmarkArticleById(testId) }
    }

    /**
     * Tests bookmarking an article with an ID containing special characters.
     * Verifies that the repository's `bookmarkArticleById` method is called with the ID
     * containing special characters, ensuring these are handled correctly.
     */
    @Test
    fun `Test bookmarking with an ID containing special characters`() = runTest {
        val testId = "id-with-@#\$%-special*chars"
        coEvery { mockBookmarkRepository.bookmarkArticleById(testId) } just runs

        useCase.invoke(testId)

        coVerify(exactly = 1) { mockBookmarkRepository.bookmarkArticleById(testId) }
    }

    /**
     * Tests bookmarking an article with an extremely long ID.
     * Verifies that the repository's `bookmarkArticleById` method is called with the long ID,
     * testing the system's ability to handle large inputs.
     */
    @Test
    fun `Test bookmarking with an extremely long ID`() = runTest {
        val testId = "a".repeat(10000)
        coEvery { mockBookmarkRepository.bookmarkArticleById(testId) } just runs

        useCase.invoke(testId)

        coVerify(exactly = 1) { mockBookmarkRepository.bookmarkArticleById(testId) }
    }

    /**
     * Tests the scenario of a successful bookmarking operation.
     * Ensures that the use case completes without throwing an exception and that the repository
     * method is called as expected.
     */
    @Test
    fun `Test successful bookmarking operation`() = runTest {
        val testId = "successfulId"
        coEvery { mockBookmarkRepository.bookmarkArticleById(testId) } just runs

        useCase.invoke(testId) // Should complete without throwing

        coVerify(exactly = 1) { mockBookmarkRepository.bookmarkArticleById(testId) }
    }

    /**
     * Tests the use case's behavior when the `BookmarkRepository` throws an exception.
     * Verifies that the exception is propagated correctly by the use case and that the
     * exception details (type and message) are as expected.
     */
    @Test
    fun `Test when bookmarkRepository throws an exception`() = runTest {
        val testId = "exceptionId"
        val expectedException = IOException("Database error")
        // Simulate the repository method (which the use case calls) throwing an exception
        coEvery { mockBookmarkRepository.bookmarkArticleById(testId) } throws expectedException

        var caughtException: Throwable? = null
        try {
            // Invoke the use case, which should trigger the mocked exception from the repository
            useCase.invoke(testId) // This will internally call the throwing mock
        } catch (e: Throwable) {
            caughtException = e
        }

        assertThat(caughtException).isNotNull()
        assertThat(caughtException).isInstanceOf(IOException::class.java)
        assertThat(caughtException).hasMessageThat().isEqualTo("Database error")
        coVerify(exactly = 1) { mockBookmarkRepository.bookmarkArticleById(testId) }
    }

    /**
     * Tests multiple concurrent calls to the use case with the same ID.
     * Verifies that the repository's `bookmarkArticleById` method is called for each concurrent
     * invocation, ensuring that concurrent requests are handled independently.
     * This is important for understanding potential race conditions or resource contention.
     */
    @Test
    fun `Test multiple concurrent calls with the same ID`() = runTest {
        val testId = "concurrentSameId"
        coEvery { mockBookmarkRepository.bookmarkArticleById(testId) } just runs

        val job1 = launch { useCase.invoke(testId) }
        val job2 = launch { useCase.invoke(testId) }

        job1.join()
        job2.join()

        // Since the use case simply passes the call through, the repository method
        // will be called for each invocation.
        coVerify(exactly = 2) { mockBookmarkRepository.bookmarkArticleById(testId) }
    }

    /**
     * Tests multiple concurrent calls to the use case with different IDs.
     * Verifies that the repository's `bookmarkArticleById` method is called once for each
     * unique ID, ensuring correct handling of concurrent requests with different parameters.
     */
    @Test
    fun `Test multiple concurrent calls with different IDs`() = runTest {
        val testId1 = "concurrentId1"
        val testId2 = "concurrentId2"
        coEvery { mockBookmarkRepository.bookmarkArticleById(testId1) } just runs
        coEvery { mockBookmarkRepository.bookmarkArticleById(testId2) } just runs

        val job1 = launch { useCase.invoke(testId1) }
        val job2 = launch { useCase.invoke(testId2) }

        job1.join()
        job2.join()

        coVerify(exactly = 1) { mockBookmarkRepository.bookmarkArticleById(testId1) }
        coVerify(exactly = 1) { mockBookmarkRepository.bookmarkArticleById(testId2) }
    }

    /**
     * Tests the direct interaction with the mocked `BookmarkRepository`.
     * This is a basic test to ensure that the use case correctly delegates the call to
     * the repository's `bookmarkArticleById` method.
     */
    @Test
    fun `Test interaction with mocked BookmarkRepository`() = runTest {
        val testId = "interactionTestId"
        coEvery { mockBookmarkRepository.bookmarkArticleById(testId) } just runs

        useCase.invoke(testId)

        coVerify(exactly = 1) { mockBookmarkRepository.bookmarkArticleById(testId) }
    }

}