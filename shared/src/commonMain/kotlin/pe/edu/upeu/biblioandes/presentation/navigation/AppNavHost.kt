package pe.edu.upeu.biblioandes.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.koin.compose.koinInject
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import pe.edu.upeu.biblioandes.presentation.inicio.InicioScreen
import pe.edu.upeu.biblioandes.presentation.inicio.InicioViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf(Destino.Inicio.ruta, Destino.Catalogo.ruta, Destino.Prestamos.ruta)) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == Destino.Inicio.ruta,
                        onClick = {
                            navController.navigate(Destino.Inicio.ruta) {
                                popUpTo(navController.graph.startDestinationRoute ?: Destino.Inicio.ruta) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Text("🏠") },
                        label = { Text("Inicio") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Destino.Catalogo.ruta,
                        onClick = {
                            navController.navigate(Destino.Catalogo.ruta) {
                                popUpTo(navController.graph.startDestinationRoute ?: Destino.Inicio.ruta) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Text("📚") },
                        label = { Text("Catálogo") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Destino.Prestamos.ruta,
                        onClick = {
                            navController.navigate(Destino.Prestamos.ruta) {
                                popUpTo(navController.graph.startDestinationRoute ?: Destino.Inicio.ruta) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Text("📖") },
                        label = { Text("Préstamos") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destino.Inicio.ruta,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(Destino.Inicio.ruta) {
                val inicioViewModel = koinInject<InicioViewModel>()
                InicioScreen(
                    viewModel = inicioViewModel,
                    onNavigateToCatalogo = { navController.navigate(Destino.Catalogo.ruta) },
                    onNavigateToPrestamos = { navController.navigate(Destino.Prestamos.ruta) },
                    onToggleTheme = onToggleTheme,
                    isDarkTheme = isDarkTheme
                )
            }
            composable(Destino.Catalogo.ruta) {
                Button(onClick = { navController.navigate(Destino.Detalle.crearRuta(1)) }) {
                    Text("Ir a detalle del libro 1")
                }
            }
            composable(Destino.Prestamos.ruta) {
                Text("Pantalla de Préstamos")
            }
            composable(Destino.Detalle.ruta) { backStackEntry ->
                val idStr = backStackEntry.arguments?.getString("libroId") ?: "Desconocido"
                Button(onClick = { navController.popBackStack() }) {
                    Text("Atrás desde Detalle de $idStr")
                }
            }
        }
    }
}

