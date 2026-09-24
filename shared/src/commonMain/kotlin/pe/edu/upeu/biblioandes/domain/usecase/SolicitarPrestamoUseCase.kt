package pe.edu.upeu.biblioandes.domain.usecase

import pe.edu.upeu.biblioandes.domain.repository.BibliotecaRepository
import pe.edu.upeu.biblioandes.domain.model.Prestamo

class SolicitarPrestamoUseCase(private val repository: BibliotecaRepository) {
    suspend operator fun invoke(libroId: Int, codigoEstudiante: String): Result<Prestamo> {
        val libro = repository.obtenerDetalleLibro(libroId)
            ?: return Result.failure(Exception("Libro no encontrado"))

        if (libro.ejemplaresDisponibles <= 0) {
            return Result.failure(Exception("Sin ejemplares disponibles"))
        }

        val prestamosUsuario = repository.obtenerPrestamos(codigoEstudiante)
        val activos = prestamosUsuario.count { it.estado is pe.edu.upeu.biblioandes.domain.model.EstadoPrestamo.Activo }
        if (activos >= 3) {
            return Result.failure(Exception("Límite de préstamos activos alcanzado"))
        }

        val tieneVencidos = prestamosUsuario.any { it.estado is pe.edu.upeu.biblioandes.domain.model.EstadoPrestamo.Vencido }
        if (tieneVencidos) {
            return Result.failure(Exception("No puede solicitar con préstamos vencidos"))
        }

        return repository.solicitarPrestamo(libroId, codigoEstudiante)
    }
}
