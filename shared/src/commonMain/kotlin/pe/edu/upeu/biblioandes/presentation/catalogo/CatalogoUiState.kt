package pe.edu.upeu.biblioandes.presentation.catalogo

import pe.edu.upeu.biblioandes.domain.model.Libro

data class CatalogoUiState(
    val cargando: Boolean = true,
    val libros: List<Libro> = emptyList(),
    val categorias: List<String> = emptyList(),
    val categoriaSeleccionada: String? = null,
    val busqueda: String = "",
    val error: String? = null
)
