package com.kesicollection.feature.audioplayer

import com.kesicollection.core.uisystem.ErrorState

val initialState = UiAudioPlayerState()

data class UiAudioPlayerState(
    val playerState: PlayerStateUi = PlayerStateUi.Idle,
    val title: String = "",
    val error: ErrorState<AudioPlayerErrors>? = null
)

sealed interface Intent {
    data class InitScreen(
        val title: String,
        val fileName: String
    ) : Intent

    data object PlayAudio : Intent
    data object PauseAudio : Intent
    data class SeekTo(val distance: Float) : Intent
}

typealias Reducer = UiAudioPlayerState.() -> UiAudioPlayerState

enum class AudioPlayerErrors {
    UnknownError
}