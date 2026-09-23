package pe.edu.upeu.biblioandes.data.repository

import pe.edu.upeu.biblioandes.domain.repository.BibliotecaRepository
import pe.edu.upeu.biblioandes.data.local.DatosSimulados
import pe.edu.upeu.biblioandes.domain.model.Libro
import pe.edu.upeu.biblioandes.domain.model.Prestamo

class BibliotecaRepositoryFake : BibliotecaRepository {
    override suspend fun obtenerCatalogo(): List<Libro> {
        return DatosSimulados.libros
    }

    override suspend fun obtenerDetalleLibro(id: Int): Libro? {
        return DatosSimulados.libros.find { it.id == id }
    }

    override suspend fun obtenerPrestamos(codigoEstudiante: String): List<Prestamo> {
        return if (codigoEstudiante == DatosSimulados.estudiante.codigo) {
            DatosSimulados.prestamos
        } else {
            emptyList()
        }
    }

    override suspend fun solicitarPrestamo(libroId: Int, codigoEstudiante: String): Result<Prestamo> {
        val libro = DatosSimulados.libros.find { it.id == libroId }
        if (libro == null) return Result.failure(Exception("Libro no encontrado"))
        if (libro.ejemplaresDisponibles <= 0) return Result.failure(Exception("Sin ejemplares disponibles"))
        
        val nuevoPrestamo = Prestamo(
            id = (DatosSimulados.prestamos.maxOfOrNull { it.id } ?: 0) + 1,
            libro = libro,
            fechaPrestamo = "2026-09-23",
            fechaLimite = "2026-09-30",
            estado = pe.edu.upeu.biblioandes.domain.model.EstadoPrestamo.Activo(7)
        )
        return Result.success(nuevoPrestamo)
    }
}
