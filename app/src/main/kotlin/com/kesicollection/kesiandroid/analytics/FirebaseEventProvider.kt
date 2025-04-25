package com.kesicollection.kesiandroid.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.kesicollection.core.analytics.AnalyticsWrapper

object FirebaseEventProvider: AnalyticsWrapper.Event {
    override val screenView: String
        get() = FirebaseAnalytics.Event.SCREEN_VIEW
}