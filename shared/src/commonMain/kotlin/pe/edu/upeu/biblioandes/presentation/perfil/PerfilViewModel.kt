package pe.edu.upeu.biblioandes.presentation.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.biblioandes.domain.model.Estudiante
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerEstudianteUseCase

data class PerfilUiState(
    val cargando: Boolean = true,
    val estudiante: Estudiante? = null,
    val error: String? = null
)

class PerfilViewModel(
    private val obtenerEstudiante: ObtenerEstudianteUseCase,
    scope: CoroutineScope? = null
) : ViewModel() {
    private val alcance = scope ?: viewModelScope
    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    init {
        alcance.launch {
            try {
                _uiState.value = PerfilUiState(cargando = false, estudiante = obtenerEstudiante())
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.value = PerfilUiState(cargando = false, error = error.message)
            }
        }
    }
}
