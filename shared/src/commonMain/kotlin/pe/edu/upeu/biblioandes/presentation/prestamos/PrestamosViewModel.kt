package pe.edu.upeu.biblioandes.presentation.prestamos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.biblioandes.domain.model.EstadoPrestamo
import pe.edu.upeu.biblioandes.domain.model.Prestamo
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerPrestamosUseCase

class PrestamosViewModel(
    private val obtenerPrestamosUseCase: ObtenerPrestamosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PrestamosUiState())
    val uiState: StateFlow<PrestamosUiState> = _uiState.asStateFlow()

    private var todosLosPrestamos: List<Prestamo> = emptyList()

    init {
        cargarPrestamos()
    }

    fun cargarPrestamos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                todosLosPrestamos = obtenerPrestamosUseCase()
                aplicarFiltro()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, isEmpty = true) }
            }
        }
    }

    fun onFiltroSeleccionado(filtro: String) {
        _uiState.update { it.copy(filtroEstado = filtro) }
        aplicarFiltro()
    }

    private fun aplicarFiltro() {
        val currentState = _uiState.value
        val filtrados = if (currentState.filtroEstado == "Todos") {
            todosLosPrestamos
        } else {
            todosLosPrestamos.filter { 
                when(currentState.filtroEstado) {
                    "Activo" -> it.estado is EstadoPrestamo.Activo
                    "Devuelto" -> it.estado is EstadoPrestamo.Devuelto
                    "Vencido" -> it.estado is EstadoPrestamo.Vencido
                    else -> true
                }
            }
        }

        val ordenados = filtrados.sortedBy { it.fechaLimite }

        _uiState.update {
            it.copy(
                isLoading = false,
                prestamos = ordenados,
                isEmpty = ordenados.isEmpty()
            )
        }
    }
}
