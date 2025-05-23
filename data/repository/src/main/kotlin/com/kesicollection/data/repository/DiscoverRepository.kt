package com.kesicollection.data.repository

import com.kesicollection.core.model.Discover
import com.kesicollection.data.api.DiscoverApi
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository interface for fetching discover content.
 *
 * This interface defines the contract for retrieving data related to the "Discover" section of the application.
 * Implementations of this interface are responsible for handling the actual data fetching logic,
 * whether it's from a remote API, local database, or any other data source.
 */
interface DiscoverRepository {
    suspend fun getDiscoverContent(): Result<Discover>
}

/**
 * Implementation of [DiscoverRepository] that fetches discover content from a remote API.
 *
 * @property remoteDiscoverApi The API service for fetching discover content.
 */
@Singleton
class DiscoverRepositoryImpl @Inject constructor(
    private val remoteDiscoverApi: DiscoverApi
) : DiscoverRepository {
    override suspend fun getDiscoverContent(): Result<Discover> =
        remoteDiscoverApi.getDiscoverContent()
}