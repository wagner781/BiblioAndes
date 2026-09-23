package pe.edu.upeu.biblioandes.presentation.detalle

import pe.edu.upeu.biblioandes.domain.model.Libro

data class DetalleUiState(
    val libro: Libro? = null,
    val isLoading: Boolean = false,
    val showSuccess: Boolean = false,
    val error: String? = null
)
