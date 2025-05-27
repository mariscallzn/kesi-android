package com.kesicollection.domain

import com.kesicollection.core.model.Podcast
import com.kesicollection.data.repository.PodcastRepository
import javax.inject.Inject

/**
 * Use case for retrieving a list of podcasts.
 *
 * This use case encapsulates the logic for fetching all podcasts from the repository.
 * It utilizes dependency injection to obtain an instance of [PodcastRepository].
 *
 * @property podcastRepository The repository responsible for podcast data operations.
 */
class GetPodcastsUseCase @Inject constructor(
    private val podcastRepository: PodcastRepository,
) {
    /**
     * Executes the use case to get all podcasts.
     */
    suspend operator fun invoke(): Result<List<Podcast>> = podcastRepository.getAllPodcasts()
}