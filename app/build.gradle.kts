import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kesiandroid.android.application)
    alias(libs.plugins.kesiandroid.android.application.compose)
    alias(libs.plugins.kesiandroid.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val configDevPropertiesFile = rootProject.file("config.dev.properties")
val configPropertiesFile = rootProject.file("config.properties")
val admobPropertiesFile = rootProject.file("admob.properties")

val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))
val configDevProperties = Properties()
configDevProperties.load(FileInputStream(configDevPropertiesFile))
val configProperties = Properties()
configProperties.load(FileInputStream(configPropertiesFile))
val admobProperties = Properties()
admobProperties.load(FileInputStream(admobPropertiesFile))

android {
    namespace = "com.kesicollection.kesiandroid"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kesicollection.kesiandroid"
        versionCode = 2
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("config") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            buildConfigField(
                "String",
                "KESI_API_URL",
                "\"${configDevProperties["kesiApiUrl"]}\""
            )
            buildConfigField(
                "String",
                "AD_UNIT_ARTICLES",
                "\"ca-app-pub-3940256099942544/9214589741\""
            )
            buildConfigField(
                "String",
                "AD_UNIT_ARTICLE",
                "\"ca-app-pub-3940256099942544/9214589741\""
            )
        }
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("config")
            ndk {
                debugSymbolLevel = "FULL"
            }
            buildConfigField(
                "String",
                "KESI_API_URL",
                "\"${configProperties["kesiApiUrl"]}\""
            )
            buildConfigField(
                "String",
                "AD_UNIT_ARTICLES",
                "\"${admobProperties["articlesBannerUintId"]}\""
            )
            buildConfigField(
                "String",
                "AD_UNIT_ARTICLE",
                "\"${admobProperties["articleBannerUintId"]}\""
            )
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
    implementation(projects.core.app)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.androidx.core.splashscreen)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

}