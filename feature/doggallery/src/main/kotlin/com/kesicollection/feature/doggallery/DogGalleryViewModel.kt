package com.kesicollection.feature.doggallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogGalleryViewModel @Inject constructor(
    private val repo: DogGalleryRepository
) : ViewModel() {

    private val intentFlow = MutableSharedFlow<Intent>()

    private var _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<UiDoggGalleryState>
        get() = _uiState

    init {
        viewModelScope.launch {
            intentFlow.collect {
                processIntent(it)
            }
        }
    }

    private fun reduce(reducer: UiDoggGalleryState.() -> UiDoggGalleryState) {
        val newState = _uiState.value.reducer()
        _uiState.value = newState
    }

    fun sendIntent(intent: Intent) {
        viewModelScope.launch {
            intentFlow.emit(intent)
        }
    }

    private fun processIntent(intent: Intent) {
        when (intent) {
            is Intent.FetchDogsByBreed -> fetchDogsByBreed(intent.breed)
        }
    }

    private fun fetchDogsByBreed(breed: String) {
        viewModelScope.launch {
            val images = repo.getDogImagesByBreed(breed, 50).getOrThrow()
            reduce { copy(images = images) }
        }
    }
}