package com.kesicollection.data.retrofit.service

import com.kesicollection.data.retrofit.model.kesiandroid.NetworkArticle
import com.kesicollection.data.retrofit.model.kesiandroid.NetworkDiscover
import com.kesicollection.data.retrofit.model.kesiandroid.NetworkIndexArticle
import com.kesicollection.data.retrofit.model.kesiandroid.NetworkPodcast
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit service interface for interacting with the Kesi Android API.
 * This interface defines the HTTP operations for fetching articles and discover content.
 */
interface KesiAndroidService {
    /**
     * Fetches a list of all articles' metadata.
     * This endpoint retrieves an index of all available articles,
     * typically containing essential information like title, author, and publication date for each article.
     *
     * @return A list of [NetworkIndexArticle] objects, each representing metadata for an article.
     */
    @GET("articles/index.json")
    suspend fun fetchAllArticles(): List<NetworkIndexArticle>

    /**
     * Fetches a specific article by its ID.
     *
     * @param id The unique identifier of the article to retrieve.
     * @return A [NetworkArticle] object representing the fetched article.
     */
    @GET("articles/{id}.json")
    suspend fun getArticleById(
        @Path("id")
        id: String
    ): NetworkArticle

    /**
     * Retrieves the discover content.
     *
     * This function makes a GET request to the "discover.json" endpoint
     * to fetch the content displayed on the discover screen of the application.
     *
     * @return A [NetworkDiscover] object containing the discover content.
     */
    @GET("discover.json")
    suspend fun getDiscoverContent(): NetworkDiscover

    @GET("podcasts/{id}.json")
    suspend fun getPodcastById(
        @Path("id")
        id: String
    ): NetworkPodcast

    @GET("podcasts/index.json")
    suspend fun getAllPodcasts(): List<NetworkPodcast>
}