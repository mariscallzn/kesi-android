package com.kesicollection.feature.audioplayer.player

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaMetadata
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.kesicollection.core.app.CrashlyticsWrapper
import com.kesicollection.core.app.qualifiers.KesiAndroidApiUrl
import com.kesicollection.feature.audioplayer.di.AudioPlayerDefaultDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutionException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interface defining the contract for an audio player controller.
 * This interface provides a standardized way to interact with an underlying audio playback mechanism.
 * Implementations of this interface are responsible for managing the playback lifecycle,
 * providing state updates, and handling user interactions.
 */
interface AudioPlayerController {
    /**
     * A [StateFlow] that emits the current state of the audio player.
     * This allows observers to react to changes in playback status (e.g., Idle, Playing, Paused, Error).
     *
     * Example states could include:
     * - [PlayerStateUi.Idle]: The player is not initialized or has been released.
     * - [PlayerStateUi.Ready]: The player is initialized and ready to accept commands.
     * - [PlayerStateUi.Buffering]: The player is buffering media content.
     * - [PlayerStateUi.Playing]: The player is currently playing media.
     * - [PlayerStateUi.Paused]: The player is currently paused.
     * - [PlayerStateUi.Ended]: The playback has reached the end of the media.
     * - [PlayerStateUi.Error]: An error occurred during playback or initialization.
     */
    val playerState: StateFlow<PlayerStateUi>

    /**
     * A [StateFlow] that emits the current playback position as a percentage (0.0 to 1.0) of the total duration.
     * This is useful for updating UI elements like progress bars.
     * The value is normalized, where 0.0 represents the beginning of the media and 1.0 represents the end.
     */
    val currentPositionMs: StateFlow<Float>

    /**
     * Prepares the player with the given [mediaItem] and starts playback.
     * If the player is already playing the same media item, this method might behave differently
     * (e.g., restart from the beginning or do nothing, depending on the implementation).
     * This method typically involves:
     * 1. Setting the media source for the player.
     * 2. Preparing the player for playback (which might include buffering).
     * 3. Starting the playback.
     *
     * @param mediaItem The [DomainMediaItem] to be played. This object should contain
     *                  all necessary information to identify and load the audio content
     *                  (e.g., ID, URL, metadata).
     */
    fun prepareAndPlay(mediaItem: DomainMediaItem)

    /**
     * Starts or resumes playback of the current media item.
     * If the player is paused, it will resume from the current position.
     * If the player is stopped or in an idle state (but a media item is loaded), it might start from the beginning.
     * If no media item is loaded or prepared, this method might have no effect or could trigger an error state.
     */
    fun play()

    /**
     * Pauses the current playback.
     * If the player is playing, it will be paused at its current position.
     * If the player is already paused or stopped, this method might have no effect.
     */
    fun pause()

    /**
     * Seeks to a specific position in the current media item.
     * The player will attempt to jump to the specified [positionMs] (in milliseconds).
     *
     * @param positionMs The target position in milliseconds from the beginning of the media.
     *                   Implementations should handle invalid values gracefully (e.g., negative values or values beyond the media duration).
     */
    fun seekTo(positionMs: Long)

    /**
     * Initializes the audio player resources.
     * This method should be called before any other playback operations.
     * It typically involves setting up connections to media services, allocating buffers, etc.
     * If the player is already initialized, calling this method again might have no effect.
     */
    fun initialize()

    /**
     * Releases any resources held by the audio player.
     * This method should be called when the player is no longer needed (e.g., when the component using it is destroyed).
     * It ensures that resources like media controllers, listeners, and system services are properly cleaned up
     * to prevent memory leaks or other issues. After calling release, the player might need to be initialized again
     * before it can be used.
     */
    fun release()
}

/**
 * An implementation of [AudioPlayerController] that uses Media3's [MediaController]
 * to interact with a [MediaSessionService][androidx.media3.session.MediaSessionService]
 * (specifically [AudioPlayerService] in this case) for background audio playback.
 *
 * This class manages the connection to the `MediaController`, handles player state updates,
 * and provides methods for controlling playback (play, pause, seek, prepare).
 * It also exposes the player's state and current playback position as [StateFlow]s
 * for UI observation.
 *
 * It acts as a bridge between the application's UI/ViewModel layer and the underlying
 * Media3 playback components.
 *
 * @property dispatcher The coroutine dispatcher used for background tasks and state updates.
 * @property context The application context, used for creating [SessionToken] and [MediaController].
 * @property kesiAndroidApiUrl The base URL for fetching audio files.
 */
@Singleton
class Media3AudioPlayerController @Inject constructor(
    @AudioPlayerDefaultDispatcher dispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context,
    @KesiAndroidApiUrl private val kesiAndroidApiUrl: String,
    private val crashlyticsWrapper: CrashlyticsWrapper,
) : AudioPlayerController, Player.Listener {

    private val coroutineScope = CoroutineScope(dispatcher + SupervisorJob())
    private var mediaController: MediaController? = null
    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var progressUpdateJob: Job? = null
    private val componentName: ComponentName by lazy {
        ComponentName(context, AudioPlayerService::class.java)
    }

    private val _playerState = MutableStateFlow<PlayerStateUi>(PlayerStateUi.Idle)
    override val playerState: StateFlow<PlayerStateUi> = _playerState.asStateFlow()
    private val _currentPositionMs = MutableStateFlow(0f)
    override val currentPositionMs: StateFlow<Float> = _currentPositionMs.asStateFlow()

    /**
     * Initializes the connection to the MediaController.
     * This method is asynchronous and will attempt to connect to the [AudioPlayerService].
     *
     * - If already initialized or in the process of initializing, this method does nothing.
     * - On successful connection, the [mediaController] is set, a listener is added,
     *   and the [_playerState] is updated to [PlayerStateUi.Ready].
     * - On failure (e.g., connection error, controller is null, interruption, or other exceptions),
     *   the [_playerState] is updated to [PlayerStateUi.Error] with a descriptive message.
     */
    override fun initialize() {
        if (mediaController != null || controllerFuture != null) return // Already initialized or initializing
        val sessionToken = SessionToken(context, componentName)
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture?.addListener({
            try {
                controllerFuture?.get()?.run {
                    mediaController = this
                    addListener(this@Media3AudioPlayerController)
                    _playerState.value = PlayerStateUi.Ready
                } ?: with(_playerState) {
                    value = PlayerStateUi.Error(
                        "Failed to connect to MediaController: Controller is null."
                    )
                }
            } catch (e: ExecutionException) {
                _playerState.value =
                    PlayerStateUi.Error("Connection error: ${e.cause?.message ?: e.message}")
                crashlyticsWrapper.recordException(
                    e, mapOf(
                        crashlyticsWrapper.params.screenName to "AudioPlayerScreen",
                        crashlyticsWrapper.params.className to "Media3AudioPlayerController",
                        crashlyticsWrapper.params.action to "initialize",
                    )
                )
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                _playerState.value = PlayerStateUi.Error("Connection interrupted.")
                crashlyticsWrapper.recordException(
                    e, mapOf(
                        crashlyticsWrapper.params.screenName to "AudioPlayerScreen",
                        crashlyticsWrapper.params.className to "Media3AudioPlayerController",
                        crashlyticsWrapper.params.action to "initialize",
                    )
                )
            } catch (e: Exception) {
                _playerState.value =
                    PlayerStateUi.Error("An unexpected error occurred during connection: ${e.message}")
                crashlyticsWrapper.recordException(
                    e, mapOf(
                        crashlyticsWrapper.params.screenName to "AudioPlayerScreen",
                        crashlyticsWrapper.params.className to "Media3AudioPlayerController",
                        crashlyticsWrapper.params.action to "initialize",
                    )
                )
            }
        }, ContextCompat.getMainExecutor(context))
    }

    /**
     * Called when the player's playing state changes.
     *
     * This function updates the UI state based on whether the player is currently playing.
     * If playing, it sets the state to [PlayerStateUi.Playing] and starts progress updates.
     * If not playing, it determines if the state is [PlayerStateUi.Ended] (if playback reached the end)
     * or [PlayerStateUi.Paused] (if paused by user or due to buffering, but not actively buffering).
     * It also stops progress updates when not playing.
     * Note: If the player stops because it's buffering, `onPlaybackStateChanged` will handle setting
     * the [PlayerStateUi.Buffering] state.
     *
     * @param isPlaying True if the player is currently playing, false otherwise.
     */
    override fun onIsPlayingChanged(isPlaying: Boolean) {
        if (isPlaying) {
            _playerState.value = PlayerStateUi.Playing
            startProgressUpdates()
        } else {
            // Determine if paused due to end of media or user action/buffering
            if (mediaController?.playbackState == Player.STATE_ENDED) {
                _playerState.value = PlayerStateUi.Ended
            } else if (mediaController?.playbackState != Player.STATE_BUFFERING) {
                _playerState.value = PlayerStateUi.Paused
            }
            // If it's just buffering, onPlaybackStateChanged will set PlayerStateUi.Buffering
            stopProgressUpdates()
        }
    }

    /**
     * Releases the MediaController and cleans up resources.
     *
     * This method should be called when the audio player is no longer needed,
     * typically in a lifecycle callback like `onDestroy` or `onCleared`.
     * It stops progress updates, removes the listener from the `MediaController`,
     * attempts to cancel any ongoing connection attempts, and releases the
     * `MediaController` instance and its future.
     */
    override fun release() {
        stopProgressUpdates()
        mediaController?.removeListener(this)
        controllerFuture?.let {
            if (!it.isDone && !it.isCancelled) {
                // Attempt to cancel if connection was in progress
                it.cancel(true)
            }
            MediaController.releaseFuture(it)
        }
        mediaController = null
        controllerFuture = null
    }

    /**
     * Prepares the player for playback with the given [mediaItem] and starts playing.
     *
     * If the provided [mediaItem] is the same as the current item and the player is already playing:
     * - If the playback has ended, it seeks to the beginning.
     * - Otherwise, it ensures playback is active and sets the state to [PlayerStateUi.Playing].
     *
     * If the [mediaItem] is different or the player is not playing the current item,
     * it sets the new media item, prepares the player, and starts playback.
     *
     * @param mediaItem The [DomainMediaItem] to prepare and play.
     */
    override fun prepareAndPlay(mediaItem: DomainMediaItem) {
        mediaController?.takeIf { it.currentMediaItem?.mediaId == mediaItem.id }?.let {
            if (it.isPlaying) {
                if (it.playbackState == Player.STATE_ENDED) {
                    it.seekTo(0)
                } else {
                    it.play()
                    _playerState.value = PlayerStateUi.Playing
                }
            }
        } ?: mediaController?.apply {
            val mediaMetadataBuilder = MediaMetadata.Builder()
            mediaMetadataBuilder.setTitle(mediaItem.title)
            mediaItem.artworkUri?.let {
                mediaMetadataBuilder.setArtworkUri(
                    "${kesiAndroidApiUrl}images/${it}".toUri()
                )
            }
            setMediaItem(
                MediaItem.Builder()
                    .setMediaId(mediaItem.id)
                    .setUri("${kesiAndroidApiUrl}audios/${mediaItem.fileName}")
                    .setMediaMetadata(mediaMetadataBuilder.build())
                    .build()
            )
            prepare()
            play()
        }
    }

    /**
     * Resumes playback if currently paused.
     * If the player is not prepared or already playing, this might have no effect
     * or could depend on the underlying MediaController's behavior.
     */
    override fun play() {
        mediaController?.play()
    }

    /**
     * Pauses the currently playing media.
     * If no media is playing or the controller is not initialized, this call has no effect.
     */
    override fun pause() {
        mediaController?.pause()
    }

    /**
     * Seeks to a specific position in the current media item.
     * The position is relative to the current position of the media player.
     * If the provided `positionMs` results in a negative value after adding to the current position,
     * it will be coerced to 0 (the beginning of the media).
     *
     * @param positionMs The time in milliseconds to seek by, relative to the current position.
     *                   A positive value seeks forward, a negative value seeks backward.
     */
    override fun seekTo(positionMs: Long) {
        val result = (mediaController?.currentPosition?.toFloat() ?: 0f) + positionMs.toFloat()
        mediaController?.seekTo(result.toLong().coerceAtLeast(0))
        _currentPositionMs.value = result / (mediaController?.duration?.toFloat() ?: 1f)
    }

    /**
     * Starts a coroutine that periodically updates the [_currentPositionMs] StateFlow
     * with the current playback progress of the media.
     * It cancels any existing progress update job before starting a new one.
     * The progress is calculated as a float value between 0.0 and 1.0.
     * Updates occur every 250 milliseconds while the media is playing.
     */
    private fun startProgressUpdates() {
        progressUpdateJob?.cancel()
        progressUpdateJob = coroutineScope.launch {
            while (true) {
                mediaController?.let {
                    if (it.isPlaying) {
                        val duration = if (it.duration < 0) 1L else it.duration
                        val progress = it.currentPosition.toFloat() / duration.toFloat()
                        _currentPositionMs.value = progress
                    }
                }
                delay(250L)
            }
        }
    }

    /**
     * Stops the periodic updates of the playback progress.
     * This is typically called when the player is paused, stopped, or the controller is released.
     * It cancels the ongoing coroutine job responsible for progress updates and nullifies the job reference.
     */
    private fun stopProgressUpdates() {
        progressUpdateJob?.cancel()
        progressUpdateJob = null
    }
}