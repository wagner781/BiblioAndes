package pe.edu.upeu.biblioandes.presentation.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.biblioandes.domain.model.Libro
import pe.edu.upeu.biblioandes.domain.usecase.SolicitarPrestamoUseCase

class DetalleLibroViewModel(
    private val solicitarPrestamoUseCase: SolicitarPrestamoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalleUiState())
    val uiState: StateFlow<DetalleUiState> = _uiState.asStateFlow()

    fun setLibro(libro: Libro) {
        _uiState.update { it.copy(libro = libro, error = null, showSuccess = false) }
    }

    fun solicitarPrestamo() {
        val libro = _uiState.value.libro ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                solicitarPrestamoUseCase(libro)
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        showSuccess = true,
                        libro = state.libro?.copy(ejemplaresDisponibles = state.libro.ejemplaresDisponibles - 1)
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(error = null) }
    }
    
    fun dismissSuccess() {
        _uiState.update { it.copy(showSuccess = false) }
    }
}
