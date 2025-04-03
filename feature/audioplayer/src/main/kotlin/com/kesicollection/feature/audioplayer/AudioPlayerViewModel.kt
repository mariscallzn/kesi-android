package com.kesicollection.feature.audioplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioPlayerViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val intent = MutableSharedFlow<Intent>()

    init {
        viewModelScope.launch {
            intent.collect {
                when (it) {
                    Intent.PlayAudio -> playAudio()
                    Intent.PauseAudio -> pauseAudio()
                    is Intent.InitScreen -> initScreen(it)
                }
            }
        }
    }

    private fun initScreen(data: Intent.InitScreen) {
        reducer { copy(title = data.title) }
    }

    fun sendIntent(action: Intent) {
        viewModelScope.launch {
            intent.emit(action)
        }
    }

    private fun pauseAudio() {
        reducer { copy(isPlaying = false) }
    }

    private fun playAudio() {
        reducer { copy(isPlaying = true) }
    }

    private fun reducer(reducer: Reducer) {
        val newState = _uiState.value.reducer()
        _uiState.value = newState
    }
}