plugins {
    alias(libs.plugins.kesiandroid.android.application)
    alias(libs.plugins.kesiandroid.android.application.compose)
    alias(libs.plugins.kesiandroid.hilt)
}

android {
    namespace = "com.kesicollection.kesiandroid"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kesicollection.kesiandroid"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

dependencies {
    implementation(projects.core.uisystem)
    implementation(projects.core.model)
    implementation(projects.feature.quiz)
    implementation(projects.feature.quiztopics)
    implementation(projects.feature.doggallery)
    implementation(projects.feature.articles)

    implementation(projects.data.retrofit)
    implementation(projects.data.room)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
}