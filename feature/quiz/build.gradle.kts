plugins {
    alias(libs.plugins.kesiandroid.android.feature)
    alias(libs.plugins.kesiandroid.android.library.compose)
}

android {
    namespace = "com.kesicollection.feature.quiz"
}

dependencies {
    implementation(projects.data.repository)
}