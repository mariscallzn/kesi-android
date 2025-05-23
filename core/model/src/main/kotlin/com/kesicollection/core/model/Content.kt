package com.kesicollection.core.model

data class Content(
    val id: String,
    val img: String,
    val type: ContentType,
    val title: String,
    val description: String,
)
