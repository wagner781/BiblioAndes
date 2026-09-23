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

    init {
        cargarCatalogo()
    }

    private fun cargarCatalogo() {
        viewModelScope.launch {
            try {
                _uiState.value = CatalogoUiState.Cargando
                val libros = obtenerCatalogoUseCase()
                _uiState.value = CatalogoUiState.Exito(libros)
            } catch (e: Exception) {
                _uiState.value = CatalogoUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}
