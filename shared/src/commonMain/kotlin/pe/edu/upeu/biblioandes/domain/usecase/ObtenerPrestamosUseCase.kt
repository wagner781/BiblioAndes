package pe.edu.upeu.biblioandes.domain.usecase

import pe.edu.upeu.biblioandes.domain.repository.BibliotecaRepository
import pe.edu.upeu.biblioandes.domain.model.Prestamo

class ObtenerPrestamosUseCase(private val repository: BibliotecaRepository) {
    suspend operator fun invoke(codigoEstudiante: String): List<Prestamo> {
        return repository.obtenerPrestamos(codigoEstudiante)
    }
}
