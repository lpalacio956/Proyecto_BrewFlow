package com.brewflow.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Brown700,
    secondary = Amber500,
    tertiary = Brown500,
    secondaryContainer = Brown500,
    onSecondaryContainer = Color.White
)

@Composable
fun BrewFlowTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}
