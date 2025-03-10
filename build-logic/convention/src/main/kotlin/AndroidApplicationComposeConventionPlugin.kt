import com.android.build.api.dsl.ApplicationExtension
import com.kesicollection.buildlogic.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

/**
 * A convention plugin that applies common configurations for Android Application projects using Compose.
 *
 * This plugin applies the following plugins:
 * - `com.android.application`: Enables Android application capabilities.
 * - `org.jetbrains.kotlin.plugin.compose`: Enables Kotlin Compose compiler plugin.
 *
 * It also configures the Android Application extension with common Compose settings using [configureAndroidCompose].
 *
 * Usage:
 * Apply this plugin to your Android application module's `build.gradle.kts` file:
 *
 * ```kotlin
 * plugins {
 *      id("your.plugin.id.android.application.compose") // Replace with your plugin ID
 * }
 * ```
 *
 */
class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")

            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
        }
    }
}