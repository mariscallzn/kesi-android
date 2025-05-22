package com.kesicollection.core.uisystem

import androidx.compose.runtime.compositionLocalOf
import coil3.ImageLoader

val LocalImageLoader = compositionLocalOf<ImageLoader> {
    error("No ImageLoader provided")
}