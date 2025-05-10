plugins {
    alias(libs.plugins.kesiandroid.android.feature)
    alias(libs.plugins.kesiandroid.android.library.compose)
}

android {
    namespace = "com.kesicollection.feature.audioplayer"
}

dependencies {
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui.compose)
    implementation(libs.androidx.media3.session)
    implementation(projects.core.app)
}