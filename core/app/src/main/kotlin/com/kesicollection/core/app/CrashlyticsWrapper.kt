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

object PreviewCrashlyticsWrapper : CrashlyticsWrapper {
    override val params: CrashlyticsWrapper.Params
        get() = object : CrashlyticsWrapper.Params {
            override val screenName: String
                get() = ""
            override val className: String
                get() = ""
            override val action: String
                get() = ""
        }

    override fun recordException(
        exception: Exception,
        params: Map<String, String>?
    ) {

    }
}