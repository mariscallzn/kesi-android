package com.kesicollection.testing.api

import com.kesicollection.core.app.CrashlyticsWrapper
import java.lang.Exception

class TestDoubleCrashlyticsWrapper: CrashlyticsWrapper {
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