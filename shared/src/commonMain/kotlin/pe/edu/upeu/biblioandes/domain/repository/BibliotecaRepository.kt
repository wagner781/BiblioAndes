package pe.edu.upeu.biblioandes.domain.repository

import pe.edu.upeu.biblioandes.domain.model.Estudiante
import pe.edu.upeu.biblioandes.domain.model.Prestamo

interface BibliotecaRepository {
    suspend fun getEstudiante(): Estudiante
    suspend fun getPrestamos(): List<Prestamo>
}
