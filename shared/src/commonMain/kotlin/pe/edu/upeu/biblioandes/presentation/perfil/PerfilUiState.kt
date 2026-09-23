package pe.edu.upeu.biblioandes.presentation.perfil

import pe.edu.upeu.biblioandes.domain.model.Estudiante

data class PerfilUiState(
    val isLoading: Boolean = true,
    val estudiante: Estudiante? = null,
    val isError: Boolean = false
)
