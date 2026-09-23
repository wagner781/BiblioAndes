package pe.edu.upeu.biblioandes.domain.usecase

import pe.edu.upeu.biblioandes.domain.model.EstadoPrestamo
import pe.edu.upeu.biblioandes.domain.model.Libro
import pe.edu.upeu.biblioandes.domain.repository.BibliotecaRepository

class SolicitarPrestamoUseCase(
    private val repository: BibliotecaRepository,
    private val obtenerPrestamosUseCase: ObtenerPrestamosUseCase
) {
    suspend operator fun invoke(libro: Libro) {
        // RN-02: No se puede solicitar un libro cuyo ejemplaresDisponibles sea 0.
        if (libro.ejemplaresDisponibles <= 0) {
            throw Exception("No hay ejemplares disponibles de este libro.")
        }

        val prestamos = obtenerPrestamosUseCase()
        
        // RN-04: Un estudiante que tenga al menos un préstamo Vencido no puede solicitar un libro nuevo.
        val tieneVencidos = prestamos.any { it.estado is EstadoPrestamo.Vencido }
        if (tieneVencidos) {
            throw Exception("No puedes solicitar libros porque tienes préstamos vencidos.")
        }

        // RN-01: Un estudiante no puede tener más de tres préstamos Activos simultáneamente.
        val activos = prestamos.count { it.estado is EstadoPrestamo.Activo }
        if (activos >= 3) {
            throw Exception("Límite de 3 préstamos activos alcanzado.")
        }

        repository.solicitarPrestamo(libro)
    }
}
