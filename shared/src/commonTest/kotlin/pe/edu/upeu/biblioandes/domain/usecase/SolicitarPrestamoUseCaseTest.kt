package pe.edu.upeu.biblioandes.domain.usecase

import kotlinx.coroutines.test.runTest
import pe.edu.upeu.biblioandes.data.local.DatosSimulados
import pe.edu.upeu.biblioandes.data.repository.BibliotecaRepositoryFake
import pe.edu.upeu.biblioandes.domain.model.EstadoPrestamo
import pe.edu.upeu.biblioandes.domain.model.Libro
import pe.edu.upeu.biblioandes.domain.model.Prestamo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SolicitarPrestamoUseCaseTest {
    private val codigo = DatosSimulados.estudiante.codigo
    private val hoy = "2026-09-30"

    @Test
    fun rn01_rechaza_cuarto_prestamo_activo() = runTest {
        val activos = (1..3).map { id -> prestamo(id, EstadoPrestamo.Activo(id)) }
        val resultado = casoUso(activos)(1, codigo)
        assertTrue(resultado.isFailure)
        assertTrue(resultado.exceptionOrNull()?.message.orEmpty().contains("tres"))
    }

    @Test
    fun rn02_rechaza_libro_sin_ejemplares() = runTest {
        val libroAgotado = DatosSimulados.libros.first().copy(ejemplaresDisponibles = 0)
        val resultado = casoUso(libros = listOf(libroAgotado))(libroAgotado.id, codigo)
        assertTrue(resultado.isFailure)
        assertTrue(resultado.exceptionOrNull()?.message.orEmpty().contains("ejemplares"))
    }

    @Test
    fun rn03_crea_prestamo_por_siete_dias() = runTest {
        val resultado = casoUso()(1, codigo)
        assertTrue(resultado.isSuccess)
        assertEquals(hoy, resultado.getOrThrow().fechaPrestamo)
        assertEquals("2026-10-07", resultado.getOrThrow().fechaLimite)
        assertEquals(EstadoPrestamo.Activo(7), resultado.getOrThrow().estado)
    }

    @Test
    fun rn04_rechaza_solicitud_si_existe_vencido() = runTest {
        val resultado = casoUso(listOf(prestamo(1, EstadoPrestamo.Vencido(2))))(1, codigo)
        assertTrue(resultado.isFailure)
        assertTrue(resultado.exceptionOrNull()?.message.orEmpty().contains("vencidos"))
    }

    private fun casoUso(
        prestamos: List<Prestamo> = emptyList(),
        libros: List<Libro> = DatosSimulados.libros
    ): SolicitarPrestamoUseCase = SolicitarPrestamoUseCase(
        BibliotecaRepositoryFake(
            librosIniciales = libros,
            prestamosIniciales = prestamos,
            proveedorFecha = { hoy },
            demoraMillis = 0
        )
    )

    private fun prestamo(id: Int, estado: EstadoPrestamo) = Prestamo(
        id = id,
        libro = DatosSimulados.libros[id % DatosSimulados.libros.size],
        fechaPrestamo = "2026-09-29",
        fechaLimite = "2026-10-06",
        estado = estado
    )
}
