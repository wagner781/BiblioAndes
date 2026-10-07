package pe.edu.upeu.biblioandes

import androidx.compose.ui.window.ComposeUIViewController
import pe.edu.upeu.biblioandes.di.initKoin

fun MainViewController() = ComposeUIViewController {
    initKoin()
    App()
}
