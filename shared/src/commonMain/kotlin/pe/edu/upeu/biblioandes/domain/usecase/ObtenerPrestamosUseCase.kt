package pe.edu.upeu.biblioandes.domain.usecase

import pe.edu.upeu.biblioandes.domain.model.Prestamo
import pe.edu.upeu.biblioandes.domain.repository.BibliotecaRepository

class ObtenerPrestamosUseCase(private val repository: BibliotecaRepository) {
    suspend operator fun invoke(): List<Prestamo> {
        return repository.getPrestamos()
    }
}
