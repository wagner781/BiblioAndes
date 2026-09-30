package pe.edu.upeu.biblioandes.domain.usecase

import pe.edu.upeu.biblioandes.domain.model.Prestamo
import pe.edu.upeu.biblioandes.domain.repository.BibliotecaRepository

class ObtenerPrestamosUseCase(private val repository: BibliotecaRepository) {
    suspend operator fun invoke(codigoEstudiante: String): List<Prestamo> =
        repository.obtenerPrestamos(codigoEstudiante).sortedBy { it.fechaLimite }
}
