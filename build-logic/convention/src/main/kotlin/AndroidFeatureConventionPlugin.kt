import com.android.build.gradle.LibraryExtension
import com.kesicollection.buildlogic.androidTestImplementation
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
 * This plugin is applied to all Android feature modules.
 * It applies the following plugins:
 * - `kesiandroid.android.library`
 * - `kesiandroid.hilt`
 *
 * It also adds the following dependencies:
 * - `:core:uisystem`
 * - `:domain`
 * - `androidx.hilt.navigation.compose`
 * - `androidx.lifecycle.runtimeCompose`
 * - `androidx.lifecycle.viewModelCompose`
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
                implementation(project(":domain"))
                implementation(libs.findLibrary("androidx.hilt.navigation.compose").get())
                implementation(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                implementation(libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
                implementation(libs.findLibrary("androidx.navigation.compose").get())
                implementation(libs.findLibrary("kotlinx.collections.immutable").get())

                testImplementation(project(":test:core"))
                androidTestImplementation(project(":test:instrumentation"))
                testImplementation(libs.findLibrary("robolectric").get())
                debugImplementation(libs.findBundle("androidx.compose.ui.test").get())

            }
        }
    }
}