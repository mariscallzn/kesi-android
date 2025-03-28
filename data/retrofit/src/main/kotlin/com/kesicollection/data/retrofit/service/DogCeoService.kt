package com.kesicollection.data.retrofit.service

import com.kesicollection.data.retrofit.model.DogPhotosResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Interface for interacting with the Dog CEO API.
 *
 * This service provides endpoints to retrieve random dog images based on breed.
 */
interface DogCeoService {
    @GET("breed/{breed}/images/random/{max}")
    suspend fun getRandomImageByBreed(
        @Path("breed") breed: String,
        @Path("max") max: Int,
    ): DogPhotosResponse
}