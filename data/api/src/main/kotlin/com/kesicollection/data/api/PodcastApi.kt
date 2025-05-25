package com.kesicollection.data.api

import com.kesicollection.core.model.Podcast

/**
 * Interface for interacting with the podcast API.
 */
interface PodcastApi {
    /**
     * Retrieves a podcast by its ID.
     * @param id The ID of the podcast to retrieve.
     * @return A [Result] containing the [Podcast] if successful, or an error otherwise.
     */
    suspend fun getPodcastById(id: String): Result<Podcast>
    /**
     * Retrieves all podcasts.
     * @return A [Result] containing a list of [Podcast] objects if successful, or an error otherwise.
     */
    suspend fun getAllPodcasts(): Result<List<Podcast>>
}