plugins {
    alias(libs.plugins.kesiandroid.android.library)
    alias(libs.plugins.kesiandroid.hilt)
}

android {
    namespace = "com.kesicollection.data.datastore"
}

dependencies {
    implementation(projects.data.api)
    implementation(libs.androidx.datastore.preferences)
}
