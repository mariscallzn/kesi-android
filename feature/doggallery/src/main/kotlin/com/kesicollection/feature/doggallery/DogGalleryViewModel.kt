package com.kesicollection.feature.doggallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DogGalleryViewModel @Inject constructor() : ViewModel() {

    private val intent = MutableSharedFlow<Intent>()

    private var _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<UiDoggGalleryState>
        get() = _uiState

    init {
        viewModelScope.launch {
            intent.collect {
                processIntent(it)
            }
        }
    }

    private fun reduce(reducer: UiDoggGalleryState.() -> UiDoggGalleryState) {
        val newState = _uiState.value.reducer()
        _uiState.value = newState
    }

    fun sendIntent() {

    }

    private fun processIntent(intent: Intent) {
        when(intent) {
            is Intent.FetchDogsByBreed -> fetchDogsByBreed(intent.breed)
        }
    }

    private fun fetchDogsByBreed(breed: String) {
        //TODO:
    }
}