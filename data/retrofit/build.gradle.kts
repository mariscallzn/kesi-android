plugins {
    alias(libs.plugins.kesiandroid.jvm.library)
    alias(libs.plugins.kesiandroid.hilt)
}

dependencies {
    api(projects.data.api)
    
    implementation(projects.core.app)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)

    testImplementation(projects.test.core)
}