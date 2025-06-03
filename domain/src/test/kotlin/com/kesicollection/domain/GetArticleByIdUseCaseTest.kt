package com.kesicollection.domain

import com.google.common.truth.Truth.assertThat
import com.kesicollection.data.repository.ArticleRepository
import com.kesicollection.test.core.fake.FakeArticles
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

/**
 * Unit tests for the [GetArticleByIdUseCase].
 * This class tests the behavior of the use case when retrieving an article by its ID.
 */
class GetArticleByIdUseCaseTest {

    private lateinit var mockArticleRepository: ArticleRepository
    private lateinit var getArticleByIdUseCase: GetArticleByIdUseCase

    /**
     * Sets up the test environment before each test.
     * Initializes the mock [ArticleRepository] and the [GetArticleByIdUseCase] with the mock repository.
     */
    @Before
    fun setUp() {
        mockArticleRepository = mockk()
        getArticleByIdUseCase = GetArticleByIdUseCase(mockArticleRepository)
    }

    /**
     * Tests that invoking the use case with an existing article ID returns a successful result
     * containing the expected article.
     *
     * Steps:
     * 1. **Given**: An existing article ID and the corresponding expected article.
     *    The mock repository is configured to return a successful result with the article when
     *    `getArticleById` is called with this ID.
     * 2. **When**: The [GetArticleByIdUseCase] is invoked with the article ID.
     * 3. **Then**: The result should be successful, and the article within the result should
     *    match the expected article.
     */
    @Test
    fun `invoke with existing article ID returns success with article`() = runTest {
        // Given
        val articleId = FakeArticles.items.first().id
        val expectedArticle = FakeArticles.items.first()
        // Mock the repository to return the expected article for the given ID
        coEvery { mockArticleRepository.getArticleById(articleId) } returns Result.success(expectedArticle)

        // When
        val result = getArticleByIdUseCase(articleId)

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(expectedArticle)
    }

    /**
     * Tests that invoking the use case with a non-existent article ID returns a failure result.
     *
     * Steps:
     * 1. **Given**: A non-existent article ID and an expected exception (e.g., [NoSuchElementException]).
     *    The mock repository is configured to return a failure result with this exception when
     *    `getArticleById` is called with this ID.
     * 2. **When**: The [GetArticleByIdUseCase] is invoked with the non-existent article ID.
     * 3. **Then**: The result should be a failure.
     *    The exception within the result should match the expected exception, be an instance of
     *    [NoSuchElementException], and have the expected message.
     */
    @Test
    fun `invoke with non-existent article ID returns failure`() = runTest {
        // Given
        val articleId = "unknown"
        val expectedException = NoSuchElementException("Article not found")
        // Mock the repository to return a failure for the non-existent ID
        coEvery { mockArticleRepository.getArticleById(articleId) } returns Result.failure(expectedException)


        // When
        val result = getArticleByIdUseCase(articleId)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(expectedException)
        assertThat(result.exceptionOrNull()).isInstanceOf(NoSuchElementException::class.java)
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Article not found")
    }

    /**
     * Tests that invoking the use case with an empty article ID returns a failure result
     * if the underlying repository is configured to handle it that way.
     *
     * Steps:
     * 1. **Given**: An empty article ID and an expected exception (e.g., [IllegalArgumentException]).
     *    The mock repository is configured to return a failure result with this exception when
     *    `getArticleById` is called with an empty ID.
     * 2. **When**: The [GetArticleByIdUseCase] is invoked with the empty article ID.
     * 3. **Then**: The result should be a failure.
     *    The exception within the result should match the expected exception and be an instance of
     *    [IllegalArgumentException].
     */
    @Test
    fun `invoke with empty article ID returns failure if repository dictates`() = runTest {
        // Given
        val articleId = ""
        val expectedException = IllegalArgumentException("Article ID cannot be empty")
        // Assuming your repository would return this for an empty ID
        // Mock the repository to return a failure for an empty ID
        coEvery { mockArticleRepository.getArticleById(articleId) } returns Result.failure(expectedException)

        // When
        val result = getArticleByIdUseCase(articleId)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(expectedException)
        assertThat(result.exceptionOrNull()).isInstanceOf(IllegalArgumentException::class.java)
    }
}