package pe.edu.upeu.bibliomobil.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun BiblioAndesTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        typography = AppTypography,
        content = content
    )
}
