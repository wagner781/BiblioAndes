package pe.edu.upeu.biblioandes.presentation.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pe.edu.upeu.biblioandes.domain.model.EstadoPrestamo
import pe.edu.upeu.biblioandes.presentation.common.CargandoScreen
import pe.edu.upeu.biblioandes.presentation.common.ErrorScreen
import pe.edu.upeu.biblioandes.presentation.common.VacioScreen

@Composable
fun InicioScreen(
    viewModel: InicioViewModel,
    irCatalogo: () -> Unit,
    irPrestamos: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.uiState.collectAsState()
    val estudiante = estado.estudiante
    when {
        estado.cargando -> CargandoScreen(modifier)
        estado.error != null -> ErrorScreen(estado.error.orEmpty(), viewModel::cargar, modifier)
        estudiante == null -> VacioScreen("No hay información del estudiante", modifier)
        else -> Column(
            modifier = modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text("Hola, ${estudiante.nombre}", style = MaterialTheme.typography.headlineSmall)
            Text("¿Qué libro quieres descubrir hoy?", style = MaterialTheme.typography.bodyLarge)

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Próxima devolución", style = MaterialTheme.typography.titleMedium)
                    val prestamo = estado.prestamoDestacado
                    if (prestamo == null) {
                        Text("No tienes préstamos pendientes")
                    } else {
                        Text(prestamo.libro.titulo, style = MaterialTheme.typography.titleLarge)
                        Text("Fecha límite: ${prestamo.fechaLimite}")
                        val detalle = when (val tipo = prestamo.estado) {
                            is EstadoPrestamo.Activo -> "Quedan ${tipo.diasRestantes} días"
                            is EstadoPrestamo.Vencido -> "Vencido hace ${tipo.diasDeAtraso} días"
                            is EstadoPrestamo.Devuelto -> "Devuelto"
                        }
                        Text(detalle, color = if (prestamo.estado is EstadoPrestamo.Vencido) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = irCatalogo, modifier = Modifier.weight(1f)) { Text("Ver catálogo") }
                Button(onClick = irPrestamos, modifier = Modifier.weight(1f)) { Text("Mis préstamos") }
            }
        }
    }
}
