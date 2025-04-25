package com.kesicollection.core.uisystem

import androidx.compose.runtime.staticCompositionLocalOf
import com.kesicollection.core.analytics.AnalyticsWrapper

val LocalAnalytics = staticCompositionLocalOf<AnalyticsWrapper> {
    error("No AnalyticsWrapper provided")
}