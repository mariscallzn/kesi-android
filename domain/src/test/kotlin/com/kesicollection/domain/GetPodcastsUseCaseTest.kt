package com.kesicollection.domain

import com.google.common.truth.Truth.assertThat
import com.kesicollection.core.model.Podcast
import com.kesicollection.data.repository.PodcastRepository
import com.kesicollection.test.core.fake.FakePodcasts
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

/**
 * Unit tests for the [GetPodcastsUseCase].
 */
class GetPodcastsUseCaseTest {

    private lateinit var mockPodcastRepository: PodcastRepository
    private lateinit var getPodcastsUseCase: GetPodcastsUseCase

    /**
     * Sets up the test environment before each test.
     * Initializes the mock [PodcastRepository] and the [GetPodcastsUseCase] instance.
     */
    @Before
    fun setUp() {
        mockPodcastRepository = mockk()
        getPodcastsUseCase = GetPodcastsUseCase(mockPodcastRepository)
    }

    /**
     * Tests that [GetPodcastsUseCase.invoke] returns a [Result.success] with a list of podcasts
     * when the repository returns podcasts successfully.
     */
    @Test
    fun `invoke when repository returns podcasts successfully returns success with podcast list`() = runTest {
        // Given
        val fakePodcastList = FakePodcasts.items // Use your fake data directly
        coEvery { mockPodcastRepository.getAllPodcasts() } returns Result.success(fakePodcastList)

        // When
        val result = getPodcastsUseCase() // UseCase invoke()

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(fakePodcastList)
        assertThat(result.getOrNull()).hasSize(FakePodcasts.items.size)
        if (fakePodcastList.isNotEmpty()) {
            assertThat(result.getOrNull()?.first()).isEqualTo(fakePodcastList.first())
        }
    }

    /**
     * Tests that [GetPodcastsUseCase.invoke] returns a [Result.success] with an empty list
     * when the repository returns an empty list of podcasts successfully.
     */
    @Test
    fun `invoke when repository returns empty list successfully returns success with empty list`() = runTest { // ktlint-disable max-line-length
        // Given
        val emptyPodcastList = emptyList<Podcast>()
        coEvery { mockPodcastRepository.getAllPodcasts() } returns Result.success(emptyPodcastList)

        // When
        val result = getPodcastsUseCase()

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(emptyPodcastList)
        assertThat(result.getOrNull()).isEmpty()
    }

    /**
     * Tests that [GetPodcastsUseCase.invoke] returns a [Result.failure] with the same exception
     * when the repository returns a failure.
     */
    @Test
    fun `invoke when repository returns failure returns failure with the same exception`() = runTest { // ktlint-disable max-line-length
        // Given
        val expectedException = IOException("Network error fetching podcasts")
        coEvery { mockPodcastRepository.getAllPodcasts() } returns Result.failure(expectedException)

        // When
        val result = getPodcastsUseCase()

        // Then
        assertThat(result.isFailure).isTrue()
        val actualException = result.exceptionOrNull()
        assertThat(actualException).isEqualTo(expectedException)
        assertThat(actualException).isInstanceOf(IOException::class.java)
        assertThat(actualException?.message).isEqualTo("Network error fetching podcasts")
    }
}