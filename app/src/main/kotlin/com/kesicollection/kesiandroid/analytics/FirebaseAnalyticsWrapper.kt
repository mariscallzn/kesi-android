package com.kesicollection.kesiandroid.analytics

import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.kesicollection.core.app.AnalyticsWrapper
import com.kesicollection.kesiandroid.BuildConfig
import javax.inject.Inject

/**
 * A wrapper class for Firebase Analytics, providing a simplified interface for logging events.
 *
 * This class encapsulates the `FirebaseAnalytics` instance and handles the process of
 * converting parameter types to those supported by Firebase. It ensures type safety
 * and throws an exception if an unsupported type is encountered.
 *
 * @property firebaseAnalytics The underlying FirebaseAnalytics instance.
 */
class FirebaseAnalyticsWrapper @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val eventProvider: AnalyticsWrapper.Event,
    private val paramProvider: AnalyticsWrapper.Param,
) : AnalyticsWrapper {

    override val event: AnalyticsWrapper.Event
        get() = eventProvider
    override val param: AnalyticsWrapper.Param
        get() = paramProvider

    override fun logEvent(eventName: String, params: Map<String, Any>?) {
        if (BuildConfig.DEBUG) {
            Log.d("FirebaseAnalyticsWrapper", "logEvent: eventName=$eventName params=$params")
        } else {
            firebaseAnalytics.logEvent(eventName) {
                params?.forEach { entry ->
                    when (entry.value) {
                        is String -> param(entry.key, entry.value as String)
                        is Double -> param(entry.key, entry.value as Double)
                        is Long -> param(entry.key, entry.value as Long)
                        else -> throw RuntimeException("Type not implemented")
                    }
                }
            }
        }
    }
}