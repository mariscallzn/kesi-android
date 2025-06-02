package com.kesicollection.data.retrofit

import com.kesicollection.data.retrofit.fake.FakeNetworkArticle
import com.kesicollection.data.retrofit.fake.FakeNetworkIndexArticle
import com.kesicollection.data.retrofit.model.kesiandroid.asArticle
import com.kesicollection.data.retrofit.service.KesiAndroidService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest // or runBlockingTest for older versions
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class RetrofitArticleApiTest {

    private lateinit var mockKesiAndroidService: KesiAndroidService
    private lateinit var retrofitArticleApi: RetrofitArticleApi

    @Before
    fun setUp() {
        mockKesiAndroidService = mockk()
        retrofitArticleApi = RetrofitArticleApi(mockKesiAndroidService)
    }

    // --- Tests for getAll() ---

    @Test
    fun `getAll returns success with mapped articles when service succeeds`() = runTest {
        coEvery { mockKesiAndroidService.fetchAllArticles() } returns FakeNetworkIndexArticle.items

        // When
        val result = retrofitArticleApi.getAll()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(FakeNetworkIndexArticle.items.map { it.asArticle() }, result.getOrNull())
    }

    @Test
    fun `getAll returns failure when service throws IOException`() = runTest {
        // Given
        val expectedException = IOException("Network error")
        coEvery { mockKesiAndroidService.fetchAllArticles() } throws expectedException

        // When
        val result = retrofitArticleApi.getAll()

        // Then
        assertTrue(result.isFailure)
        assertEquals(expectedException, result.exceptionOrNull())
    }

    @Test
    fun `getAll returns success with empty list when service returns empty list`() = runTest {
        // Given
        coEvery { mockKesiAndroidService.fetchAllArticles() } returns emptyList()

        // When
        val result = retrofitArticleApi.getAll()

        // Then
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    // --- Tests for getContentById() ---

    @Test
    fun `getContentById returns success with mapped article when service succeeds`() = runTest {
        // Given
        val articleId = FakeNetworkArticle.items.first().id
        val networkArticle = FakeNetworkArticle.items.first()

        val expectedArticle = FakeNetworkArticle.items.first().asArticle()

        coEvery { mockKesiAndroidService.getArticleById(articleId) } returns networkArticle

        // When
        val result = retrofitArticleApi.getContentById(articleId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedArticle, result.getOrNull())
    }

    @Test
    fun `getContentById returns failure when service throws IOException`() = runTest {
        // Given
        val articleId = "article123"
        val expectedException = IOException("Network error for specific ID")
        coEvery { mockKesiAndroidService.getArticleById(articleId) } throws expectedException

        // When
        val result = retrofitArticleApi.getContentById(articleId)

        // Then
        assertTrue(result.isFailure)
        assertEquals(expectedException, result.exceptionOrNull())
    }
}