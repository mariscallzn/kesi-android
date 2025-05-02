package com.kesicollection.kesiandroid.logger

import android.util.Log
import com.kesicollection.core.app.Logger

object AndroidLogger : Logger {
    override fun log(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun error(tag: String, message: String, exception: Exception?) {
        Log.e(tag, message, exception)
    }
}