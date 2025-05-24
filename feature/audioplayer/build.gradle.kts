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
    implementation(libs.androidx.media3.common)
    implementation(libs.coil.compose)

    implementation(projects.core.app)
    
    testImplementation(libs.coil.test)
}