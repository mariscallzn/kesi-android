package com.kesicollection.kesiandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.home.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KesiTheme {
                HomeScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}