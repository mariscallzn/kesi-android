package com.kesicollection.data.api

import com.kesicollection.core.model.Podcast

interface PodcastApi {
    suspend fun getPodcastByArticleId(): Result<Podcast>
}