package pe.edu.upeu.biblioandes.presentation.prestamos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.biblioandes.domain.model.EstadoPrestamo
import pe.edu.upeu.biblioandes.domain.model.Prestamo
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerPrestamosUseCase

enum class FiltroPrestamo(val etiqueta: String) {
    TODOS("Todos"), ACTIVO("Activo"), DEVUELTO("Devuelto"), VENCIDO("Vencido")
}

data class PrestamosUiState(
    val cargando: Boolean = true,
    val prestamos: List<Prestamo> = emptyList(),
    val filtro: FiltroPrestamo = FiltroPrestamo.TODOS,
    val error: String? = null
)

class PrestamosViewModel(
    private val obtenerPrestamos: ObtenerPrestamosUseCase,
    private val codigoEstudiante: String,
    scope: CoroutineScope? = null
) : ViewModel() {
    private val alcance = scope ?: viewModelScope
    private val _uiState = MutableStateFlow(PrestamosUiState())
    val uiState: StateFlow<PrestamosUiState> = _uiState.asStateFlow()
    private var todos: List<Prestamo> = emptyList()

    init {
        cargar()
    }

    fun cargar() {
        alcance.launch {
            _uiState.value = _uiState.value.copy(cargando = true, error = null)
            try {
                todos = obtenerPrestamos(codigoEstudiante)
                publicar()
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.value = _uiState.value.copy(cargando = false, error = error.message)
            }
        }
    }

    fun filtrar(filtro: FiltroPrestamo) {
        _uiState.value = _uiState.value.copy(filtro = filtro)
        publicar()
    }

    private fun publicar() {
        val filtro = _uiState.value.filtro
        val filtrados = todos.filter {
            when (filtro) {
                FiltroPrestamo.TODOS -> true
                FiltroPrestamo.ACTIVO -> it.estado is EstadoPrestamo.Activo
                FiltroPrestamo.DEVUELTO -> it.estado is EstadoPrestamo.Devuelto
                FiltroPrestamo.VENCIDO -> it.estado is EstadoPrestamo.Vencido
            }
        }
        _uiState.value = _uiState.value.copy(cargando = false, prestamos = filtrados, error = null)
    }
}
