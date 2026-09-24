package pe.edu.upeu.biblioandes

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

import org.koin.compose.KoinContext
import org.koin.core.context.startKoin
import pe.edu.upeu.biblioandes.di.appModule
import pe.edu.upeu.biblioandes.ui.navigation.AppNavGraph

@Composable
@Preview
fun App() {
    // Inicializar Koin de manera segura
    LaunchedEffect(Unit) {
        try {
            startKoin {
                modules(appModule)
            }
        } catch (e: Exception) {
            // Koin ya inicializado
        }
    }
    
    KoinContext {
        MaterialTheme {
            AppNavGraph()
        }
    }
}