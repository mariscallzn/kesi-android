package com.kesicollection.data.repository

import com.google.common.truth.Truth.assertThat
import com.kesicollection.data.api.DiscoverApi
import com.kesicollection.test.core.fake.FakeDiscover
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

/**
 * Test class for [DiscoverRepositoryImpl].
 */
class DiscoverRepositoryImplTest {

    private val discoverApi: DiscoverApi = mockk()
    private lateinit var discoverRepository: DiscoverRepositoryImpl

    /**
     * Sets up the test environment before each test.
     * Initializes [discoverRepository] with a mocked [DiscoverApi].
     */
    @Before
    fun setUp() {
        discoverRepository = DiscoverRepositoryImpl(
            remoteDiscoverApi = discoverApi
        )
    }

    /**
     * Tests that [DiscoverRepositoryImpl.getDiscoverContent] returns a successful result
     * when the API call is successful.
     */
    @Test
    fun `getDiscoverContent returns success when api call is successful`() = runTest {
        // Given
        val mockDiscoverData = FakeDiscover.items.first()
        coEvery { discoverApi.getDiscoverContent() } returns Result.success(mockDiscoverData)

        // When
        val result = discoverRepository.getDiscoverContent()

        // Then
        coVerify(exactly = 1) { discoverApi.getDiscoverContent() }
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(mockDiscoverData)
    }

    /**
     * Tests that [DiscoverRepositoryImpl.getDiscoverContent] returns a failure result
     * when the API call fails.
     */
    @Test
    fun `getDiscoverContent returns failure when api call fails`() = runTest {
        // Given
        val expectedException = IOException("Network error")
        coEvery { discoverApi.getDiscoverContent() } returns Result.failure(expectedException)

        // When
        val result = discoverRepository.getDiscoverContent()

        // Then
        coVerify(exactly = 1) { discoverApi.getDiscoverContent() }
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(expectedException)
    }

}