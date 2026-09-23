package pe.edu.upeu.biblioandes.presentation.catalogo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.biblioandes.domain.model.Libro
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerCatalogoUseCase

fun String.quitarTildes(): String {
    var conTildes = this
    val a = arrayOf("á", "é", "í", "ó", "ú", "Á", "É", "Í", "Ó", "Ú")
    val sin = arrayOf("a", "e", "i", "o", "u", "A", "E", "I", "O", "U")
    for (i in a.indices) {
        conTildes = conTildes.replace(a[i], sin[i])
    }
    return conTildes
}

class CatalogoViewModel(
    private val obtenerCatalogoUseCase: ObtenerCatalogoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogoUiState())
    val uiState: StateFlow<CatalogoUiState> = _uiState.asStateFlow()

    private var todosLosLibros: List<Libro> = emptyList()

    init {
        cargarCatalogo()
    }

    fun cargarCatalogo() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }
            try {
                todosLosLibros = obtenerCatalogoUseCase()
                aplicarFiltros()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, isError = true, errorMessage = e.message ?: "Error desconocido") }
            }
        }
    }

    fun onCategoriaSeleccionada(categoria: String) {
        _uiState.update { it.copy(categoriaSeleccionada = categoria) }
        aplicarFiltros()
    }

    fun onBuscarTextoChange(texto: String) {
        _uiState.update { it.copy(textoBusqueda = texto) }
        aplicarFiltros()
    }

    private fun aplicarFiltros() {
        val currentState = _uiState.value
        var filtrados = todosLosLibros

        if (currentState.categoriaSeleccionada != "Todos") {
            filtrados = filtrados.filter { it.categoria == currentState.categoriaSeleccionada }
        }

        if (currentState.textoBusqueda.isNotBlank()) {
            val busqueda = currentState.textoBusqueda.quitarTildes().lowercase()
            filtrados = filtrados.filter {
                it.titulo.quitarTildes().lowercase().contains(busqueda) ||
                it.autor.quitarTildes().lowercase().contains(busqueda)
            }
        }

        _uiState.update {
            it.copy(
                isLoading = false,
                libros = filtrados,
                isEmpty = filtrados.isEmpty(),
                isError = false
            )
        }
    }
}
