package pe.edu.upeu.biblioandes.ui.detalle

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
fun DetalleScreen(
    libroId: Int,
    onNavigateBack: () -> Unit,
    viewModel: DetalleViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val prestamoState by viewModel.prestamoState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var studentCode by remember { mutableStateOf("") }

    LaunchedEffect(libroId) {
        viewModel.cargarDetalle(libroId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Libro") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            when (val state = uiState) {
                is DetalleUiState.Cargando -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is DetalleUiState.Error -> {
                    Text(text = "Error: ${state.mensaje}", color = MaterialTheme.colorScheme.error)
                }
                is DetalleUiState.Exito -> {
                    val libro = state.libro
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(text = libro.titulo, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Autor: ${libro.autor}", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Categoría: ${libro.categoria}", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Descripción:", style = MaterialTheme.typography.titleSmall)
                        Text(text = libro.descripcion, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        val disponibilidadText = if (libro.ejemplaresDisponibles > 0) "${libro.ejemplaresDisponibles} disponibles" else "No disponible"
                        Text(text = "Ejemplares: $disponibilidadText", style = MaterialTheme.typography.titleMedium)
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Button(
                            onClick = { showDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = libro.ejemplaresDisponibles > 0 && prestamoState !is PrestamoActionState.Cargando
                        ) {
                            Text(if (prestamoState is PrestamoActionState.Cargando) "Solicitando..." else "Solicitar Préstamo")
                        }
                        
                        when (val pState = prestamoState) {
                            is PrestamoActionState.Error -> {
                                Text(text = pState.mensaje, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
                            }
                            is PrestamoActionState.Exito -> {
                                Text(text = "¡Préstamo exitoso! A devolver el ${pState.prestamo.fechaDevolucion}", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 8.dp))
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
        
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Código de Estudiante") },
                text = {
                    OutlinedTextField(
                        value = studentCode,
                        onValueChange = { studentCode = it },
                        label = { Text("Ingresa tu código") }
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        showDialog = false
                        viewModel.solicitarPrestamo(libroId, studentCode)
                    }) {
                        Text("Confirmar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
