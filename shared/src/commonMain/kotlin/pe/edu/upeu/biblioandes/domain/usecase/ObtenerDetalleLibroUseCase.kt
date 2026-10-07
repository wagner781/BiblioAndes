package pe.edu.upeu.biblioandes.domain.usecase

import pe.edu.upeu.biblioandes.domain.model.Libro
import pe.edu.upeu.biblioandes.domain.repository.BibliotecaRepository

class ObtenerDetalleLibroUseCase(private val repository: BibliotecaRepository) {
    suspend operator fun invoke(id: Int): Libro? = repository.obtenerDetalleLibro(id)
}
