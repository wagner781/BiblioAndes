package pe.edu.upeu.biblioandes.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.savedstate.read
import org.koin.compose.koinInject
import pe.edu.upeu.biblioandes.presentation.catalogo.CatalogoScreen
import pe.edu.upeu.biblioandes.presentation.catalogo.CatalogoViewModel
import pe.edu.upeu.biblioandes.presentation.detalle.DetalleLibroScreen
import pe.edu.upeu.biblioandes.presentation.detalle.DetalleLibroViewModel
import pe.edu.upeu.biblioandes.presentation.inicio.InicioScreen
import pe.edu.upeu.biblioandes.presentation.inicio.InicioViewModel
import pe.edu.upeu.biblioandes.presentation.perfil.PerfilScreen
import pe.edu.upeu.biblioandes.presentation.perfil.PerfilViewModel
import pe.edu.upeu.biblioandes.presentation.prestamos.PrestamosScreen
import pe.edu.upeu.biblioandes.presentation.prestamos.PrestamosViewModel

@Composable
fun AppNavHost(
    temaOscuro: Boolean,
    cambiarTema: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    val entrada by navController.currentBackStackEntryAsState()
    val ruta = entrada?.destination?.route
    val esPrincipal = Destino.barraInferior.any { it.ruta == ruta }

    Scaffold(
        topBar = {
            Surface(shadowElevation = 3.dp) {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (!esPrincipal) {
                        Button(onClick = { navController.popBackStack() }) { Text("Atrás") }
                    } else {
                        Text("BiblioAndes", style = MaterialTheme.typography.titleLarge)
                    }
                    if (ruta != Destino.Perfil.ruta) {
                        Button(onClick = { navController.navigate(Destino.Perfil.ruta) }) { Text("Perfil") }
                    }
                }
            }
        },
        bottomBar = {
            if (esPrincipal) {
                NavigationBar {
                    Destino.barraInferior.forEach { destino ->
                        NavigationBarItem(
                            selected = ruta == destino.ruta,
                            onClick = {
                                navController.navigate(destino.ruta) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Text(destino.simbolo) },
                            label = { Text(destino.etiqueta) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Destino.Inicio.ruta,
            modifier = Modifier.padding(padding)
        ) {
            composable(Destino.Inicio.ruta) {
                InicioScreen(
                    viewModel = koinInject<InicioViewModel>(),
                    irCatalogo = { navController.navigate(Destino.Catalogo.ruta) },
                    irPrestamos = { navController.navigate(Destino.Prestamos.ruta) }
                )
            }
            composable(Destino.Catalogo.ruta) {
                CatalogoScreen(
                    viewModel = koinInject<CatalogoViewModel>(),
                    abrirDetalle = { navController.navigate(Destino.Detalle.crearRuta(it)) }
                )
            }
            composable(Destino.Prestamos.ruta) {
                PrestamosScreen(viewModel = koinInject<PrestamosViewModel>())
            }
            composable(Destino.Perfil.ruta) {
                PerfilScreen(
                    viewModel = koinInject<PerfilViewModel>(),
                    temaOscuro = temaOscuro,
                    cambiarTema = cambiarTema
                )
            }
            composable(Destino.Detalle.ruta) { backStackEntry ->
                val libroId = backStackEntry.arguments?.read { getString("libroId") }?.toIntOrNull() ?: -1
                DetalleLibroScreen(libroId, koinInject<DetalleLibroViewModel>())
            }
        }
    }
}
