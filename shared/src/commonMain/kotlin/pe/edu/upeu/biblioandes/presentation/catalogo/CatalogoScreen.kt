package pe.edu.upeu.biblioandes.presentation.catalogo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pe.edu.upeu.biblioandes.domain.model.Libro
import pe.edu.upeu.biblioandes.presentation.common.CargandoScreen
import pe.edu.upeu.biblioandes.presentation.common.ErrorScreen
import pe.edu.upeu.biblioandes.presentation.common.VacioScreen

@Composable
fun CatalogoScreen(
    viewModel: CatalogoViewModel,
    abrirDetalle: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.uiState.collectAsState()
    Column(modifier.fillMaxSize()) {
        OutlinedTextField(
            value = estado.busqueda,
            onValueChange = viewModel::cambiarBusqueda,
            label = { Text("Buscar por título o autor") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            item {
                FilterChip(
                    selected = estado.categoriaSeleccionada == null,
                    onClick = { viewModel.seleccionarCategoria(null) },
                    label = { Text("Todos") }
                )
            }
            items(estado.categorias) { categoria ->
                FilterChip(
                    selected = estado.categoriaSeleccionada == categoria,
                    onClick = { viewModel.seleccionarCategoria(categoria) },
                    label = { Text(categoria) }
                )
            }
        }
        when {
            estado.cargando -> CargandoScreen(Modifier.weight(1f))
            estado.error != null -> ErrorScreen(estado.error.orEmpty(), viewModel::cargar, Modifier.weight(1f))
            estado.libros.isEmpty() -> VacioScreen("No se encontraron libros", Modifier.weight(1f))
            else -> LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) {
                items(estado.libros, key = { it.id }) { libro ->
                    LibroCard(libro, abrirDetalle)
                }
            }
        }
    }
}

@Composable
private fun LibroCard(libro: Libro, abrirDetalle: (Int) -> Unit) {
    Card(Modifier.fillMaxWidth().clickable { abrirDetalle(libro.id) }) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(libro.titulo, style = MaterialTheme.typography.titleMedium)
            Text(libro.autor)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(libro.categoria, color = MaterialTheme.colorScheme.primary)
                Text("Disponibles: ${libro.ejemplaresDisponibles}")
            }
        }
    }
}
