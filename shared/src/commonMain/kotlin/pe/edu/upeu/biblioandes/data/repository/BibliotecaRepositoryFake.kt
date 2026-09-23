package pe.edu.upeu.biblioandes.data.repository

import kotlinx.coroutines.delay
import pe.edu.upeu.biblioandes.data.local.DatosSimulados
import pe.edu.upeu.biblioandes.domain.model.*
import pe.edu.upeu.biblioandes.domain.repository.BibliotecaRepository

class BibliotecaRepositoryFake : BibliotecaRepository {
    var simulateError: Boolean = false

    private val libros = DatosSimulados.libros.toMutableList()
    private val prestamos = DatosSimulados.prestamos.toMutableList()

    override suspend fun getCatalogo(): List<Libro> {
        delay(800)
        if (simulateError) throw Exception("Error simulado en catálogo")
        return libros.toList()
    }

    override suspend fun getPrestamos(): List<Prestamo> {
        delay(800)
        return prestamos.toList()
    }

    override suspend fun getEstudiante(): Estudiante {
        delay(800)
        return DatosSimulados.estudiante
    }

    override suspend fun solicitarPrestamo(libro: Libro): Prestamo {
        delay(800)
        
        val index = libros.indexOfFirst { it.id == libro.id }
        if (index != -1 && libros[index].ejemplaresDisponibles > 0) {
            libros[index] = libros[index].copy(
                ejemplaresDisponibles = libros[index].ejemplaresDisponibles - 1
            )
        }

        val currentDate = "2026-09-23"
        val limiteDate = "2026-09-30"
        
        val newPrestamo = Prestamo(
            id = (prestamos.maxOfOrNull { it.id } ?: 0) + 1,
            libro = libro,
            fechaPrestamo = currentDate,
            fechaLimite = limiteDate,
            estado = EstadoPrestamo.Activo(7)
        )
        prestamos.add(newPrestamo)
        return newPrestamo
    }
}
