package com.kesicollection.feature.audioplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kesicollection.core.app.CrashlyticsWrapper
import com.kesicollection.core.model.ErrorState
import com.kesicollection.domain.GetPodcastByIdUseCase
import com.kesicollection.feature.audioplayer.di.AudioPlayerDefaultDispatcher
import com.kesicollection.feature.audioplayer.player.AudioPlayerController
import com.kesicollection.feature.audioplayer.player.DomainMediaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.CancellationException
import javax.inject.Inject

/**
 * ViewModel for the Audio Player screen.
 *
 * This ViewModel manages the state and logic for playing audio, including fetching podcast details,
 * controlling playback (play, pause, seek), and updating the UI state.
 *
 * @param audioPlayerController The controller responsible for handling audio playback.
 * @param dispatcher The coroutine dispatcher used for background tasks.
 * @param getPodcastByIdUseCase The use case for fetching podcast details by ID.
 */
@HiltViewModel
class AudioPlayerViewModel @Inject constructor(
    private val audioPlayerController: AudioPlayerController,
    @AudioPlayerDefaultDispatcher private val dispatcher: CoroutineDispatcher,
    private val getPodcastByIdUseCase: GetPodcastByIdUseCase,
    private val crashlyticsWrapper: CrashlyticsWrapper,
) : ViewModel() {

    /**
     * The current playback position in milliseconds.
     * This value is obtained from the [AudioPlayerController].
     */
    val currentPositionMs = audioPlayerController.currentPositionMs

    /**
     * Internal state for the UI, not exposed directly to the UI.
     * This state is combined with the player state to create the final UI state.
     */
    private val _uiState: MutableStateFlow<UiAudioPlayerState> = MutableStateFlow(initialState)

    /**
     * The current state of the audio player UI.
     *
     * This property is a [StateFlow] that combines the internal `_uiState` with the `playerState`
     * from the [AudioPlayerController]. It emits a new [UiAudioPlayerState] whenever either of
     * these underlying flows emits a new value.
     *
     * The flow is shared and started when there is at least one subscriber and stopped after a
     * 5-second timeout when there are no subscribers.
     * The initial state is provided by the `initialState` property.
     */
    val uiState = combine(
        _uiState,
        audioPlayerController.playerState,
    ) { uiState, playerState ->
        uiState.copy(playerState = playerState)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialState
    )

    /**
     * A [MutableSharedFlow] that emits [Intent] objects.
     * This flow is used to send intents from the UI to the ViewModel.
     */
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
                    is Intent.FetchPodcast -> fetchPodcast(it.id)
                }
            }
        }
    }

    /**
     * Sends an intent to the ViewModel for processing.
     * This function launches a coroutine in the ViewModel's scope to emit the given action
     * to the internal `intent` SharedFlow, which is then collected to handle different intents.
     *
     * @param action The [Intent] to be processed by the ViewModel.
     */
    fun sendIntent(action: Intent) {
        viewModelScope.launch(dispatcher) {
            intent.emit(action)
        }
    }

    /**
     * Fetches a podcast by its ID and updates the UI state accordingly.
     *
     * This function is a suspend function, meaning it can be paused and resumed, allowing for
     * asynchronous operations without blocking the main thread.
     *
     * It first sets the UI state to loading, clears any existing podcast data and error messages.
     * Then, it attempts to retrieve the podcast using the `getPodcastByIdUseCase`.
     *
     * If the podcast is fetched successfully:
     *  - The UI state is updated with the fetched podcast data.
     *
     * If an exception occurs during the fetch operation:
     *  - The UI state is updated to reflect an error, clearing any podcast data,
     *    setting loading to false, and populating the error state with details
     *    about the failure.
     *
     * @param id The unique identifier of the podcast to fetch.
     */
    private suspend fun fetchPodcast(id: String) {
        try {
            reducer { copy(isLoading = true, podcast = null, error = null) }
            val podcast = getPodcastByIdUseCase(id).getOrThrow()
            reducer {
                copy(podcast = podcast)
            }
        } catch (_: CancellationException) {
            /* no-op */
        } catch (e: Exception) {
            reducer {
                copy(
                    podcast = null,
                    isLoading = false,
                    error = ErrorState(
                        AudioPlayerErrors.UnknownError,
                        e.message
                    )
                )
            }
            crashlyticsWrapper.recordException(
                e, mapOf(
                    crashlyticsWrapper.params.screenName to "AudioPlayerScreen",
                    crashlyticsWrapper.params.className to "AudioPlayerViewModel",
                    crashlyticsWrapper.params.action to "fetch",
                )
            )
        }
    }

    /**
     * Seeks the audio playback to the specified distance.
     *
     * @param distance The distance to seek to, in milliseconds.
     */
    private fun seekTo(distance: Float) {
        audioPlayerController.seekTo(distance.toLong())
    }

    /**
     * Initializes the screen with the provided podcast data.
     * This function prepares and plays the audio associated with the given podcast.
     *
     * @param data The [Intent.InitScreen] object containing the podcast data to be played.
     */
    private fun initScreen(data: Intent.InitScreen) {
        audioPlayerController.prepareAndPlay(
            DomainMediaItem(
                id = data.podcast.id,
                fileName = data.podcast.audio,
                title = data.podcast.title,
                artworkUri = data.podcast.img
            )
        )
    }

    /**
     * Pauses the audio playback.
     * This function calls the `pause` method on the [audioPlayerController]
     * to stop the currently playing audio.
     */
    private fun pauseAudio() {
        audioPlayerController.pause()
    }

    /**
     * Resumes playback of the current audio track.
     * This function calls the `play` method on the [audioPlayerController].
     */
    private fun playAudio() {
        audioPlayerController.play()
    }

    /**
     * Updates the UI state by applying the given reducer function.
     * This function takes a [Reducer] lambda, which is an extension function on [UiAudioPlayerState].
     * It computes the new state by invoking the reducer on the current [UiAudioPlayerState]
     * and then updates the internal [_uiState] with the new state.
     *
     * @param reducer A lambda function that takes the current [UiAudioPlayerState] as its receiver
     *                and returns a new [UiAudioPlayerState].
     */
    private fun reducer(reducer: Reducer) {
        val newState = _uiState.value.reducer()
        _uiState.value = newState
    }

    /**
     * Called when the ViewModel is about to be destroyed.
     * Releases the player controller and its resources.
     */
    override fun onCleared() {
        super.onCleared()
        audioPlayerController.release()
    }
}