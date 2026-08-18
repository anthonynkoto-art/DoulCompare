package com.doulcompare.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DoulCompareColors = lightColorScheme(
    primary = Marine,
    onPrimary = CardWhite,
    secondary = Rouge,
    onSecondary = CardWhite,
    tertiary = Marron,
    background = Cream,
    onBackground = Ink,
    surface = CardWhite,
    onSurface = Ink,
    surfaceVariant = LineColor,
    error = RougeDark
)

@Composable
fun DoulCompareTheme(content: @Composable () -> Unit) {
    // Le mode sombre système n'est pas géré pour l'instant : la charte
    // graphique (rouge / bleu marine / marron) reste fixe.
    val darkTheme = isSystemInDarkTheme()
    MaterialTheme(
        colorScheme = DoulCompareColors,
        typography = DoulCompareTypography,
        content = content
    )
}
