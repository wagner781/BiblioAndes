package pe.edu.upeu.biblioandes.presentation.prestamos

import pe.edu.upeu.biblioandes.domain.model.EstadoPrestamo
import pe.edu.upeu.biblioandes.domain.model.Prestamo

data class PrestamosUiState(
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val prestamos: List<Prestamo> = emptyList(),
    val filtroEstado: String = "Todos"
)
