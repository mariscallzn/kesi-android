package com.kesicollection.data.retrofit

import com.kesicollection.core.model.Podcast
import com.kesicollection.data.api.PodcastApi
import com.kesicollection.data.retrofit.model.kesiandroid.asPodcast
import com.kesicollection.data.retrofit.service.KesiAndroidService
import javax.inject.Inject

/**
 * Implementation of [PodcastApi] that uses Retrofit to fetch podcast data from the KesiAndroidService.
 *
 * @property kesiAndroidService The Retrofit service for accessing KesiAndroid API.
 */
class RetrofitPodcastApi @Inject constructor(
    private val kesiAndroidService: KesiAndroidService,
) : PodcastApi {
    /**
     * Retrieves a podcast by its ID.
     *
     * @param id The ID of the podcast to retrieve.
     * @return A [Result] containing the [Podcast] if successful, or an exception if an error occurred.
     */
    override suspend fun getPodcastById(id: String): Result<Podcast> = Result.runCatching {
        kesiAndroidService.getPodcastById(id).asPodcast()
    }

    /**
     * Retrieves all podcasts.
     *
     * @return A [Result] containing a list of [Podcast] objects if successful, or an exception if an error occurred.
     */
    override suspend fun getAllPodcasts(): Result<List<Podcast>> = Result.runCatching {
        kesiAndroidService.getAllPodcasts().map { it.asPodcast() }
    }
}


