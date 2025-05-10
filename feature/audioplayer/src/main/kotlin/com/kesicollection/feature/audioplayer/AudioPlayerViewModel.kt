package com.kesicollection.feature.audioplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kesicollection.feature.audioplayer.di.AudioPlayerDefaultDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioPlayerViewModel @Inject constructor(
    private val audioPlayerController: AudioPlayerController,
    @AudioPlayerDefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    val currentPositionMs = audioPlayerController.currentPositionMs

    private val _uiState = MutableStateFlow(initialState)
    val uiState = combine(
        _uiState,
        audioPlayerController.playerState,
    ) { uiState, playerState ->
        uiState.copy(
            playerState = playerState,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialState
    )

    private val intent = MutableSharedFlow<Intent>()

    init {
        audioPlayerController.initialize()
        viewModelScope.launch(dispatcher) {
            intent.collect {
                when (it) {
                    Intent.PlayAudio -> playAudio()
                    Intent.PauseAudio -> pauseAudio()
                    is Intent.InitScreen -> initScreen(it)
                    is Intent.SeekTo -> seekTo(it.distance)
                }
            }
        }
    }

    private fun seekTo(distance: Float) {
        audioPlayerController.seekTo(distance.toLong())
    }

    /**
     * Called when the ViewModel is about to be destroyed.
     * Releases the player controller and its resources.
     */
    override fun onCleared() {
        super.onCleared()
        audioPlayerController.release()
    }

    private fun initScreen(data: Intent.InitScreen) {
        audioPlayerController.prepareAndPlay(DomainMediaItem(id = data.title, fileName = data.fileName))
        reducer { copy(title = data.title) }
    }

    fun sendIntent(action: Intent) {
        viewModelScope.launch(dispatcher) {
            intent.emit(action)
        }
    }

    private fun pauseAudio() {
        audioPlayerController.pause()
    }

    private fun playAudio() {
        audioPlayerController.play()
    }

    private fun reducer(reducer: Reducer) {
        val newState = _uiState.value.reducer()
        _uiState.value = newState
    }
}