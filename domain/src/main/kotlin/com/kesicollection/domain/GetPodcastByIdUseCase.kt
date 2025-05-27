package com.kesicollection.domain

import com.kesicollection.core.model.Podcast
import com.kesicollection.data.repository.PodcastRepository
import javax.inject.Inject

/**
 * Use case for retrieving a podcast by its ID.
 *
 * This class encapsulates the business logic for fetching a specific podcast
 * from the repository.
 *
 * @property podcastRepository The repository responsible for podcast data operations.
 */
class GetPodcastByIdUseCase @Inject constructor(
    private val podcastRepository: PodcastRepository,
) {
    /**
     * Invokes the use case to get a podcast by its ID.
     */
    suspend operator fun invoke(id: String): Result<Podcast> = podcastRepository.getPodcastById(id)
}