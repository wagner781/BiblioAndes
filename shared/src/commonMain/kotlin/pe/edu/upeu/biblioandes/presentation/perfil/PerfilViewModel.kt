package pe.edu.upeu.biblioandes.presentation.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerEstudianteUseCase

class PerfilViewModel(
    private val obtenerEstudianteUseCase: ObtenerEstudianteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    init {
        cargarDatos()
    }

    fun cargarDatos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }
            try {
                val estudiante = obtenerEstudianteUseCase()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        estudiante = estudiante
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, isError = true) }
            }
        }
    }
}
