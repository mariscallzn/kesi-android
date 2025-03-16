package com.kesicollection.kesiandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.kesicollection.core.uisystem.theme.KesiTheme
import com.kesicollection.feature.quiz.QuizScreen
import com.kesicollection.feature.quiz.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * NOTES, Links and ideas:
 * - https://medium.com/@manuelvicnt/interviewing-at-staff-level-7a31836285e6
 * - https://jussi.hallila.com/Kollections/
 * - [Youtube text fields talk](https://www.youtube.com/watch?v=oln9LO2aRVM)
 * - A place to find googlers emails: https://android-review.googlesource.com/c/platform/frameworks/support/+/2953777
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: QuizViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KesiTheme {
                QuizScreen(
                    viewModel = viewModel,
                    topic = "Jetpack Compose",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}