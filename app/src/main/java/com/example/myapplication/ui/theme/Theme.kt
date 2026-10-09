package com.aadil.quiz.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Grape,
    secondary = SkyBlue,
    tertiary = Sunshine,
    background = Cream,
    surface = CardWhite,
    onPrimary = CardWhite,
    onSecondary = Ink,
    onTertiary = Ink,
    onBackground = Ink,
    onSurface = Ink,
    error = Berry
)

@Composable
fun AadilsQuizTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
