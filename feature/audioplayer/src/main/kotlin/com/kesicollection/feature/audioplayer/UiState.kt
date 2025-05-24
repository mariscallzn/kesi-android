package com.kesicollection.feature.audioplayer

import com.kesicollection.core.model.ErrorState
import com.kesicollection.core.model.Podcast
import com.kesicollection.feature.audioplayer.player.PlayerStateUi

/**
 * Represents the initial state of the audio player UI.
 * This state is used when the audio player screen is first loaded or reset.
 */
val initialState = UiAudioPlayerState()

/**
 * Represents the state of the audio player UI.
 *
 * @property isLoading Indicates whether the audio player is currently loading.
 * @property playerState The current state of the audio player.
 * @property podcast The podcast currently loaded or being played.
 * @property error An error state, if any, encountered by the audio player.
 */
data class UiAudioPlayerState(
    val isLoading: Boolean = true,
    val playerState: PlayerStateUi = PlayerStateUi.Idle,
    val podcast: Podcast? = null,
    val error: ErrorState<AudioPlayerErrors>? = null
)

/**
 * Represents the different user interactions or events that can occur on the audio player screen.
 * These intents are processed by the ViewModel to update the [UiAudioPlayerState].
 */
sealed interface Intent {
    /**
     * Represents an intent to initialize the audio player screen with a specific podcast.
     *
     * @property podcast The podcast to be played.
     */
    data class InitScreen(
        val podcast: Podcast
    ) : Intent

    /**
     * Intent to fetch a podcast by its ID.
     *
     * @property id The ID of the podcast to fetch.
     */
    data class FetchPodcast(val id: String) : Intent
    /**
     * Represents an intent to start playing audio.
     * This object is used to signal the audio player to begin playback.
     */
    data object PlayAudio : Intent
    /**
     * Represents an intent to pause the currently playing audio.
     */
    data object PauseAudio : Intent
    /**
     * An intent to seek the audio playback to a specific position.
     *
     * @param distance The position to seek to, as a float value.
     *                 This typically represents a percentage (0.0 to 1.0) or a specific time in milliseconds,
     *                 depending on the underlying player implementation.
     */
    data class SeekTo(val distance: Float) : Intent
}

/**
 * Represents the possible errors that can occur within the audio player.
 */
enum class AudioPlayerErrors {
    UnknownError
}

typealias Reducer = UiAudioPlayerState.() -> UiAudioPlayerState