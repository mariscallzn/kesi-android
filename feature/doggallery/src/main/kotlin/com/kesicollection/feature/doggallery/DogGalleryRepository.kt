package com.kesicollection.feature.doggallery

import com.kesicollection.data.api.DogImagesApi
import javax.inject.Inject
import javax.inject.Named

/**
 * Repository interface for fetching dog images from a remote data source.
 */
interface DogGalleryRepository {
    suspend fun getDogImagesByBreed(breed: String, max: Int): Result<List<String>>
}

/**
 * Concrete implementation of [DogGalleryRepository].
 *
 * This class is responsible for fetching dog images from the network.
 * It utilizes the [DogImagesApi] to communicate with the dog image API.
 *
 * @property dogImagesApi The API interface for retrieving dog images. Injected with [Named("retrofit_dog_images_api")].
 */
class DogGalleryRepositoryImpl @Inject constructor(
    @Named("retrofit_dog_images_api")
    private val dogImagesApi: DogImagesApi,
) : DogGalleryRepository {
    override suspend fun getDogImagesByBreed(
        breed: String,
        max: Int
    ): Result<List<String>> = dogImagesApi.getImagesByBreed(breed, max)
}