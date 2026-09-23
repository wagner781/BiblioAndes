package pe.edu.upeu.biblioandes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import pe.edu.upeu.biblioandes.presentation.navigation.AppNavHost
import pe.edu.upeu.biblioandes.presentation.theme.BiblioAndesTheme

@Composable
fun App() {
    val systemIsDark = isSystemInDarkTheme()
    var isDark by remember { mutableStateOf(systemIsDark) }

    BiblioAndesTheme(useDarkTheme = isDark) {
        AppNavHost(
            isDarkTheme = isDark,
            onToggleTheme = { isDark = !isDark }
        )
    }
}
