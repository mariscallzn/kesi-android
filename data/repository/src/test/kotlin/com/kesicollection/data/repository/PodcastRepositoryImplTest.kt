package com.kesicollection.data.repository

import com.google.common.truth.Truth.assertThat
import com.kesicollection.data.api.PodcastApi
import com.kesicollection.test.core.fake.FakePodcasts
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

/**
 * Test class for [PodcastRepositoryImpl].
 */
class PodcastRepositoryImplTest {

    /**
     * Mocked instance of [PodcastApi] for testing.
     */
    private val podcastApi: PodcastApi = mockk()
    /**
     * Instance of [PodcastRepositoryImpl] under test.
     */
    private lateinit var podcastRepository: PodcastRepositoryImpl

    /**
     * Sets up the test environment before each test case.
     * Initializes [podcastRepository] with the mocked [podcastApi].
     */
    @Before
    fun setUp() {
        podcastRepository = PodcastRepositoryImpl(
            remotePodcastApi = podcastApi
        )
    }
    /**
     * Test case to verify that `getPodcastById` returns a success result when the remote API call is successful.
     */
    @Test
    fun `getPodcastById returns success when remote api call is successful`() = runTest {
        // Given
        val podcastId = FakePodcasts.items.first().id
        coEvery { podcastApi.getPodcastById(podcastId) } returns Result.success(FakePodcasts.items.first())

        // When
        val result = podcastRepository.getPodcastById(podcastId)

        // Then
        coVerify(exactly = 1) { podcastApi.getPodcastById(podcastId) }
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(FakePodcasts.items.first())
    }
    /**
     * Test case to verify that `getPodcastById` returns a failure result when the remote API call fails.
     */
    @Test
    fun `getPodcastById returns failure when remote api call fails`() = runTest {
        // Given
        val podcastId = "nonExistentId"
        val expectedException = IOException("Podcast not found or API error")
        coEvery { podcastApi.getPodcastById(podcastId) } returns Result.failure(expectedException)

        // When
        val result = podcastRepository.getPodcastById(podcastId)

        // Then
        coVerify(exactly = 1) { podcastApi.getPodcastById(podcastId) }
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(expectedException)
        assertThat(result.exceptionOrNull()).isInstanceOf(IOException::class.java)
    }

}