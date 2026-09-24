package pe.edu.upeu.biblioandes.data.repository

import pe.edu.upeu.biblioandes.domain.repository.BibliotecaRepository
import pe.edu.upeu.biblioandes.data.local.DatosSimulados
import pe.edu.upeu.biblioandes.domain.model.Libro
import pe.edu.upeu.biblioandes.domain.model.Prestamo
import kotlinx.coroutines.delay

class BibliotecaRepositoryFake : BibliotecaRepository {
    override suspend fun obtenerCatalogo(): List<Libro> {
        delay(800)
        return DatosSimulados.libros
    }

    override suspend fun obtenerDetalleLibro(id: Int): Libro? {
        delay(800)
        return DatosSimulados.libros.find { it.id == id }
    }

    override suspend fun obtenerPrestamos(codigoEstudiante: String): List<Prestamo> {
        delay(800)
        return if (codigoEstudiante == DatosSimulados.estudiante.codigo) {
            DatosSimulados.prestamos
        } else {
            emptyList()
        }
    }

    override suspend fun solicitarPrestamo(libroId: Int, codigoEstudiante: String): Result<Prestamo> {
        delay(800)
        val libro = DatosSimulados.libros.find { it.id == libroId }
            ?: return Result.failure(Exception("Libro no encontrado en BD"))
        
        val nuevoPrestamo = Prestamo(
            id = (DatosSimulados.prestamos.maxOfOrNull { it.id } ?: 0) + 1,
            libro = libro,
            fechaPrestamo = "2026-09-24",
            fechaLimite = "2026-10-01",
            estado = pe.edu.upeu.biblioandes.domain.model.EstadoPrestamo.Activo(7)
        )
        return Result.success(nuevoPrestamo)
    }
}
