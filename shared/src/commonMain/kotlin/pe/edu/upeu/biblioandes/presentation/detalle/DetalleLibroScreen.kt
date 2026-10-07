package pe.edu.upeu.biblioandes.presentation.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pe.edu.upeu.biblioandes.presentation.common.CargandoScreen
import pe.edu.upeu.biblioandes.presentation.common.ErrorScreen

@Composable
fun DetalleLibroScreen(
    libroId: Int,
    viewModel: DetalleLibroViewModel,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(libroId) { viewModel.cargar(libroId) }
    val estado by viewModel.uiState.collectAsState()
    val libro = estado.libro

    when {
        estado.cargando -> CargandoScreen(modifier)
        libro == null -> ErrorScreen(estado.error ?: "Libro no encontrado", modifier = modifier)
        else -> {
            Column(modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(libro.titulo, style = MaterialTheme.typography.headlineMedium)
                Text(libro.autor, style = MaterialTheme.typography.titleMedium)
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Año: ${libro.anio}")
                        Text("Categoría: ${libro.categoria}")
                        Text("Sede: ${libro.sede}")
                        Text("Ejemplares disponibles: ${libro.ejemplaresDisponibles}")
                    }
                }
                estado.mensaje?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
                estado.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                Button(
                    onClick = viewModel::pedirConfirmacion,
                    enabled = libro.ejemplaresDisponibles > 0 && !estado.solicitando,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (estado.solicitando) "Registrando…" else "Solicitar préstamo")
                }
            }
        }
    }

    if (estado.mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = viewModel::cancelarConfirmacion,
            title = { Text("Confirmar préstamo") },
            text = { Text("El préstamo tendrá una duración de 7 días. ¿Deseas continuar?") },
            confirmButton = { TextButton(onClick = viewModel::confirmarSolicitud) { Text("Confirmar") } },
            dismissButton = { TextButton(onClick = viewModel::cancelarConfirmacion) { Text("Cancelar") } }
        )
    }
}
