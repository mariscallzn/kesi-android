plugins {
    alias(libs.plugins.kesiandroid.jvm.library)
    alias(libs.plugins.kesiandroid.hilt)
}

dependencies {
    api(projects.data.api)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)
}