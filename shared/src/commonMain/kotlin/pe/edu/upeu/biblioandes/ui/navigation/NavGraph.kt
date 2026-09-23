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
import pe.edu.upeu.biblioandes.ui.catalogo.CatalogoScreen
import pe.edu.upeu.biblioandes.ui.detalle.DetalleScreen

import pe.edu.upeu.biblioandes.ui.prestamos.PrestamosScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "catalogo"
    ) {
        composable("catalogo") {
            CatalogoScreen(
                onNavigateToDetalle = { libroId ->
                    navController.navigate("detalle/$libroId")
                },
                onNavigateToPrestamos = {
                    navController.navigate("prestamos")
                }
            )
        }
        composable("detalle/{libroId}") { backStackEntry ->
            val libroId = backStackEntry.arguments?.getString("libroId")?.toIntOrNull() ?: 0
            DetalleScreen(
                libroId = libroId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("prestamos") {
            PrestamosScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
