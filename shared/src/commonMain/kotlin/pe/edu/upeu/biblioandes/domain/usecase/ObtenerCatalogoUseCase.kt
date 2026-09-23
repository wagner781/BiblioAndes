package pe.edu.upeu.biblioandes.domain.usecase

import pe.edu.upeu.biblioandes.domain.model.Libro
import pe.edu.upeu.biblioandes.domain.repository.BibliotecaRepository

class ObtenerCatalogoUseCase(private val repository: BibliotecaRepository) {
    suspend operator fun invoke(): List<Libro> {
        return repository.getCatalogo()
    }
}
