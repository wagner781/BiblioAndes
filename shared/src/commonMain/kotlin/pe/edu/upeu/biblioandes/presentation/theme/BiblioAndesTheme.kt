package pe.edu.upeu.biblioandes.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = AndesBlue,
    secondary = AndesGold,
    primaryContainer = AndesLightBlue,
    surface = AndesLightSurface,
    background = Color.White
)

private val DarkColors = darkColorScheme(
    primary = AndesGold,
    secondary = AndesLightBlue,
    primaryContainer = AndesDarkBlue,
    surface = AndesDarkSurface,
    background = Color(0xFF08161C)
)

@Composable
fun BiblioAndesTheme(oscuro: Boolean = false, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (oscuro) DarkColors else LightColors,
        typography = AppTypography,
        content = content
    )
}
