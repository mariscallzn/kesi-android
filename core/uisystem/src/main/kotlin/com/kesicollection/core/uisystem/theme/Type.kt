package com.kesicollection.core.uisystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.kesicollection.core.uisystem.R

val kFontFamily = FontFamily(
    Font(R.font.lexend_light, FontWeight.Light),
    Font(R.font.lexend_regular, FontWeight.Normal),
    Font(R.font.lexend_medium, FontWeight.Medium),
    Font(R.font.lexend_bold, FontWeight.Bold),
)

/**
 * Default baseline
 */
val baseline = Typography()

val KTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = kFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = kFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = kFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = kFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = kFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = kFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = kFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = kFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = kFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = kFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = kFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = kFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = kFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = kFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = kFontFamily),
)