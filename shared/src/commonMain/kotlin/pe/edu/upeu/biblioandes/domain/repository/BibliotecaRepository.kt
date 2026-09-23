package pe.edu.upeu.biblioandes.domain.repository

import pe.edu.upeu.biblioandes.domain.model.Estudiante
import pe.edu.upeu.biblioandes.domain.model.Libro
import pe.edu.upeu.biblioandes.domain.model.Prestamo

interface BibliotecaRepository {
    suspend fun getCatalogo(): List<Libro>
    suspend fun getPrestamos(): List<Prestamo>
    suspend fun getEstudiante(): Estudiante
    suspend fun solicitarPrestamo(libro: Libro): Prestamo
}
