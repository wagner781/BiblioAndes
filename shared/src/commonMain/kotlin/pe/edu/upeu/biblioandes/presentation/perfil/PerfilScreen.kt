package pe.edu.upeu.biblioandes.presentation.perfil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pe.edu.upeu.biblioandes.presentation.common.CargandoScreen
import pe.edu.upeu.biblioandes.presentation.common.ErrorScreen
import pe.edu.upeu.biblioandes.presentation.common.VacioScreen

@Composable
fun PerfilScreen(
    viewModel: PerfilViewModel,
    temaOscuro: Boolean,
    cambiarTema: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.uiState.collectAsState()
    val estudiante = estado.estudiante
    when {
        estado.cargando -> CargandoScreen(modifier)
        estado.error != null -> ErrorScreen(estado.error.orEmpty(), modifier = modifier)
        estudiante != null -> Column(
            modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Mi perfil", style = MaterialTheme.typography.headlineMedium)
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(estudiante.nombre, style = MaterialTheme.typography.titleLarge)
                    Text("Código: ${estudiante.codigo}")
                    Text(estudiante.carrera)
                    Text(estudiante.correo)
                }
            }
            Card(Modifier.fillMaxWidth()) {
                Row(
                    Modifier.fillMaxWidth().padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Tema oscuro", style = MaterialTheme.typography.titleMedium)
                        Text("Se aplica en toda la aplicación")
                    }
                    Switch(checked = temaOscuro, onCheckedChange = cambiarTema)
                }
            }
        }
        else -> VacioScreen("No hay información del estudiante", modifier)
    }
}
