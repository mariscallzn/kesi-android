package com.kesicollection.core.uisystem

import androidx.compose.runtime.staticCompositionLocalOf
import com.kesicollection.core.app.AnalyticsWrapper
import com.kesicollection.core.app.AppManager

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