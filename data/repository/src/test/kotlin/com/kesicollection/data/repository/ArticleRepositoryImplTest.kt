package com.kesicollection.data.repository

import com.google.common.truth.Truth.assertThat
import com.kesicollection.core.model.Article
import com.kesicollection.data.api.ArticleApi
import com.kesicollection.test.core.fake.FakeArticles
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ArticleRepositoryImplTest {
    // Mock the ArticleApi dependency
    private lateinit var mockArticleApi: ArticleApi

    // The class under test
    private lateinit var articleRepository: ArticleRepositoryImpl

    @Before
    fun setUp() {
        // Initialize the mock before each test
        mockArticleApi = mock()
        // Create an instance of the repository with the mocked API
        articleRepository = ArticleRepositoryImpl(remoteArticleApi = mockArticleApi)
    }

    @Test
    fun `getArticles success should return list of articles from api`() = runTest {
        // 1. Arrange
        val successResponse: Result<List<Article>> = Result.success(FakeArticles.items)

        // Stub the API call to return the success result
        whenever(mockArticleApi.getAll()).thenReturn(successResponse)

        // 2. Act
        val actualResult = articleRepository.getArticles()

        // 3. Assert
        // Verify that the API's getAll method was called exactly once
        verify(mockArticleApi).getAll()
        assertThat(actualResult).isEqualTo(successResponse)
        assertThat(actualResult.isSuccess).isTrue()
        assertThat(actualResult.getOrNull()).isEqualTo(FakeArticles.items)
    }

    @Test
    fun `getArticles failure should return error result from api`() = runTest {
        // 1. Arrange
        val expectedException = RuntimeException("Network Error")
        val failureResult: Result<List<Article>> = Result.failure(expectedException)

        whenever(mockArticleApi.getAll()).thenReturn(failureResult)

        // 2. Act
        val actualResult = articleRepository.getArticles()

        // 3. Assert
        verify(mockArticleApi).getAll()
        assertThat(actualResult).isEqualTo(failureResult)
        assertThat(actualResult.isFailure).isTrue()
        assertThat(actualResult.exceptionOrNull()).isEqualTo(expectedException)
    }

    @Test
    fun `getArticleById success should return article from api`() = runTest {
        // 1. Arrange
        val articleId = FakeArticles.items.first().id
        val expectedArticle = FakeArticles.items.first()
        val successResult: Result<Article> = Result.success(expectedArticle)

        whenever(mockArticleApi.getContentById(articleId)).thenReturn(successResult)

        // 2. Act
        val actualResult = articleRepository.getArticleById(articleId)

        // 3. Assert
        verify(mockArticleApi).getContentById(articleId)
        assertThat(actualResult).isEqualTo(successResult)
        assertThat(actualResult.isSuccess).isTrue()
        assertThat(actualResult.getOrNull()).isEqualTo(expectedArticle)
    }

    @Test
    fun `getArticleById failure should return error result from api`() = runTest {
        // 1. Arrange
        val articleId = FakeArticles.items.first().id
        val expectedException = RuntimeException("Article not found")
        val failureResult: Result<Article> = Result.failure(expectedException)

        whenever(mockArticleApi.getContentById(articleId)).thenReturn(failureResult)

        // 2. Act
        val actualResult = articleRepository.getArticleById(articleId)

        // 3. Assert
        verify(mockArticleApi).getContentById(articleId)
        assertThat(actualResult).isEqualTo(failureResult)
        assertThat(actualResult.isFailure).isTrue()
        assertThat(actualResult.exceptionOrNull()).isEqualTo(expectedException)
    }

    @Test
    fun `getArticleById with different id should call api with correct id`() = runTest {
        // 1. Arrange
        val articleId1 = FakeArticles.items.first().id
        val articleId2 = FakeArticles.items.last().id
        val expectedArticle1 = FakeArticles.items.first()
        val result1: Result<Article> = Result.success(expectedArticle1)
        val expectedArticle2 = FakeArticles.items.last()
        val result2: Result<Article> = Result.success(expectedArticle2)

        whenever(mockArticleApi.getContentById(articleId1)).thenReturn(result1)
        whenever(mockArticleApi.getContentById(articleId2)).thenReturn(result2)

        // 2. Act
        val actualResult1 = articleRepository.getArticleById(articleId1)
        val actualResult2 = articleRepository.getArticleById(articleId2)

        // 3. Assert
        verify(mockArticleApi).getContentById(articleId1)
        verify(mockArticleApi).getContentById(articleId2)
        assertThat(actualResult1).isEqualTo(result1)
        assertThat(actualResult2).isEqualTo(result2)
    }
}