package com.kesicollection.data.repository

import com.kesicollection.core.model.Podcast
import com.kesicollection.data.api.PodcastApi
import javax.inject.Inject
/**
 * Interface for accessing podcast data.
 */
interface PodcastRepository {
    /**
     * Retrieves a podcast by its ID.
     * @param id The ID of the podcast to retrieve.
     * @return A [Result] containing the [Podcast] if found, or an error.
     */
    suspend fun getPodcastById(id: String): Result<Podcast>
    /**
     * Retrieves all podcasts.
     * @return A [Result] containing a list of all [Podcast]s, or an error.
     */
    suspend fun getAllPodcasts(): Result<List<Podcast>>
}
/**
 * Implementation of [PodcastRepository] that fetches data from a remote API.
 * @property remotePodcastApi The remote API for accessing podcast data.
 */
class PodcastRepositoryImpl @Inject constructor(
    private val remotePodcastApi: PodcastApi,
) : PodcastRepository {
    /**
     * Retrieves a podcast by its ID from the remote API.
     * @param id The ID of the podcast to retrieve.
     * @return A [Result] containing the [Podcast] if found, or an error.
     */
    override suspend fun getPodcastById(id: String): Result<Podcast> =
        remotePodcastApi.getPodcastById(id)
    /**
     * Retrieves all podcasts from the remote API.
     * @return A [Result] containing a list of all [Podcast]s, or an error.
     */
    override suspend fun getAllPodcasts(): Result<List<Podcast>> = remotePodcastApi.getAllPodcasts()
}