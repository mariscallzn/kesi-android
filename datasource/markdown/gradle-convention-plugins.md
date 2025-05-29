# Stop Drowning in Gradle! How Kesi Android's Convention Plugins Will Save Your Android Project

Ah, Gradle. The powerful, flexible, and sometimes overwhelmingly complex build system that powers our Android projects. If you've ever found yourself endlessly copying and pasting configurations across `build.gradle.kts` files, battling inconsistencies between modules, or dreading the thought of updating a common dependency everywhere, then this post is for you. This was certainly our reality at Kesi Android before we embraced a more structured approach.

It's time to share how we transformed our Gradle setup from a potential tangled mess into a streamlined masterpiece using **Convention Plugins**.

Ready to see how you can achieve this too? Let's dive into our journey and the specifics!

## What Exactly ARE Convention Plugins?

Think of convention plugins as **reusable, opinionated blueprints for your Gradle build configurations**. Instead of scattering `compileSdk`, `minSdk`, Kotlin options, Hilt setup, and common dependencies across every module's `build.gradle.kts` file, you define these "conventions" once in centralized plugins. Then, you simply apply these plugins to your modules.

For example, in our project, Kesi Android, we have:

- `kesiandroid.android.library`: A plugin that sets up everything needed for a typical Android library module.

- `kesiandroid.hilt`: A dedicated plugin to configure Hilt for dependency injection.

- `kesiandroid.android.feature`: A comprehensive plugin for our feature modules, which itself uses the library and Hilt plugins.

- `kesiandroid.jvm.library`: For our pure Kotlin/JVM modules.


These plugins live within our project in a dedicated `build-logic` directory (using an included build), making them version-controlled and easily shareable across the team.

## Why We Switched: The Massive Benefits of Convention Plugins

The move to convention plugins wasn't just for kicks; it brought tangible benefits that significantly improved our development workflow:

- **DRY (Don't Repeat Yourself):** This is the big one. Define configurations once, use them everywhere. Updating our `targetSdk`? Change it in `kesiandroid.android.library`, and all relevant modules are updated.

- **Consistency is King:** All our modules now adhere to the same standards – same Kotlin version, Android Gradle Plugin (AGP) version, lint checks, Hilt setup, etc. No more "it works on my machine" due to a rogue module configuration.

- **Simplified Module `build.gradle.kts` Files:** Our feature and library build scripts became incredibly lean. They declare _what kind_ of module they are (e.g., `alias(libs.plugins.kesiandroid.android.feature)`) rather than detailing _how_ to configure every little thing.

- **Improved Maintainability:** Refactoring build logic or updating dependencies is now a breeze. Changes are localized in `build-logic`, reducing the risk of errors across dozens of modules.

- **Faster Onboarding:** New team members can get up to speed quicker as the project's build structure is standardized and easier to understand. The convention is clear.

- **Scalability for Growth:** As Kesi Android grows and we add more modules, convention plugins make it manageable. Adding a new feature module and having its entire build configuration, including Hilt and Compose, set up by applying one plugin is a huge win.

- **Better Build Performance (Potentially):** While not the primary goal, well-structured build logic in plugins can lead to more optimized configuration phases as Gradle can better understand and cache this logic.


## Getting Started: Our Approach to `build-logic`

We use an **included build** for our convention plugins, housed in a directory named `build-logic/convention` (you can name the subdirectory as you like, or just use `build-logic`).

**1. Project Structure Setup:**

```
kesi-collection-android/
├── app/
├── build-logic/
│   └── convention/ # Our convention plugins live here
│       ├── build.gradle.kts  // Meta build script for the 'convention' plugins module
│       └── src/
│           └── main/
│               └── kotlin/   // Plugin source code (e.g., AndroidFeatureConventionPlugin.kt)
├── feature/
│   └── newfeature/
│       └── build.gradle.kts // Example feature module
├── gradle/
│   └── libs.versions.toml   // Our beloved version catalog
├── settings.gradle.kts
└── build.gradle.kts (project root)
```

**2. Include `build-logic` in `settings.gradle.kts`:**

```
// settings.gradle.kts (project root)

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Kesi AndroidApp"
include(":app")
// Include all modules in 'feature' directory, for example
// includeDir("feature") // Custom helper function to include all subdirectories

// Include the build-logic:convention directory as an included build
includeBuild("build-logic/convention") {
    dependencySubstitution {
        // If your convention plugins have a group and version, substitute them
        // substitute(module("com.Kesi Android.buildlogic:convention")).using(project(":"))
    }
}
```

**3. Configure `build-logic/convention/build.gradle.kts`:**

This file is where we register our custom convention plugins and declare dependencies that the plugins themselves need (like the Android Gradle Plugin). (See specific example in the Deep Dive section below).

## Deep Dive: Kesi Android's `AndroidFeatureConventionPlugin`

To truly grasp the power and elegance of convention plugins, let's walk through a real-world example from our project at Kesi Android: the `AndroidFeatureConventionPlugin`. This plugin is pivotal in how we standardize our Android feature modules.

**The Goal:** Ensure every new feature module in our app is consistently configured with all necessary Android settings, Hilt for dependency injection, common UI and navigation dependencies, and our testing infrastructure, all without duplicating build logic.

**Plugin ID:** `kesiandroid.android.feature`

We manage our plugin IDs (and versions, though "unspecified" is common for local convention plugins) centrally in our `gradle/libs.versions.toml` file:

```
# gradle/libs.versions.toml

[versions]
# ... other versions ...

[libraries]
# ... androidx.hilt.navigation.compose, androidx.lifecycle.runtimeCompose, etc. ...
# robolectric = "..."
# kotlinx-collections-immutable = "..."

[plugins]
# Our convention plugin IDs
kesiandroid-android-feature = { id = "kesiandroid.android.feature", version = "unspecified" }
kesiandroid-android-library = { id = "kesiandroid.android.library", version = "unspecified" } # Base Android library conventions
kesiandroid-hilt = { id = "kesiandroid.hilt", version = "unspecified" } # Hilt setup convention

# External plugins (examples)
android-gradle = { id = "com.android.tools.build:gradle", version.ref = "agp" } # Assuming 'agp' version is defined
kotlin-gradle = { id = "org.jetbrains.kotlin:kotlin-gradle-plugin", version.ref = "kotlin" } # Assuming 'kotlin' version
```

**Core Responsibilities of `AndroidFeatureConventionPlugin`:**

1. **Applies Base Conventions (Plugin Composition):** It doesn't reinvent the wheel. Instead, it applies other, more granular convention plugins we've created:

    - `kesiandroid.android.library` (via `libs.plugins.kesiandroid.android.library`): This plugin handles the fundamental setup for any Android library module in our project (e.g., `compileSdk`, `minSdk`, `targetSdk`, Kotlin Android options, basic build type configurations).

    - `kesiandroid.hilt` (via `libs.plugins.kesiandroid.hilt`): This plugin takes care of setting up Hilt, including applying the Hilt Gradle plugin and adding core Hilt dependencies.

2. **Configures Feature-Specific Android Settings:** For instance, we ensure unit tests in feature modules can access Android resources:

    ```
    extensions.configure<LibraryExtension> {
        testOptions {
            unitTests {
                isIncludeAndroidResources = true
            }
        }
    }
    ```

3. **Adds Standard Feature Module Dependencies:** It automatically includes a suite of dependencies typically needed by our feature modules, referencing our version catalog (`libs`) for library versions and other project modules:

    - Project modules: `project(":core:uisystem")` (our shared UI components) and `project(":domain")` (core domain logic).

    - Jetpack Compose related libraries: `androidx.hilt.navigation.compose`, `androidx.lifecycle.runtimeCompose`, `androidx.lifecycle.viewModelCompose`, `androidx.navigation.compose`.

    - Immutable Collections: `kotlinx.collections.immutable`.

    - Testing: `project(":testing")` (our custom test utilities/runners) and `robolectric`.

    - Compose UI Testing: The `androidx.compose.ui.test` bundle for debug builds.


**The Plugin Code (`build-logic/convention/src/main/kotlin/AndroidFeatureConventionPlugin.kt`):** _(Ensure your `implementationClass` path in `build.gradle.kts` matches any package declaration you use here, e.g., `com.Kesi Android.buildlogic.convention.AndroidFeatureConventionPlugin`)_

```
// If you have a package, declare it here: e.g., package com.Kesi Android.buildlogic.convention
import com.android.build.gradle.LibraryExtension
// import com.Kesi Android.buildlogic.libs // This might be how you access it if generated for build-logic
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val libs = target.extensions.getByType<VersionCatalogsExtension>().named("libs")

        with(target) {
            pluginManager.apply(libs.findPlugin("kesiandroid-android-library").get().get().pluginId)
            pluginManager.apply(libs.findPlugin("kesiandroid-hilt").get().get().pluginId)

            extensions.configure<LibraryExtension> {
                testOptions {
                    unitTests {
                        isIncludeAndroidResources = true
                    }
                }
                // Potentially other Android configurations like Compose options if not in android.library
                // buildFeatures {
                //     compose = true
                // }
                // composeOptions {
                //     kotlinCompilerExtensionVersion = libs.findVersion("composeCompiler").get().toString()
                // }
            }

            dependencies {
                add("implementation", project(":core:uisystem"))
                add("implementation", project(":domain"))

                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
                add("implementation", libs.findLibrary("androidx.navigation.compose").get())
                add("implementation", libs.findLibrary("kotlinx.collections.immutable").get())

                add("testImplementation", project(":testing"))
                add("testImplementation", libs.findLibrary("robolectric").get())
                add("debugImplementation", libs.findBundle("androidx.compose.ui.test").get())
            }
        }
    }
}
```

**Registering the Plugin (`build-logic/convention/build.gradle.kts`):**

This is where we link the plugin ID (from `libs.versions.toml`) to its implementation class.

```
// build-logic/convention/build.gradle.kts

plugins {
    `kotlin-dsl`
}

// Make version catalog accessible for plugin registration
val libs = extensions.getByType<org.gradle.api.artifacts.VersionCatalogsExtension>().named("libs")

gradlePlugin {
    plugins {
        register("androidFeature") {
            id = libs.findPlugin("kesiandroid-android-feature").get().get().pluginId
            implementationClass = "AndroidFeatureConventionPlugin" // Adjust if you have a package
        }
        register("androidLibrary") {
            id = libs.findPlugin("kesiandroid-android-library").get().get().pluginId
            implementationClass = "AndroidLibraryConventionPlugin" // Replace with actual class name
        }
        register("androidHilt") {
            id = libs.findPlugin("kesiandroid-hilt").get().get().pluginId
            implementationClass = "AndroidHiltConventionPlugin" // Replace with actual class name
        }
        // You might also register your JvmLibraryConventionPlugin here
        // register("jvmLibrary") {
        //    id = "com.Kesi Android.jvm.library" // Or from libs
        //    implementationClass = "com.Kesi Android.buildlogic.JvmLibraryConventionPlugin"
        // }
    }
}

// Dependencies that the convention plugins themselves need.
dependencies {
    compileOnly(libs.findPlugin("android-gradle").get()) // For Android Gradle Plugin APIs
    compileOnly(libs.findPlugin("kotlin-gradle").get())   // For Kotlin Gradle Plugin APIs
    // You might need to add implementation dependencies if your plugins use libraries directly,
    // e.g., for Hilt Dagger plugin if you interact with its specifics beyond just applying it.
    // compileOnly(libs.findPlugin("hilt-gradle-plugin-entry-from-toml").get())
}
```

**Applying the Plugin to an Android Feature Module (`feature/newfeature/build.gradle.kts`):**

```
plugins {
    // Apply our custom Android Feature convention plugin using the type-safe alias from libs
    alias(libs.plugins.kesiandroid.android.feature)
}

// Module-specific Android configurations (if any)
android {
    namespace = "com.Kesi Android.newfeature"
}

// Module-specific dependencies
dependencies {
    // e.g., implementation(libs.findLibrary("some.feature.specific.library").get())
}
```

This `AndroidFeatureConventionPlugin` demonstrates a robust, maintainable, and scalable approach to managing build configurations. By centralizing logic and composing plugins, we at Kesi Android significantly reduce boilerplate, enforce consistency, and make onboarding new features (and developers!) much smoother.

## Other Convention Plugins We Use

While the `AndroidFeatureConventionPlugin` is a great example, we also use others like:

- **`kesiandroid.android.application`:** For our main `:app` module, handling application-specifics like `applicationId`, signing configs (though secrets are handled separately), etc.

- **`kesiandroid.quality`:** Applies JaCoCo for code coverage, and Detekt/Spotless for static analysis and formatting, ensuring code quality across all modules it's applied to.

- **`kesiandroid.jvm.library`:** As mentioned earlier, for pure Kotlin/JVM modules, setting up Kotlin serialization, coroutines, and common JVM testing libraries.


## Pro-Tips for Awesome Convention Plugins (Our Learnings)

- **Version Catalogs are Your Best Friend (`libs.versions.toml`):** Define ALL your dependency versions, plugin versions, AND your convention plugin IDs here. This is crucial for consistency and easy updates.

- **Compose, Don't Bloat:** Keep plugins focused. Our `AndroidFeatureConventionPlugin` _applies_ `AndroidLibraryConventionPlugin` and `AndroidHiltConventionPlugin`. This is better than one massive plugin trying to do everything.

- **Parameterize Carefully:** Avoid making your convention plugins overly configurable. The goal is "convention over configuration." If you find yourself adding many parameters, perhaps a new, more specific plugin is needed.

- **Clear Naming and Structure:** Use a clear naming scheme for your plugin IDs (e.g., `kesiandroid.android.*`, `kesiandroid.jvm.*`) and organize your plugin code logically within `build-logic`.

- **Document within `build-logic`:** A simple `README.md` in your `build-logic/convention` directory or KDoc comments in your plugin classes explaining what each plugin does and how/when to use it is invaluable.

- **Test Your Build Logic (Advanced):** For very complex logic, consider writing tests for your convention plugins, though this adds another layer of complexity.


## The "Before & After" - A Glimpse of the Magic

**Before (A typical feature module `build.gradle.kts` at Kesi Android):**

```
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    // Potentially compose, jacoco, etc.
}

android {
    namespace = "com.Kesi Android.somefeature"
    compileSdk = 34 // Or some hardcoded value
    defaultConfig {
        minSdk = 26 // Another hardcoded value
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release { /* ... proguard ... */ }
    }
    compileOptions { /* ... Java versions ... */ }
    kotlinOptions { /* ... JVM target ... */ }
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.5.3" } // Yet another version
    testOptions { unitTests { isIncludeAndroidResources = true } }
}

dependencies {
    implementation(project(":core:uisystem"))
    implementation(project(":domain"))
    implementation("com.google.dagger:hilt-android:2.48") // Version direct here
    kapt("com.google.dagger:hilt-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    // ... many more common dependencies for lifecycle, compose, navigation ...
    // ... testing dependencies ...
}
```

**After (Using `kesiandroid.android.feature`):**

```
plugins {
    alias(libs.plugins.kesiandroid.android.feature)
}

android {
    namespace = "com.Kesi Android.somefeature"
    // Only truly unique Android settings for this specific module, if any.
}

dependencies {
    // Only dependencies truly unique to "somefeature".
    // e.g., implementation(libs.findLibrary("some.very.specific.library.for.this.feature").get())
}
```

The reduction in boilerplate and the increase in clarity and maintainability are dramatic!

## Are There Any Downsides?

- **Initial** Setup & **Learning Curve:** Yes, there's an upfront investment to understand the concepts and set up your `build-logic` and first few plugins.

- **Abstraction Can Hide Logic:** For team members unfamiliar with the convention plugins, it might seem like "magic." Good documentation and team communication are key to ensuring everyone understands where the build logic resides.


However, for us at Kesi Android, the long-term benefits have overwhelmingly outweighed these initial considerations, especially as our project scaled.

## Take Control of Your Gradle Builds!

Convention plugins have been a game-changer for our Android development process at Kesi Android. They've brought order to chaos, enforced consistency, and allowed our developers to focus more on building great features rather than wrestling with Gradle configurations.

If you're working on a medium to large Android project, or even a smaller one you expect to grow, I wholeheartedly encourage you to explore convention plugins. Start small, identify common patterns in your `build.gradle.kts` files, and refactor them into your first convention plugin. You won't regret it!

**Your turn!** What are your experiences with managing Gradle build logic? Have you tried convention plugins, or are you inspired to start? Share your thoughts, questions, and successes in the comments below!