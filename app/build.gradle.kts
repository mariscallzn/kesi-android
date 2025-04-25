plugins {
    alias(libs.plugins.kesiandroid.android.application)
    alias(libs.plugins.kesiandroid.android.application.compose)
    alias(libs.plugins.kesiandroid.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
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
    implementation(projects.feature.articles)
    implementation(projects.feature.article)
    implementation(projects.feature.audioplayer)

    implementation(projects.data.retrofit)
    implementation(projects.data.datastore)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.androidx.core.splashscreen)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

}