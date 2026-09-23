package pe.edu.upeu.biblioandes.domain.usecase

import pe.edu.upeu.biblioandes.domain.repository.BibliotecaRepository
import pe.edu.upeu.biblioandes.domain.model.Prestamo

class SolicitarPrestamoUseCase(private val repository: BibliotecaRepository) {
    suspend operator fun invoke(libroId: Int, codigoEstudiante: String): Result<Prestamo> {
        return repository.solicitarPrestamo(libroId, codigoEstudiante)
    }
}
