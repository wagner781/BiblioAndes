package pe.edu.upeu.biblioandes.ui.prestamos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pe.edu.upeu.biblioandes.domain.model.Prestamo

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
fun PrestamosScreen(
    onNavigateBack: () -> Unit,
    viewModel: PrestamosViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var studentCode by remember { mutableStateOf("") }
    var searchExecuted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Préstamos") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("<", style = MaterialTheme.typography.titleLarge)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = studentCode,
                    onValueChange = { studentCode = it },
                    label = { Text("Código de Estudiante") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { 
                        viewModel.cargarPrestamos(studentCode) 
                        searchExecuted = true
                    },
                    enabled = studentCode.isNotBlank()
                ) {
                    Text("Buscar")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (searchExecuted) {
                when (val state = uiState) {
                    is PrestamosUiState.Cargando -> {
                        CircularProgressIndicator()
                    }
                    is PrestamosUiState.Error -> {
                        Text(text = "Error: ${state.mensaje}", color = MaterialTheme.colorScheme.error)
                    }
                    is PrestamosUiState.Exito -> {
                        if (state.prestamos.isEmpty()) {
                            Text("No tienes préstamos activos.")
                        } else {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(state.prestamos) { prestamo ->
                                    PrestamoItem(prestamo = prestamo)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PrestamoItem(prestamo: Prestamo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Libro: ${prestamo.libro.titulo}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Fecha Préstamo: ${prestamo.fechaPrestamo}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Fecha Límite: ${prestamo.fechaLimite}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
        }
    }
}
