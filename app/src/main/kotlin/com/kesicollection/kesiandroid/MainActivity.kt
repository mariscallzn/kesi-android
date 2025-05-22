package com.kesicollection.kesiandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import com.google.android.gms.ads.MobileAds
import com.kesicollection.articles.ArticlesRoute
import com.kesicollection.core.app.AppManager
import com.kesicollection.core.uisystem.LocalApp
import com.kesicollection.core.uisystem.LocalImageLoader
import com.kesicollection.core.uisystem.theme.KesiTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * NOTES, Links and ideas:
 * - https://medium.com/@manuelvicnt/interviewing-at-staff-level-7a31836285e6
 * - https://jussi.hallila.com/Kollections/
 * - [Youtube text fields talk](https://www.youtube.com/watch?v=oln9LO2aRVM)
 * - A place to find googlers emails: https://android-review.googlesource.com/c/platform/frameworks/support/+/2953777
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appManager: AppManager

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        lifecycleScope.launch {
            MobileAds.initialize(this@MainActivity)
        }
        setContent {
            CompositionLocalProvider(LocalImageLoader provides imageLoader) {
                CompositionLocalProvider(LocalApp provides appManager) {
                    KesiTheme {
                        AppNavigation(
                            navController = rememberNavController(),
                            startDestination = ArticlesRoute,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}