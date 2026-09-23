package pe.edu.upeu.biblioandes.presentation.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.biblioandes.domain.model.EstadoPrestamo
import pe.edu.upeu.biblioandes.domain.model.Estudiante
import pe.edu.upeu.biblioandes.domain.model.Prestamo
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerEstudianteUseCase
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerPrestamosUseCase

data class InicioUiState(
    val isLoading: Boolean = true,
    val estudiante: Estudiante? = null,
    val proximoVencer: Prestamo? = null,
    val isPrestamosEmpty: Boolean = false,
    val isError: Boolean = false
)

class InicioViewModel(
    private val obtenerEstudianteUseCase: ObtenerEstudianteUseCase,
    private val obtenerPrestamosUseCase: ObtenerPrestamosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(InicioUiState())
    val uiState: StateFlow<InicioUiState> = _uiState.asStateFlow()

    init {
        cargarDatos()
    }

    fun cargarDatos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }
            try {
                val estudianteDeferred = async { obtenerEstudianteUseCase() }
                val prestamosDeferred = async { obtenerPrestamosUseCase() }
                
                val estudiante = estudianteDeferred.await()
                val prestamos = prestamosDeferred.await()
                
                val activos = prestamos.filter { it.estado is EstadoPrestamo.Activo }
                val proximo = activos.minByOrNull { it.fechaLimite }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        estudiante = estudiante,
                        proximoVencer = proximo,
                        isPrestamosEmpty = prestamos.isEmpty()
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, isError = true) }
            }
        }
    }
}
