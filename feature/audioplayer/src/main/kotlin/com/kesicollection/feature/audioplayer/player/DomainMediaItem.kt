package com.kesicollection.feature.audioplayer.player

data class DomainMediaItem(
    val id: String,
    val fileName: String,
    val title: String? = null,
    val artist: String? = null,
    val albumTitle: String? = null,
    val artworkUri: String? = null,
    val durationMs: Long? = null,
    val userExtras: Map<String, String>? = null
)
