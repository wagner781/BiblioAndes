package pe.edu.upeu.biblioandes.domain.usecase

import pe.edu.upeu.biblioandes.domain.model.EstadoPrestamo
import pe.edu.upeu.biblioandes.domain.model.Prestamo
import pe.edu.upeu.biblioandes.domain.model.ReglaNegocioException
import pe.edu.upeu.biblioandes.domain.repository.BibliotecaRepository

class SolicitarPrestamoUseCase(private val repository: BibliotecaRepository) {
    suspend operator fun invoke(libroId: Int, codigoEstudiante: String): Result<Prestamo> {
        val libro = repository.obtenerDetalleLibro(libroId)
            ?: return fallo("El libro no existe")

        if (libro.ejemplaresDisponibles <= 0) {
            return fallo("No hay ejemplares disponibles")
        }

        val prestamos = repository.obtenerPrestamos(codigoEstudiante)
        if (prestamos.any { it.estado is EstadoPrestamo.Vencido }) {
            return fallo("Regulariza tus préstamos vencidos antes de solicitar otro libro")
        }

        if (prestamos.count { it.estado is EstadoPrestamo.Activo } >= MAXIMO_PRESTAMOS_ACTIVOS) {
            return fallo("Solo puedes tener tres préstamos activos")
        }

        return repository.registrarPrestamo(libroId, codigoEstudiante)
    }

    private fun fallo(mensaje: String): Result<Prestamo> =
        Result.failure(ReglaNegocioException(mensaje))

    companion object {
        const val MAXIMO_PRESTAMOS_ACTIVOS = 3
    }
}
