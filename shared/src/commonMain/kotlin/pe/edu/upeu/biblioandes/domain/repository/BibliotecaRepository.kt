package pe.edu.upeu.biblioandes.domain.repository

import pe.edu.upeu.biblioandes.domain.model.Libro
import pe.edu.upeu.biblioandes.domain.model.Prestamo

interface BibliotecaRepository {
    suspend fun obtenerCatalogo(): List<Libro>
    suspend fun obtenerDetalleLibro(id: Int): Libro?
    suspend fun obtenerPrestamos(codigoEstudiante: String): List<Prestamo>
    suspend fun solicitarPrestamo(libroId: Int, codigoEstudiante: String): Result<Prestamo>
}
