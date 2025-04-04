package com.kesicollection.feature.audioplayer

import com.kesicollection.core.uisystem.ErrorState

val initialState = UiAudioPlayerState()

data class UiAudioPlayerState(
    val title: String = "",
    val isPlaying: Boolean = false,
    val error: ErrorState<AudioPlayerErrors>? = null
)

sealed interface Intent {
    data class InitScreen(val title: String) : Intent
    data object PlayAudio : Intent
    data object PauseAudio : Intent
}

typealias Reducer = UiAudioPlayerState.() -> UiAudioPlayerState

enum class AudioPlayerErrors {
    UnknownError
}