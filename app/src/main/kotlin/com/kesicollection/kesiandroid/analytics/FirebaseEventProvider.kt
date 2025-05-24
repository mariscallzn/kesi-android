package com.kesicollection.kesiandroid.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.kesicollection.core.app.AnalyticsWrapper

/**
 * `FirebaseEventProvider` provides a set of event names used for logging events
 * to Firebase Analytics. It implements the `AnalyticsWrapper.Event` interface,
 * allowing it to be used consistently with other analytics providers.
 *
 * This object acts as a centralized place to define and access the string
 * representations of Firebase Analytics events, ensuring consistency and
 * reducing the risk of typos throughout the codebase.
 */
object FirebaseEventProvider: AnalyticsWrapper.Event {
    override val screenView: String
        get() = FirebaseAnalytics.Event.SCREEN_VIEW
    override val selectItem: String
        get() = FirebaseAnalytics.Event.SELECT_ITEM
    override val tryAgain: String
        get() = "try_again"
    override val playAudioPlayer: String
        get() = "play_audio_player"
    override val reply10AudioPlayer: String
        get() = "reply_10_audio_player"
    override val forward10AudioPlayer: String
        get() = "forward_10_audio_player"
    override val pauseAudioPlayer: String
        get() = "pause_audio_player"
    override val adImpression: String
        get() = FirebaseAnalytics.Event.AD_IMPRESSION
}