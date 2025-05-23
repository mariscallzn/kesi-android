package com.kesicollection.data.api

import com.kesicollection.core.model.Discover

/**
 * Represents the API for discovering content.
 */
interface DiscoverApi {
    /**
     * Fetches the discover content from the API.
     *
     * This function makes a network request to retrieve the data needed to populate
     * the discover section of the application.
     *
     * @return A [Result] object containing either a [Discover] object on success,
     *         or an exception on failure.
     */
    suspend fun getDiscoverContent(): Result<Discover>
}