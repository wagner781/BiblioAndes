package pe.edu.upeu.biblioandes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.koinInject
import pe.edu.upeu.biblioandes.presentation.navigation.AppNavHost
import pe.edu.upeu.biblioandes.presentation.theme.BiblioAndesTheme
import pe.edu.upeu.biblioandes.presentation.theme.TemaViewModel

@Composable
fun App() {
    val temaViewModel = koinInject<TemaViewModel>()
    val oscuro by temaViewModel.oscuro.collectAsState()
    BiblioAndesTheme(oscuro = oscuro) {
        AppNavHost(temaOscuro = oscuro, cambiarTema = temaViewModel::cambiar)
    }
}
