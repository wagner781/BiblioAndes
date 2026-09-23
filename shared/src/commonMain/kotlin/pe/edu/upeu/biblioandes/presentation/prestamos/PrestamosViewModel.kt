package pe.edu.upeu.biblioandes.presentation.prestamos

import androidx.lifecycle.ViewModel
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerPrestamosUseCase

class PrestamosViewModel(private val obtenerPrestamosUseCase: ObtenerPrestamosUseCase) : ViewModel() {
}
