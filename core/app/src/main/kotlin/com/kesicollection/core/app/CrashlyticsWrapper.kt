package com.kesicollection.core.app

import java.lang.Exception

interface CrashlyticsWrapper {

    val params: Params
    fun recordException(exception: Exception, params: Map<String, String>? = null)

    interface Params {
        val screenName: String
        val className: String
        val action: String
    }
}