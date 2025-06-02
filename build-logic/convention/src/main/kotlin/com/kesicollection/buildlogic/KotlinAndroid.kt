/**
 * This file contains Kotlin build logic configurations for Android and JVM projects.
 * It defines constants, extension functions, and configurations for setting up Kotlin projects
 * with specific SDK versions, Java compatibility, and compiler options.
 */

@file:Suppress("UnstableApiUsage")

package com.kesicollection.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

/**
 * The target SDK version for Android projects.
 */
const val TARGET_SDK = 35

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    // Apply common configurations to the Android project.
    commonExtension.apply {
        compileSdk = TARGET_SDK // Set the compile SDK version.

        defaultConfig {
            minSdk = 23 // Set the minimum SDK version.
        }

        // Configure Java compatibility options.
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11 // Set source compatibility to Java 11.
            targetCompatibility = JavaVersion.VERSION_11 // Set target compatibility to Java 11.
        }
    }

    configureKotlin<KotlinAndroidProjectExtension>()
}

/**
 * Configure base Kotlin options for JVM (non-Android)
 */
internal fun Project.configureKotlinJvm() {
    // Configure Java compatibility options for JVM projects.
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_11 // Set source compatibility to Java 11.
        targetCompatibility = JavaVersion.VERSION_11 // Set target compatibility to Java 11.
    }

    // Configure Kotlin options for JVM projects.
    configureKotlin<KotlinJvmProjectExtension>()
}

/**
 * Configure base Kotlin options
 * @param T The type of Kotlin extension to configure.
 */
private inline fun <reified T : KotlinBaseExtension> Project.configureKotlin() = configure<T> {
    // Determine if Kotlin warnings should be treated as errors.
    // Treat all Kotlin warnings as errors (disabled by default)
    // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
    val warningsAsErrors: String? by project
    when (this) {
        is KotlinAndroidProjectExtension -> compilerOptions
        is KotlinJvmProjectExtension -> compilerOptions
        else -> TODO("Unsupported project extension $this ${T::class}")
    }.apply { // Apply compiler options.
        jvmTarget.set(JvmTarget.JVM_11) // Set the JVM target to Java 11.
        allWarningsAsErrors.set(warningsAsErrors.toBoolean()) // Set whether warnings should be treated as errors.
        // Add free compiler arguments.
        freeCompilerArgs.add(
            // Enable experimental coroutines APIs, including Flow
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        )
        freeCompilerArgs.add(
            /**
             * Remove this args after Phase 3.
             * https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-consistent-copy-visibility/#deprecation-timeline
             *
             * Deprecation timeline
             * Phase 3. (Supposedly Kotlin 2.2 or Kotlin 2.3).
             * The default changes.
             * Unless ExposedCopyVisibility is used, the generated 'copy' method has the same visibility as the primary constructor.
             * The binary signature changes. The error on the declaration is no longer reported.
             * '-Xconsistent-data-class-copy-visibility' compiler flag and ConsistentCopyVisibility annotation are now unnecessary.
             */
            "-Xconsistent-data-class-copy-visibility"
        )
    }
}
