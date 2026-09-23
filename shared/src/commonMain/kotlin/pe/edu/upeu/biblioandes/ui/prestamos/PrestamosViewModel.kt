package pe.edu.upeu.biblioandes.ui.prestamos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.biblioandes.domain.model.Prestamo
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerPrestamosUseCase

sealed class PrestamosUiState {
    data object Cargando : PrestamosUiState()
    data class Exito(val prestamos: List<Prestamo>) : PrestamosUiState()
    data class Error(val mensaje: String) : PrestamosUiState()
}

class PrestamosViewModel(
    private val obtenerPrestamosUseCase: ObtenerPrestamosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PrestamosUiState>(PrestamosUiState.Cargando)
    val uiState: StateFlow<PrestamosUiState> = _uiState.asStateFlow()

    fun cargarPrestamos(codigoEstudiante: String) {
        viewModelScope.launch {
            try {
                _uiState.value = PrestamosUiState.Cargando
                val prestamos = obtenerPrestamosUseCase(codigoEstudiante)
                _uiState.value = PrestamosUiState.Exito(prestamos)
            } catch (e: Exception) {
                _uiState.value = PrestamosUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}
