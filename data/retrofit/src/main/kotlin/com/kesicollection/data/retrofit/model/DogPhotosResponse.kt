package com.kesicollection.data.retrofit.model

import kotlinx.serialization.Serializable

/**
 * Represents the response from the Dog API for a list of dog photos.
 *
 * @property message A list of URLs, each representing a dog photo.
 * @property status The status of the API request (e.g., "success").
 */
@Serializable
data class DogPhotosResponse(
    val message: List<String>,
    val status: String
)
