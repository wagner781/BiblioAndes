package pe.edu.upeu.biblioandes.presentation.prestamos

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pe.edu.upeu.biblioandes.domain.model.EstadoPrestamo
import pe.edu.upeu.biblioandes.domain.model.Prestamo
import pe.edu.upeu.biblioandes.presentation.common.CargandoScreen
import pe.edu.upeu.biblioandes.presentation.common.ErrorScreen
import pe.edu.upeu.biblioandes.presentation.common.VacioScreen

@Composable
fun PrestamosScreen(viewModel: PrestamosViewModel, modifier: Modifier = Modifier) {
    val estado by viewModel.uiState.collectAsState()
    Column(modifier.fillMaxSize()) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            items(FiltroPrestamo.entries) { filtro ->
                FilterChip(
                    selected = estado.filtro == filtro,
                    onClick = { viewModel.filtrar(filtro) },
                    label = { Text(filtro.etiqueta) }
                )
            }
        }
        when {
            estado.cargando -> CargandoScreen(Modifier.weight(1f))
            estado.error != null -> ErrorScreen(estado.error.orEmpty(), viewModel::cargar, Modifier.weight(1f))
            estado.prestamos.isEmpty() -> VacioScreen("No hay préstamos para este filtro", Modifier.weight(1f))
            else -> LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
            ) {
                items(estado.prestamos, key = { it.id }) { PrestamoCard(it) }
            }
        }
    }
}

@Composable
private fun PrestamoCard(prestamo: Prestamo) {
    val (estado, color) = when (val valor = prestamo.estado) {
        is EstadoPrestamo.Activo -> "Activo · ${valor.diasRestantes} días" to MaterialTheme.colorScheme.primary
        is EstadoPrestamo.Devuelto -> "Devuelto · ${valor.fechaDevolucion}" to MaterialTheme.colorScheme.secondary
        is EstadoPrestamo.Vencido -> "Vencido · ${valor.diasDeAtraso} días" to MaterialTheme.colorScheme.error
    }
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(prestamo.libro.titulo, style = MaterialTheme.typography.titleMedium)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Límite: ${prestamo.fechaLimite}")
                Text(estado, color = color)
            }
        }
    }
}
