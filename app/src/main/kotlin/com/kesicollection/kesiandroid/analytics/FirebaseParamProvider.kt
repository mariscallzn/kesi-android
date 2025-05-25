package com.kesicollection.kesiandroid.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.kesicollection.core.app.AnalyticsWrapper

/**
 * `FirebaseParamProvider` provides a set of constants representing Firebase Analytics parameter keys.
 *
 * This object implements the `AnalyticsWrapper.Param` interface, making it compatible with systems
 * that rely on a generic parameter key provider. It specifically exposes parameter names used by
 * Firebase Analytics for tracking screen views and classes.
 *
 * Using this object helps maintain consistency and reduces the chance of typos when working with
 * Firebase Analytics parameters across your application.
 */
object FirebaseParamProvider : AnalyticsWrapper.Param {
    override val screenName: String
        get() = FirebaseAnalytics.Param.SCREEN_NAME
    override val screenClass: String
        get() = FirebaseAnalytics.Param.SCREEN_CLASS
    override val itemId: String
        get() = FirebaseAnalytics.Param.ITEM_ID
    override val itemName: String
        get() = FirebaseAnalytics.Param.ITEM_NAME
    override val contentType: String
        get() = FirebaseAnalytics.Param.CONTENT_TYPE
    override val contentEmphasis: String
        get() = "content_emphasis"
}