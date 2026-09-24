package pe.edu.upeu.biblioandes.ui.catalogo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.biblioandes.domain.model.Libro
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerCatalogoUseCase

sealed class CatalogoUiState {
    data object Cargando : CatalogoUiState()
    data class Exito(val libros: List<Libro>) : CatalogoUiState()
    data class Error(val mensaje: String) : CatalogoUiState()
}

class CatalogoViewModel(
    private val obtenerCatalogoUseCase: ObtenerCatalogoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CatalogoUiState>(CatalogoUiState.Cargando)
    val uiState: StateFlow<CatalogoUiState> = _uiState.asStateFlow()

    private var todosLosLibros = listOf<Libro>()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    init {
        cargarCatalogo()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        applyFilters()
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
        applyFilters()
    }

    private fun applyFilters() {
        val q = _searchQuery.value.lowercase()
        val c = _selectedCategory.value
        val filtrados = todosLosLibros.filter { libro ->
            val matchCategory = c == null || libro.categoria == c
            val matchQuery = libro.titulo.lowercase().contains(q) || libro.autor.lowercase().contains(q)
            matchCategory && matchQuery
        }
        _uiState.value = CatalogoUiState.Exito(filtrados)
    }

    private fun cargarCatalogo() {
        viewModelScope.launch {
            try {
                _uiState.value = CatalogoUiState.Cargando
                todosLosLibros = obtenerCatalogoUseCase()
                applyFilters()
            } catch (e: Exception) {
                _uiState.value = CatalogoUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}
