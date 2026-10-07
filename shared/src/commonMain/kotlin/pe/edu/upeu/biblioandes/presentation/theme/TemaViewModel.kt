package pe.edu.upeu.biblioandes.presentation.theme

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TemaViewModel : ViewModel() {
    private val _oscuro = MutableStateFlow(false)
    val oscuro: StateFlow<Boolean> = _oscuro.asStateFlow()

    fun cambiar(usarTemaOscuro: Boolean) {
        _oscuro.value = usarTemaOscuro
    }
}
