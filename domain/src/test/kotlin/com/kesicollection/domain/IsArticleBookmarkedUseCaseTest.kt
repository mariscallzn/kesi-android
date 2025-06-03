package com.kesicollection.domain

import com.google.common.truth.Truth.assertThat
import com.kesicollection.data.repository.BookmarkRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for the [IsArticleBookmarkedUseCase].
 * This class tests the functionality of checking if an article is bookmarked.
 */
class IsArticleBookmarkedUseCaseTest {

    /**
     * A mock instance of [BookmarkRepository] used for testing.
     */
    private lateinit var mockBookmarkRepository: BookmarkRepository
    /**
     * The instance of [IsArticleBookmarkedUseCase] under test.
     */
    private lateinit var isArticleBookmarkedUseCase: IsArticleBookmarkedUseCase

    /**
     * Sets up the test environment before each test case.
     * Initializes the mock repository and the use case.
     */
    @Before
    fun setUp() {
        mockBookmarkRepository = mockk()
        isArticleBookmarkedUseCase = IsArticleBookmarkedUseCase(mockBookmarkRepository)
    }

    /**
     * Tests that [IsArticleBookmarkedUseCase.invoke] returns `true` when the article is bookmarked.
     */
    @Test
    fun `invoke when article is bookmarked returns true`() = runTest {
        // Given
        // An article ID that is expected to be bookmarked.
        val articleId = "article123"
        coEvery { mockBookmarkRepository.isBookmarked(articleId) } returns true

        // When
        val result = isArticleBookmarkedUseCase(articleId)

        // Then
        // Assert that the result is true.
        assertThat(result).isTrue()
    }

    /**
     * Tests that [IsArticleBookmarkedUseCase.invoke] returns `false` when the article is not bookmarked.
     */
    @Test
    fun `invoke when article is not bookmarked returns false`() = runTest {
        // Given an article ID that is expected to not be bookmarked.
        val articleId = "article456"
        coEvery { mockBookmarkRepository.isBookmarked(articleId) } returns false

        // When
        val result = isArticleBookmarkedUseCase(articleId)

        // Then
        // Assert that the result is false.
        assertThat(result).isFalse()
    }

    /**
     * Tests that [IsArticleBookmarkedUseCase.invoke] correctly identifies a bookmarked article even when other articles are not.
     */
    @Test
    fun `invoke with different article ID when bookmarked returns true`() = runTest {
        // Given two article IDs, one bookmarked and one not.
        val articleId1 = "article_abc"
        val articleId2 = "article_xyz"
        coEvery { mockBookmarkRepository.isBookmarked(articleId1) } returns true
        coEvery { mockBookmarkRepository.isBookmarked(articleId2) } returns false // To ensure specificity

        // When
        val result = isArticleBookmarkedUseCase(articleId1)

        // Then
        // Assert that the result for the bookmarked article is true.
        assertThat(result).isTrue()
    }

    /**
     * Tests that [IsArticleBookmarkedUseCase.invoke] correctly identifies a non-bookmarked article even when other articles are.
     */
    @Test
    fun `invoke with different article ID when not bookmarked returns false`() = runTest {
        // Given two article IDs, one not bookmarked and one bookmarked.
        val articleId1 = "article_def"
        val articleId2 = "article_ghi"
        coEvery { mockBookmarkRepository.isBookmarked(articleId1) } returns false
        coEvery { mockBookmarkRepository.isBookmarked(articleId2) } returns true // To ensure specificity

        // When
        val result = isArticleBookmarkedUseCase(articleId1)

        // Then
        // Assert that the result for the non-bookmarked article is false.
        assertThat(result).isFalse()
    }

    /**
     * Tests that [IsArticleBookmarkedUseCase.invoke] returns `false` for an empty article ID when it's not bookmarked (edge case).
     */
    @Test
    fun `invoke with empty article ID when not bookmarked returns false`() = runTest {
        // Given an empty article ID that is expected to not be bookmarked.
        val articleId = "" // Testing edge case
        coEvery { mockBookmarkRepository.isBookmarked(articleId) } returns false

        // When
        // Invoke the use case with the empty article ID.
        val result = isArticleBookmarkedUseCase(articleId)

        // Then
        // Assert that the result is false.
        assertThat(result).isFalse()
    }

    /**
     * Tests that [IsArticleBookmarkedUseCase.invoke] returns `true` for an empty article ID when it is bookmarked (edge case).
     */
    @Test
    fun `invoke with empty article ID when bookmarked returns true`() = runTest {
        // Given an empty article ID that is expected to be bookmarked.
        val articleId = "" // Testing edge case
        coEvery { mockBookmarkRepository.isBookmarked(articleId) } returns true

        // When
        // Invoke the use case with the empty article ID.
        val result = isArticleBookmarkedUseCase(articleId)

        // Then
        // Assert that the result is true.
        assertThat(result).isTrue()
    }
}