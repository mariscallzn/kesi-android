package com.kesicollection.feature.doggallery

val initialState = UiDoggGalleryState()

data class UiDoggGalleryState(
    val images: List<String> = emptyList(),
    val isLoading: Boolean = false,
)

sealed interface Intent {
    data class FetchDogsByBreed(val breed: String) : Intent
}