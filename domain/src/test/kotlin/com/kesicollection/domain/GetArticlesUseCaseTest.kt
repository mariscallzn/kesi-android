/**
 * Unit tests for the [GetArticlesUseCase].
 */
package com.kesicollection.domain

import com.google.common.truth.Truth.assertThat
import com.kesicollection.data.repository.ArticleRepository
import com.kesicollection.test.core.fake.FakeArticles // Import your fake articles
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import java.io.IOException
import kotlin.test.Test

/**
 * Tests for the [GetArticlesUseCase] class.
 * This class verifies the behavior of the use case under different scenarios,
 * such as successful data retrieval, empty data, and error conditions.
 */
class GetArticlesUseCaseTest {

    /**
     * A mock instance of [ArticleRepository] used to simulate repository interactions.
     */
    private lateinit var mockArticleRepository: ArticleRepository
    /**
     * The instance of [GetArticlesUseCase] being tested.
     */
    private lateinit var getArticlesUseCase: GetArticlesUseCase

    /**
     * Sets up the test environment before each test.
     * Initializes the [mockArticleRepository] and [getArticlesUseCase].
     */
    @Before
    fun setUp() {
        mockArticleRepository = mockk() // Create a mock for ArticleRepository
        getArticlesUseCase = GetArticlesUseCase(mockArticleRepository)
    }

    /**
     * Tests that [GetArticlesUseCase.invoke] returns a [Result.success] with a list of articles
     * when the repository successfully retrieves articles.
     */
    @Test
    fun `invoke when repository returns articles successfully returns success with article list`() = runTest { // ktlint-disable annotation
        // Given
        val fakeArticleList = FakeArticles.items // Use your fake data
        coEvery { mockArticleRepository.getArticles() } returns Result.success(fakeArticleList)

        // When
        val result = getArticlesUseCase()

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(fakeArticleList)
        assertThat(result.getOrNull()).hasSize(FakeArticles.items.size)
    }

    /**
     * Tests that [GetArticlesUseCase.invoke] returns a [Result.success] with an empty list
     * when the repository successfully retrieves an empty list of articles.
     */
    @Test
    fun `invoke when repository returns empty list successfully returns success with empty list`() = runTest { // ktlint-disable annotation
        // Given
        val emptyArticleList = emptyList<com.kesicollection.core.model.Article>()
        coEvery { mockArticleRepository.getArticles() } returns Result.success(emptyArticleList)

        // When
        val result = getArticlesUseCase()

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(emptyArticleList)
        assertThat(result.getOrNull()).isEmpty()
    }

    /**
     * Tests that [GetArticlesUseCase.invoke] returns a [Result.failure] with the same exception
     * when the repository returns a failure.
     */
    @Test
    fun `invoke when repository returns failure returns failure with the same exception`() = runTest { // ktlint-disable annotation
        // Given
        val expectedException = IOException("Network error occurred")
        coEvery { mockArticleRepository.getArticles() } returns Result.failure(expectedException)

        // When
        val result = getArticlesUseCase()

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(expectedException)
        assertThat(result.exceptionOrNull()).isInstanceOf(IOException::class.java)
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Network error occurred")
    }
}