package pe.edu.upeu.biblioandes.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "catalogo"
    ) {
        composable("catalogo") {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Pantalla de Catálogo (Placeholder)")
            }
        }
        composable("detalle/{libroId}") { backStackEntry ->
            val libroId = backStackEntry.arguments?.getString("libroId")
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Pantalla de Detalle (Placeholder) - Libro: $libroId")
            }
        }
    }
}
