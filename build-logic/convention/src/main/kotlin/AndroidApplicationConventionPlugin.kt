import com.android.build.api.dsl.ApplicationExtension
import com.kesicollection.buildlogic.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

/**
 * A convention plugin that applies common configurations for Android Application projects.
 *
 * This plugin applies the following plugins:
 * - `com.android.application`: Enables Android application capabilities.
 * - `org.jetbrains.kotlin.android`: Enables Kotlin Android support.
 *
 * It also configures the Android Application extension with common settings using [configureKotlinAndroid]
 * and sets the `targetSdk` to 35.
 *
 * Usage:
 * Apply this plugin to your Android application module's `build.gradle.kts` file:
 *
 * ```kotlin
 * plugins {
 *      id("your.plugin.id.android.application") // Replace with your plugin ID
 * }
 * ```
 *
 */
class AndroidApplicationConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            apply(plugin = "org.jetbrains.kotlin.android")

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 35
            }
        }
    }
}