package com.kesicollection.kesiandroid

import com.kesicollection.core.app.AnalyticsWrapper
import com.kesicollection.core.app.AppManager
import com.kesicollection.core.app.Logger
import javax.inject.Inject

/**
 * [KesiAndroidAppManager] is a concrete implementation of the [AppManager] interface
 * tailored for Android applications. It provides access to core application services
 * such as logging and analytics.
 *
 * This class is designed to be injected using a dependency injection framework like Dagger.
 *
 * @property appLogger The [Logger] instance used for application-wide logging.
 * @property analyticsWrapper The [AnalyticsWrapper] instance used for tracking events and user behavior.
 */
class KesiAndroidAppManager @Inject constructor(
    private val appLogger: Logger,
    private val analyticsWrapper: AnalyticsWrapper,
) : AppManager {
    override val logger: Logger
        get() = appLogger
    override val analytics: AnalyticsWrapper
        get() = analyticsWrapper
}