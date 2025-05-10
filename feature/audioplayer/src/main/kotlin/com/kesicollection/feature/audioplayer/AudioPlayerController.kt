package com.kesicollection.feature.audioplayer

import android.content.ComponentName
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
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

data class DomainMediaItem(
    val id: String,                 // Your app's unique ID for this item
    val fileName: String,          // URI as a string (URL, content URI, etc.)
    val title: String? = null,
    val artist: String? = null,
    val albumTitle: String? = null,
    val artworkUri: String? = null,         // Artwork URI as a string
    val durationMs: Long? = null,   // Optional: if known beforehand
    val userExtras: Map<String, String>? = null // For any other custom data your app needs
)

/**
 * Represents the different UI-relevant states the player can be in.
 */
sealed class PlayerStateUi {
    data object Idle : PlayerStateUi()
    data object Buffering : PlayerStateUi()
    data object Ready : PlayerStateUi()
    data object Playing : PlayerStateUi()
    data object Paused : PlayerStateUi()
    data object Ended : PlayerStateUi()
    data class Error(val message: String) : PlayerStateUi()
}

interface AudioPlayerController {
    fun prepareAndPlay(mediaItem: DomainMediaItem)
    fun play()
    fun pause()
    fun seekTo(positionMs: Long)
    fun skipToNext()
    fun skipToPrevious()
    fun setShuffleModeEnabled(enabled: Boolean)
//    fun setRepeatMode(repeatMode: Int)

    // --- State Flows ---
    /** Emits the current high-level state of the player (e.g., Idle, Playing, Paused). */
    val playerState: StateFlow<PlayerStateUi>

    /** Emits the current playback position in milliseconds. */
    val currentPositionMs: StateFlow<Float>

    // --- Lifecycle ---
    fun initialize() // Could take SessionToken if built on demand
    fun release()
}

@Singleton
class Media3AudioPlayerController @Inject constructor(
    @AudioPlayerDefaultDispatcher dispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context,
    @KesiAndroidApiUrl private val kesiAndroidApiUrl: String,
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
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                _playerState.value = PlayerStateUi.Error("Connection interrupted.")
            } catch (e: Exception) {
                _playerState.value =
                    PlayerStateUi.Error("An unexpected error occurred during connection: ${e.message}")
            }
        }, ContextCompat.getMainExecutor(context))
    }

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

    override fun prepareAndPlay(mediaItem: DomainMediaItem) {
        if (mediaController?.playbackState != Player.EVENT_IS_LOADING_CHANGED
            || mediaController?.currentMediaItem?.mediaId != mediaItem.id) {
            mediaController?.apply {
                setMediaItem(
                    MediaItem.Builder()
                        .setMediaId(mediaItem.id)
                        .setUri("${kesiAndroidApiUrl}podcasts/${mediaItem.fileName}")
                        .build()
                )
                prepare()
                play()
            }
        }
    }

    override fun play() {
        mediaController?.play()
    }

    override fun pause() {
        mediaController?.pause()
    }

    override fun seekTo(positionMs: Long) {
        val result = (mediaController?.currentPosition?.toFloat() ?: 0f) + positionMs.toFloat()
        mediaController?.seekTo(result.toLong().coerceAtLeast(0))
        _currentPositionMs.value = result / (mediaController?.duration?.toFloat() ?: 1f)
    }

    override fun skipToNext() {
        mediaController?.seekToNextMediaItem()
    }

    override fun skipToPrevious() {
        mediaController?.seekToPreviousMediaItem()
    }

    override fun setShuffleModeEnabled(enabled: Boolean) {
        mediaController?.shuffleModeEnabled = enabled
    }

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

    private fun stopProgressUpdates() {
        progressUpdateJob?.cancel()
        progressUpdateJob = null
    }
}