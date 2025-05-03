package com.kesicollection.kesiandroid.crashlytics

import android.util.Log
import com.google.firebase.crashlytics.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.recordException
import com.kesicollection.core.app.CrashlyticsWrapper
import javax.inject.Inject

class FirebaseCrashlyticsWrapper @Inject constructor(
    private val crashlytics: FirebaseCrashlytics,
    private val crashlyticsParams: CrashlyticsWrapper.Params
) : CrashlyticsWrapper {

    override val params: CrashlyticsWrapper.Params
        get() = crashlyticsParams

    override fun recordException(exception: Exception, params: Map<String, String>?) {
        if (BuildConfig.DEBUG) {
            Log.e("FirebaseCrashlyticsWrapper", "recordException: ${exception.message}", exception)
        } else {
            crashlytics.recordException(exception) {
                params?.let {
                    it.forEach { entry -> key(entry.key, entry.value) }
                }
            }
        }
    }
}