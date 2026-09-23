package pe.edu.upeu.biblioandes.ui.catalogo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pe.edu.upeu.biblioandes.domain.model.Libro

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
fun CatalogoScreen(
    onNavigateToDetalle: (Int) -> Unit,
    onNavigateToPrestamos: () -> Unit,
    viewModel: CatalogoViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de Libros") },
                actions = {
                    IconButton(onClick = onNavigateToPrestamos) {
                        Icon(Icons.Filled.List, contentDescription = "Mis Préstamos")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is CatalogoUiState.Cargando -> {
                    CircularProgressIndicator()
                }
                is CatalogoUiState.Error -> {
                    Text(text = "Error: ${state.mensaje}", color = MaterialTheme.colorScheme.error)
                }
                is CatalogoUiState.Exito -> {
                    if (state.libros.isEmpty()) {
                        Text("No hay libros disponibles en el catálogo.")
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(state.libros) { libro ->
                                LibroItem(libro = libro, onClick = { onNavigateToDetalle(libro.id) })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LibroItem(libro: Libro, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = libro.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Autor: ${libro.autor}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Categoría: ${libro.categoria}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            val disponibilidadText = if (libro.ejemplaresDisponibles > 0) "${libro.ejemplaresDisponibles} disponibles" else "No disponible"
            val color = if (libro.ejemplaresDisponibles > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            Text(text = disponibilidadText, style = MaterialTheme.typography.labelMedium, color = color)
        }
    }
}
