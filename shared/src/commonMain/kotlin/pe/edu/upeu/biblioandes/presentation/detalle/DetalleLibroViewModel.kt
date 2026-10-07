package pe.edu.upeu.biblioandes.presentation.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.biblioandes.domain.model.Libro
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerDetalleLibroUseCase
import pe.edu.upeu.biblioandes.domain.usecase.SolicitarPrestamoUseCase

data class DetalleLibroUiState(
    val cargando: Boolean = false,
    val libro: Libro? = null,
    val solicitando: Boolean = false,
    val mostrarConfirmacion: Boolean = false,
    val mensaje: String? = null,
    val error: String? = null
)

class DetalleLibroViewModel(
    private val obtenerDetalleLibro: ObtenerDetalleLibroUseCase,
    private val solicitarPrestamo: SolicitarPrestamoUseCase,
    private val codigoEstudiante: String,
    scope: CoroutineScope? = null
) : ViewModel() {
    private val alcance = scope ?: viewModelScope
    private val _uiState = MutableStateFlow(DetalleLibroUiState())
    val uiState: StateFlow<DetalleLibroUiState> = _uiState.asStateFlow()

    fun cargar(id: Int) {
        if (_uiState.value.libro?.id == id) return
        alcance.launch {
            _uiState.value = DetalleLibroUiState(cargando = true)
            try {
                val libro = obtenerDetalleLibro(id)
                _uiState.value = DetalleLibroUiState(
                    cargando = false,
                    libro = libro,
                    error = if (libro == null) "Libro no encontrado" else null
                )
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.value = DetalleLibroUiState(cargando = false, error = error.message)
            }
        }
    }

    fun pedirConfirmacion() {
        _uiState.value = _uiState.value.copy(mostrarConfirmacion = true, mensaje = null, error = null)
    }

    fun cancelarConfirmacion() {
        _uiState.value = _uiState.value.copy(mostrarConfirmacion = false)
    }

    fun confirmarSolicitud() {
        val libro = _uiState.value.libro ?: return
        alcance.launch {
            _uiState.value = _uiState.value.copy(mostrarConfirmacion = false, solicitando = true, error = null)
            val resultado = solicitarPrestamo(libro.id, codigoEstudiante)
            if (resultado.isSuccess) {
                val actualizado = obtenerDetalleLibro(libro.id)
                _uiState.value = _uiState.value.copy(
                    solicitando = false,
                    libro = actualizado,
                    mensaje = "Préstamo registrado hasta ${resultado.getOrThrow().fechaLimite}"
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    solicitando = false,
                    error = resultado.exceptionOrNull()?.message ?: "No se pudo registrar el préstamo"
                )
            }
        }
    }
}
