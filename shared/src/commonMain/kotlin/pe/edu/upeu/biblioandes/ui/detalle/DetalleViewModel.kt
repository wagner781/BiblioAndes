package pe.edu.upeu.biblioandes.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.biblioandes.domain.model.Libro
import pe.edu.upeu.biblioandes.domain.model.Prestamo
import pe.edu.upeu.biblioandes.domain.repository.BibliotecaRepository
import pe.edu.upeu.biblioandes.domain.usecase.SolicitarPrestamoUseCase

sealed class DetalleUiState {
    data object Cargando : DetalleUiState()
    data class Exito(val libro: Libro) : DetalleUiState()
    data class Error(val mensaje: String) : DetalleUiState()
}

sealed class PrestamoActionState {
    data object Inactivo : PrestamoActionState()
    data object Cargando : PrestamoActionState()
    data class Exito(val prestamo: Prestamo) : PrestamoActionState()
    data class Error(val mensaje: String) : PrestamoActionState()
}

class DetalleViewModel(
    private val repository: BibliotecaRepository,
    private val solicitarPrestamoUseCase: SolicitarPrestamoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetalleUiState>(DetalleUiState.Cargando)
    val uiState: StateFlow<DetalleUiState> = _uiState.asStateFlow()

    private val _prestamoState = MutableStateFlow<PrestamoActionState>(PrestamoActionState.Inactivo)
    val prestamoState: StateFlow<PrestamoActionState> = _prestamoState.asStateFlow()

    fun cargarDetalle(libroId: Int) {
        viewModelScope.launch {
            try {
                _uiState.value = DetalleUiState.Cargando
                val libro = repository.obtenerDetalleLibro(libroId)
                if (libro != null) {
                    _uiState.value = DetalleUiState.Exito(libro)
                } else {
                    _uiState.value = DetalleUiState.Error("Libro no encontrado")
                }
            } catch (e: Exception) {
                _uiState.value = DetalleUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun solicitarPrestamo(libroId: Int, codigoEstudiante: String) {
        viewModelScope.launch {
            try {
                _prestamoState.value = PrestamoActionState.Cargando
                val resultado = solicitarPrestamoUseCase(libroId, codigoEstudiante)
                resultado.fold(
                    onSuccess = { _prestamoState.value = PrestamoActionState.Exito(it) },
                    onFailure = { _prestamoState.value = PrestamoActionState.Error(it.message ?: "Error al solicitar") }
                )
            } catch (e: Exception) {
                _prestamoState.value = PrestamoActionState.Error(e.message ?: "Error inesperado")
            }
        }
    }
    
    fun resetPrestamoState() {
        _prestamoState.value = PrestamoActionState.Inactivo
    }
}
