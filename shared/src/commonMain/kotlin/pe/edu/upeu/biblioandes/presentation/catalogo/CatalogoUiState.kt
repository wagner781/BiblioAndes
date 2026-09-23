package pe.edu.upeu.biblioandes.presentation.catalogo

import pe.edu.upeu.biblioandes.domain.model.Libro

data class CatalogoUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val isEmpty: Boolean = false,
    val libros: List<Libro> = emptyList(),
    val categorias: List<String> = listOf("Todos", "Programación", "Matemática", "Redes", "Gestión", "Literatura"),
    val categoriaSeleccionada: String = "Todos",
    val textoBusqueda: String = ""
)
