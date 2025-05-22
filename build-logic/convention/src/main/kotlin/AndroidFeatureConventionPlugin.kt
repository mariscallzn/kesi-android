import com.android.build.gradle.LibraryExtension
import com.kesicollection.buildlogic.debugImplementation
import com.kesicollection.buildlogic.implementation
import com.kesicollection.buildlogic.libs
import com.kesicollection.buildlogic.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * A convention plugin that applies common configurations for Android Feature modules.
 *
 * This plugin applies the following plugins:
 * - `kesiandroid.android.library`: Applies common Android library configurations
 * - `kesiandroid.hilt`: Applies Hilt dependency injection configurations
 *
 * It also adds common dependencies required for feature modules, including:
 * - `androidx.hilt.navigation.compose`: Hilt integration for Jetpack Navigation Compose.
 * - `androidx.lifecycle.runtimeCompose`: Lifecycle runtime support for Compose.
 * - `androidx.lifecycle.viewModelCompose`: ViewModel support for Compose.
 * - `androidx.navigation.compose`: Jetpack Navigation Compose.
 *
 * It comments out the implementation of `:core:uisystem` and `:data:repository` because
 * this plugin should be generic, and the features may not always require those.
 *
 * Usage:
 * Apply this plugin to your Android feature module's `build.gradle.kts` file:
 *
 * ```kotlin
 * plugins {
 *      id("your.plugin.id.android.feature") // Replace with your plugin ID
 * }
 * ```
 *
 */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "kesiandroid.android.library")
            apply(plugin = "kesiandroid.hilt")

            extensions.configure<LibraryExtension> {
                testOptions {
                    unitTests {
                        isIncludeAndroidResources = true
                    }
                }
            }

            dependencies {
                implementation(project(":core:uisystem"))
                implementation(project(":data:usecase"))
                implementation(libs.findLibrary("androidx.hilt.navigation.compose").get())
                implementation(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                implementation(libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
                implementation(libs.findLibrary("androidx.navigation.compose").get())
                implementation(libs.findLibrary("kotlinx.collections.immutable").get())

                testImplementation(project(":testing"))
                testImplementation(libs.findLibrary("robolectric").get())
                debugImplementation(libs.findBundle("androidx.compose.ui.test").get())

            }
        }
    }
}