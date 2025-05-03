package com.kesicollection.kesiandroid.crashlytics

import com.kesicollection.core.app.CrashlyticsWrapper

object FirebaseCrashlyticsProvider : CrashlyticsWrapper.Params {
    override val screenName: String
        get() = "screen_name"
}