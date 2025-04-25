package com.kesicollection.kesiandroid.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.kesicollection.core.analytics.AnalyticsWrapper

object FirebaseParamProvider : AnalyticsWrapper.Param {
    override val screenName: String
        get() = FirebaseAnalytics.Param.SCREEN_NAME
    override val screenClass: String
        get() = FirebaseAnalytics.Param.SCREEN_CLASS
}