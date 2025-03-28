package com.kesicollection.data.retrofit

import com.kesicollection.data.api.DogImagesApi
import com.kesicollection.data.retrofit.service.DogCeoService
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [DogImagesApi] that uses Retrofit to fetch dog images from the Dog CEO API.
 *
 * This class is a singleton, meaning only one instance of it will be created and shared throughout the application.
 * It utilizes [DogCeoService] to interact with the API.
 *
 * @property service The [DogCeoService] used to make network requests.
 */
@Singleton
class RetrofitDogImagesApi @Inject constructor(
    private val service: DogCeoService
) : DogImagesApi {
    override suspend fun getImagesByBreed(
        breed: String,
        max: Int
    ): Result<List<String>> = Result.runCatching {
        service.getRandomImageByBreed(breed, max).message
    }
}