package pe.edu.upeu.biblioandes.data.repository

import pe.edu.upeu.biblioandes.domain.model.Estudiante
import pe.edu.upeu.biblioandes.domain.model.Prestamo
import pe.edu.upeu.biblioandes.domain.repository.BibliotecaRepository
import pe.edu.upeu.biblioandes.data.local.DatosSimulados
import kotlinx.coroutines.delay

class BibliotecaRepositoryFake : BibliotecaRepository {
    override suspend fun getEstudiante(): Estudiante {
        delay(800)
        return DatosSimulados.estudiante
    }

    override suspend fun getPrestamos(): List<Prestamo> {
        delay(800)
        return DatosSimulados.prestamos
    }
}
