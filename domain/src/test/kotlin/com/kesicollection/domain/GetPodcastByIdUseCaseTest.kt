package com.kesicollection.domain

import com.google.common.truth.Truth.assertThat
import com.kesicollection.data.repository.PodcastRepository
import com.kesicollection.test.core.fake.FakePodcasts
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for the [GetPodcastByIdUseCase].
 * This class tests the behavior of the use case when fetching a podcast by its ID.
 */
class GetPodcastByIdUseCaseTest {

    /**
     * Mock implementation of [PodcastRepository] used for testing.
     */
    private lateinit var mockPodcastRepository: PodcastRepository

    /**
     * The instance of [GetPodcastByIdUseCase] being tested.
     */
    private lateinit var getPodcastByIdUseCase: GetPodcastByIdUseCase

    /**
     * Sets up the test environment before each test case.
     * Initializes the mock repository and the use case.
     */
    @Before
    fun setUp() {
        mockPodcastRepository = mockk()
        getPodcastByIdUseCase = GetPodcastByIdUseCase(mockPodcastRepository)
    }

    /**
     * Tests the scenario where a podcast with an existing ID is requested,
     * and the repository successfully returns the podcast.
     *
     * It verifies that the use case returns a [Result.success] with the correct podcast data.
     */
    @Test
    fun `invoke with existing ID when repository returns podcast successfully returns success with podcast data`() = runTest {
        // Given
        val fakePodcast = FakePodcasts.items.firstOrNull() // Get a podcast from fake data
        assertThat(fakePodcast).isNotNull() // Ensure fake data exists
        val podcastId = fakePodcast!!.id

        coEvery { mockPodcastRepository.getPodcastById(podcastId) } returns Result.success(fakePodcast)

        // When
        val result = getPodcastByIdUseCase(podcastId)

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(fakePodcast)
    }

    /**
     * Tests the scenario where a podcast with a non-existent ID is requested,
     * and the repository returns a failure.
     *
     * It verifies that the use case returns a [Result.failure] with the expected exception.
     */
    @Test
    fun `invoke with non-existent ID when repository returns failure returns failure`() = runTest {
        // Given
        val podcastId = "non_existent_id"
        val expectedException = NoSuchElementException("Podcast with ID $podcastId not found")
        coEvery { mockPodcastRepository.getPodcastById(podcastId) } returns Result.failure(expectedException)

        // When
        val result = getPodcastByIdUseCase(podcastId)

        // Then
        assertThat(result.isFailure).isTrue()
        val actualException = result.exceptionOrNull()
        assertThat(actualException).isEqualTo(expectedException)
        assertThat(actualException).isInstanceOf(NoSuchElementException::class.java)
        assertThat(actualException?.message).isEqualTo("Podcast with ID $podcastId not found")
    }

    /**
     * Tests the scenario where an empty podcast ID is provided, and the repository
     * (or the use case's input validation if implemented there) handles this by returning a failure.
     * This specific test assumes the repository handles the validation.
     *
     * It verifies that the use case returns a [Result.failure] with an [IllegalArgumentException].
     */
    @Test
    fun `invoke with empty ID when repository handles it (e_g_ returns failure)`() = runTest {
        // Given
        val podcastId = ""
        val expectedException = IllegalArgumentException("Podcast ID cannot be empty")
        coEvery { mockPodcastRepository.getPodcastById(podcastId) } returns Result.failure(expectedException)

        // When
        val result = getPodcastByIdUseCase(podcastId)

        // Then
        assertThat(result.isFailure).isTrue()
        val actualException = result.exceptionOrNull()
        assertThat(actualException).isInstanceOf(IllegalArgumentException::class.java)
        assertThat(actualException?.message).isEqualTo("Podcast ID cannot be empty")
    }
}