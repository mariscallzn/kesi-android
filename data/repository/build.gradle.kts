plugins {
    alias(libs.plugins.kesiandroid.android.library)
    alias(libs.plugins.kesiandroid.hilt)
}

android {
    namespace = "com.kesicollection.data.repository"
}

dependencies {
    api(projects.core.model)
    implementation(projects.data.retrofit)
    implementation(projects.data.room)
}