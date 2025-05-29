package com.kesicollection.data.repository

import com.kesicollection.data.api.MarkdownApi
import javax.inject.Inject

/**
 * Repository for handling markdown data.
 *
 * This interface defines the contract for downloading markdown content.
 */
interface MarkdownRepository {
    /**
     * Downloads the markdown content as a string for the given ID.
     *
     * @param fileName The identifier of the markdown content to download.
     * @return A [Result] containing the markdown string on success, or an error on failure.
     */
    suspend fun downloadAsString(fileName: String): Result<String>
}

/**
 * Implementation of [MarkdownRepository] that fetches markdown content from a remote API.
 *
 * @property remoteMarkdownApi The remote API service for accessing markdown content.
 */
class MarkdownRepositoryImpl @Inject constructor(
    private val remoteMarkdownApi: MarkdownApi,
) : MarkdownRepository {
    /**
     * Downloads the markdown content as a string for the given ID by delegating to the [remoteMarkdownApi].
     * @param fileName The identifier of the markdown content to download.
     * @return A [Result] containing the markdown string on success, or an error on failure.
     */
    override suspend fun downloadAsString(fileName: String): Result<String> =
        remoteMarkdownApi.downloadAsString(fileName)
}