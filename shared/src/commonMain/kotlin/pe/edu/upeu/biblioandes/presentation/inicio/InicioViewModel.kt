package pe.edu.upeu.biblioandes.presentation.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.biblioandes.domain.model.EstadoPrestamo
import pe.edu.upeu.biblioandes.domain.model.Estudiante
import pe.edu.upeu.biblioandes.domain.model.Prestamo
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerEstudianteUseCase
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerPrestamosUseCase

data class InicioUiState(
    val cargando: Boolean = true,
    val estudiante: Estudiante? = null,
    val prestamoDestacado: Prestamo? = null,
    val error: String? = null
)

class InicioViewModel(
    private val obtenerEstudiante: ObtenerEstudianteUseCase,
    private val obtenerPrestamos: ObtenerPrestamosUseCase,
    private val codigoEstudiante: String,
    scope: CoroutineScope? = null
) : ViewModel() {
    private val alcance = scope ?: viewModelScope
    private val _uiState = MutableStateFlow(InicioUiState())
    val uiState: StateFlow<InicioUiState> = _uiState.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        alcance.launch {
            _uiState.value = InicioUiState(cargando = true)
            try {
                val (estudiante, prestamos) = coroutineScope {
                    val estudianteDeferred = async { obtenerEstudiante() }
                    val prestamosDeferred = async { obtenerPrestamos(codigoEstudiante) }
                    estudianteDeferred.await() to prestamosDeferred.await()
                }
                _uiState.value = InicioUiState(
                    cargando = false,
                    estudiante = estudiante,
                    prestamoDestacado = prestamos
                        .filter { it.estado is EstadoPrestamo.Activo || it.estado is EstadoPrestamo.Vencido }
                        .minByOrNull { it.fechaLimite }
                )
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.value = InicioUiState(cargando = false, error = error.message ?: "Error inesperado")
            }
        }
    }
}
