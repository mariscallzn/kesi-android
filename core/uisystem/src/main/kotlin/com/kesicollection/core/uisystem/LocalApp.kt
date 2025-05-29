package com.kesicollection.core.uisystem

import androidx.compose.runtime.staticCompositionLocalOf
import com.kesicollection.core.app.AnalyticsWrapper
import com.kesicollection.core.app.AppManager
import com.kesicollection.core.app.CrashlyticsWrapper
import com.kesicollection.core.app.Logger
import com.kesicollection.core.app.PreviewAnalyticsWrapper
import com.kesicollection.core.app.PreviewCrashlyticsWrapper
import com.kesicollection.core.app.PreviewLogger

/**
 * Provides access to the [AppManager] instance within the Composition.
 *
 * This composition local allows components deeper in the UI tree to access and interact with
 * the application's manager without needing to pass it down through multiple layers of parameters.
 *
 * The [AppManager] typically handles global application-level concerns like navigation, data
 * fetching, state management, or any other functionality that needs to be accessible from
 * multiple parts of the application.
 *
 * **Usage:**
 *
 * 1. **Providing:**
 *    At the root of your composable tree (or at a suitable parent), use [CompositionLocalProvider]
 *    to provide an instance of the [AppManager]:
 *    ```kotlin
 *    CompositionLocalProvider(LocalApp provides appManager) {
 *        MyAppContent()
 *    }
 *    ```
 *
 * 2. **Consuming:**
 *    Within any composable that is a child of the provider, use `LocalApp.current` to retrieve
 *    the provided [AppManager]:
 *    ```kotlin
 *    @Composable
 *    fun MyComponent() {
 *        val appManager = LocalApp.current
 *        // ... use appManager to interact with the application ...
 *        appManager.logger.log("TAG", "Message")
 *    }
 *    ```
 *
 * **Error Handling:**
 * If you attempt to access `LocalApp.current` without providing a value using
 * `CompositionLocalProvider`, an error will be thrown with the message "No AppManager provided".
 *
 * **Best Practices:**
 * * Keep the scope of the [CompositionLocalProvider] as limited as possible to avoid unintended
 *   side effects or conflicts.
 * * Ensure that the provided [AppManager] instance is properly initialized before providing it.
 * * Consider using a dependency injection framework (like Hilt) to manage and provide the
 * [AppManager] instance.
 */
val LocalApp = staticCompositionLocalOf<AppManager> {
    error("No AppManager provided")
}

/**
 * A preview implementation of [AppManager] used for Composable previews.
 *
 * This object provides stubbed or no-op implementations for the core app services
 * ([Logger], [AnalyticsWrapper], [CrashlyticsWrapper]). This is useful for ensuring
 * that Composable previews can render without relying on fully initialized or functional
 * app services, which might not be available or desirable in a preview environment.
 *
 * When using `LocalApp` in your Composables, you can provide this `PreviewAppManager`
 * during preview generation to ensure your previews work correctly:
 *
 * ```kotlin
 * @Preview
 * @Composable
 * fun MyScreenPreview() {
 *     CompositionLocalProvider(LocalApp provides PreviewAppManager) {
 *         MyScreen()
 *     }
 * }
 * ```
 *
 * This allows `MyScreen` to access `LocalApp.current` (which will be `PreviewAppManager`)
 * and use its `logger`, `analytics`, and `crashlytics` properties without causing issues
 * in the preview environment.
 */
object PreviewAppManager : AppManager {
    override val logger: Logger
        get() = PreviewLogger
    override val analytics: AnalyticsWrapper
        get() = PreviewAnalyticsWrapper
    override val crashlytics: CrashlyticsWrapper
        get() = PreviewCrashlyticsWrapper

}