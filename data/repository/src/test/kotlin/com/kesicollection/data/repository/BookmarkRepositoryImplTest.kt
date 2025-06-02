package com.kesicollection.data.repository

import com.kesicollection.data.api.BookmarkApi
import com.kesicollection.test.core.fake.FakeArticles
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class BookmarkRepositoryImplTest {

    private val bookmarkApi: BookmarkApi = mockk()
    private lateinit var bookmarkRepository: BookmarkRepositoryImpl

    @Before
    fun setup() {
        bookmarkRepository = BookmarkRepositoryImpl(
            bookmarkApi = bookmarkApi
        )
    }

    @Test
    fun `bookmarkArticleById with valid ID`() = runTest {
        // Verify that bookmarkApi.bookmarkArticleById is called with the correct ID when a valid ID is provided.
        val articleId = FakeArticles.items.first().id
        coEvery { bookmarkApi.bookmarkArticleById(articleId) } returns Unit

        bookmarkRepository.bookmarkArticleById(articleId)

        coVerify { bookmarkApi.bookmarkArticleById(articleId) }
    }


    @Test
    fun `bookmarkArticleById concurrent calls`() = runTest {
        // Test behavior when bookmarkArticleById is called multiple times concurrently with the same or different IDs to check for race conditions or unexpected behavior.
        val articleId1 = FakeArticles.items[0].id
        val articleId2 = FakeArticles.items[1].id

        coEvery { bookmarkApi.bookmarkArticleById(articleId1) } returns Unit
        coEvery { bookmarkApi.bookmarkArticleById(articleId2) } returns Unit

        // Launch multiple coroutines to call bookmarkArticleById concurrently
        val deferred1 = async(Dispatchers.IO) { bookmarkRepository.bookmarkArticleById(articleId1) }
        val deferred2 = async(Dispatchers.IO) { bookmarkRepository.bookmarkArticleById(articleId2) }
        val deferred3 = async(Dispatchers.IO) { bookmarkRepository.bookmarkArticleById(articleId1) } // Same ID as first

        // Await all calls to complete
        awaitAll(deferred1, deferred2, deferred3)

        // Verify that the API was called the correct number of times for each ID
        // If the API should only be called once per unique ID, adjust verification accordingly.
        coVerify(exactly = 2) { bookmarkApi.bookmarkArticleById(articleId1) }
        coVerify(exactly = 1) { bookmarkApi.bookmarkArticleById(articleId2) }
    }
    @Test
    fun `isBookmarked with valid ID  article is bookmarked`() = runTest {
        // Mock bookmarkApi.isBookmarked to return true for a valid ID and verify the repository method returns true.
        val articleId = FakeArticles.items.first().id
        coEvery { bookmarkApi.isBookmarked(articleId) } returns true

        val result = bookmarkRepository.isBookmarked(articleId)

        assertTrue(result)
        coVerify { bookmarkApi.isBookmarked(articleId) }
    }

    @Test
    fun `isBookmarked with valid ID article is not bookmarked`() = runTest {
        // Mock bookmarkApi.isBookmarked to return false for a valid ID and verify the repository method returns false.
        val articleId = FakeArticles.items.first().id
        coEvery { bookmarkApi.isBookmarked(articleId) } returns false

        val result = bookmarkRepository.isBookmarked(articleId)

        assertFalse(result)
        coVerify { bookmarkApi.isBookmarked(articleId) }
    }

    @Test
    fun `isBookmarked concurrent calls`() = runTest {
        // Test behavior when isBookmarked is called multiple times concurrently with the same or different IDs to check for data consistency and thread safety.
        val articleId1 = FakeArticles.items[0].id
        val articleId2 = FakeArticles.items[1].id

        coEvery { bookmarkApi.isBookmarked(articleId1) } returns true
        coEvery { bookmarkApi.isBookmarked(articleId2) } returns false

        val results = mutableListOf<Boolean>()

        // Launch multiple coroutines to call isBookmarked concurrently
        val deferred1 = async(Dispatchers.IO) { results.add(bookmarkRepository.isBookmarked(articleId1)) }
        val deferred2 = async(Dispatchers.IO) { results.add(bookmarkRepository.isBookmarked(articleId2)) }
        val deferred3 = async(Dispatchers.IO) { results.add(bookmarkRepository.isBookmarked(articleId1)) } // Same ID as first

        awaitAll(deferred1, deferred2, deferred3)

        // Verify that the API was called the correct number of times
        coVerify(exactly = 2) { bookmarkApi.isBookmarked(articleId1) }
        coVerify(exactly = 1) { bookmarkApi.isBookmarked(articleId2) }

        // Verify results (order might not be guaranteed due to concurrency, so check counts)
        assertEquals(3, results.size)
        assertEquals(2, results.count { it }) // Two calls for articleId1 should be true
        assertEquals(1, results.count { !it }) // One call for articleId2 should be false
    }

    @Test
    fun `bookmarkArticleById and then isBookmarked consistency`() = runTest {
        // Call bookmarkArticleById, then isBookmarked with the same ID.
        // Mock the API calls such that isBookmarked reflects the bookmarking action (e.g., API returns true after bookmarking).
        val articleId = FakeArticles.items.first().id

        // Initial state: not bookmarked
        coEvery { bookmarkApi.isBookmarked(articleId) } returns false
        assertFalse(bookmarkRepository.isBookmarked(articleId))

        // Bookmark the article
        coEvery { bookmarkApi.bookmarkArticleById(articleId) } returns Unit
        // After bookmarking, isBookmarked should return true
        coEvery { bookmarkApi.isBookmarked(articleId) } returns true // Update mock for subsequent call

        bookmarkRepository.bookmarkArticleById(articleId)
        val isNowBookmarked = bookmarkRepository.isBookmarked(articleId)

        assertTrue(isNowBookmarked)
        coVerify(exactly = 1) { bookmarkApi.bookmarkArticleById(articleId) }
        coVerify(exactly = 2) { bookmarkApi.isBookmarked(articleId) } // Called once before, once after
    }
}