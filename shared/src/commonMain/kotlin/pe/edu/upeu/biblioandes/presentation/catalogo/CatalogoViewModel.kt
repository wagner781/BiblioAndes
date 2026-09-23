package pe.edu.upeu.biblioandes.presentation.catalogo

import androidx.lifecycle.ViewModel
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerCatalogoUseCase

class CatalogoViewModel(private val obtenerCatalogoUseCase: ObtenerCatalogoUseCase) : ViewModel() {
}
