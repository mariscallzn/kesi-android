package com.kesicollection.kesiandroid.crashlytics

import com.kesicollection.core.app.CrashlyticsWrapper

object FirebaseCrashlyticsProvider : CrashlyticsWrapper.Params {
    override val screenName: String
        get() = "screen_name"
    override val className: String
        get() = "class_name"
    override val action: String
        get() = "action"
}