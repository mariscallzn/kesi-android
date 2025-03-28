package com.kesicollection.data.api

/**
 * Interface for fetching dog images from a remote data source.
 */
interface DogImagesApi {
    suspend fun getImagesByBreed(breed: String, max: Int): Result<List<String>>
}