package pe.edu.upeu.biblioandes.presentation.catalogo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.biblioandes.domain.model.Libro
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerCatalogoUseCase

class CatalogoViewModel(
    private val obtenerCatalogo: ObtenerCatalogoUseCase,
    scope: CoroutineScope? = null
) : ViewModel() {
    private val alcance = scope ?: viewModelScope
    private val _uiState = MutableStateFlow(CatalogoUiState())
    val uiState: StateFlow<CatalogoUiState> = _uiState.asStateFlow()
    private var catalogoCompleto: List<Libro> = emptyList()

    init {
        cargar()
    }

    fun cargar() {
        alcance.launch {
            _uiState.value = _uiState.value.copy(cargando = true, error = null)
            try {
                catalogoCompleto = obtenerCatalogo()
                publicar()
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.value = _uiState.value.copy(
                    cargando = false,
                    libros = emptyList(),
                    error = error.message ?: "No se pudo cargar el catálogo"
                )
            }
        }
    }

    fun cambiarBusqueda(valor: String) {
        _uiState.value = _uiState.value.copy(busqueda = valor)
        publicar()
    }

    fun seleccionarCategoria(categoria: String?) {
        _uiState.value = _uiState.value.copy(categoriaSeleccionada = categoria)
        publicar()
    }

    private fun publicar() {
        val estado = _uiState.value
        val consulta = normalizar(estado.busqueda.trim())
        val filtrados = catalogoCompleto.filter { libro ->
            (estado.categoriaSeleccionada == null || libro.categoria == estado.categoriaSeleccionada) &&
                (consulta.isEmpty() || normalizar(libro.titulo).contains(consulta) || normalizar(libro.autor).contains(consulta))
        }
        _uiState.value = estado.copy(
            cargando = false,
            libros = filtrados,
            categorias = catalogoCompleto.map { it.categoria }.distinct(),
            error = null
        )
    }

    private fun normalizar(texto: String): String = texto.lowercase()
        .replace('á', 'a').replace('é', 'e').replace('í', 'i')
        .replace('ó', 'o').replace('ú', 'u').replace('ü', 'u').replace('ñ', 'n')
}
