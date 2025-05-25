package com.kesicollection.feature.audioplayer.player

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
