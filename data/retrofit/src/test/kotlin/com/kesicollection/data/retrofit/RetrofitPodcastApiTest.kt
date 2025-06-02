package com.kesicollection.data.retrofit

import com.google.common.truth.Truth.assertThat
import com.kesicollection.data.retrofit.fake.FakeNetworkPodcast
import com.kesicollection.data.retrofit.model.kesiandroid.NetworkPodcast
import com.kesicollection.data.retrofit.model.kesiandroid.asPodcast
import com.kesicollection.data.retrofit.service.KesiAndroidService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

class RetrofitPodcastApiTest {
    private val mockKesiAndroidService: KesiAndroidService = mockk()
    private lateinit var retrofitPodcastApi: RetrofitPodcastApi

    @Before
    fun setUp() {
        retrofitPodcastApi = RetrofitPodcastApi(
            kesiAndroidService = mockKesiAndroidService
        )
    }

    @Test
    fun `getPodcastById success - returns correct podcast`() = runTest {
        // Arrange
        val podcastId = FakeNetworkPodcast.items.first().id
        val fakeNetworkPodcast = FakeNetworkPodcast.items.first()
        val expectedPodcast = fakeNetworkPodcast.asPodcast()

        coEvery { mockKesiAndroidService.getPodcastById(podcastId) } returns fakeNetworkPodcast

        // Act
        val result = retrofitPodcastApi.getPodcastById(podcastId)

        // Assert
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(expectedPodcast)
        coVerify(exactly = 1) { mockKesiAndroidService.getPodcastById(podcastId) }
    }

    @Test
    fun `getPodcastById failure - returns error result`() = runTest {
        // Arrange
        val podcastId = "nonExistentId"
        val exception = IOException("Network error")
        coEvery { mockKesiAndroidService.getPodcastById(podcastId) } throws exception

        // Act
        val result = retrofitPodcastApi.getPodcastById(podcastId)

        // Assert
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify(exactly = 1) { mockKesiAndroidService.getPodcastById(podcastId) }
    }

    @Test
    fun `getAllPodcasts success - returns list of podcasts`() = runTest {
        // Arrange
        val fakeNetworkPodcasts = FakeNetworkPodcast.items
        val expectedPodcasts = fakeNetworkPodcasts.map { it.asPodcast() }

        coEvery { mockKesiAndroidService.getAllPodcasts() } returns fakeNetworkPodcasts

        // Act
        val result = retrofitPodcastApi.getAllPodcasts()

        // Assert
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(expectedPodcasts)
        coVerify(exactly = 1) { mockKesiAndroidService.getAllPodcasts() }
    }

    @Test
    fun `getAllPodcasts success - returns empty list when service returns empty`() = runTest {
        // Arrange
        val emptyNetworkPodcasts = emptyList<NetworkPodcast>()
        coEvery { mockKesiAndroidService.getAllPodcasts() } returns emptyNetworkPodcasts

        // Act
        val result = retrofitPodcastApi.getAllPodcasts()

        // Assert
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEmpty()
        coVerify(exactly = 1) { mockKesiAndroidService.getAllPodcasts() }
    }

    @Test
    fun `getAllPodcasts failure - returns error result`() = runTest {
        // Arrange
        val exception = IOException("Network error")
        coEvery { mockKesiAndroidService.getAllPodcasts() } throws exception

        // Act
        val result = retrofitPodcastApi.getAllPodcasts()

        // Assert
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify(exactly = 1) { mockKesiAndroidService.getAllPodcasts() }
    }

}