package com.kesicollection.data.retrofit.service

import com.kesicollection.data.retrofit.model.kesiandroid.NetworkArticle
import com.kesicollection.data.retrofit.model.kesiandroid.NetworkIndexArticle
import retrofit2.http.GET
import retrofit2.http.Path

interface KesiAndroidService {
    @GET("articles/index.json")
    suspend fun fetchAllArticles(): List<NetworkIndexArticle>

    @GET("articles/{id}.json")
    suspend fun getArticleById(
        @Path("id")
        id: String
    ): NetworkArticle
}