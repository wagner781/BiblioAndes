package pe.edu.upeu.biblioandes.data.repository

import kotlinx.coroutines.test.runTest
import pe.edu.upeu.biblioandes.data.local.DatosSimulados
import pe.edu.upeu.biblioandes.domain.model.EstadoPrestamo
import pe.edu.upeu.biblioandes.domain.model.Prestamo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class BibliotecaRepositoryFakeTest {
    private val hoy = "2026-09-30"

    @Test
    fun registrar_prestamo_persiste_y_reduce_existencias() = runTest {
        val repository = crearRepository()

        val resultado = repository.registrarPrestamo(1, DatosSimulados.estudiante.codigo)

        assertTrue(resultado.isSuccess)
        assertEquals(2, repository.obtenerDetalleLibro(1)?.ejemplaresDisponibles)
        assertEquals(1, repository.obtenerPrestamos(DatosSimulados.estudiante.codigo).size)
        assertEquals("2026-10-07", resultado.getOrThrow().fechaLimite)
    }

    @Test
    fun convierte_un_activo_vencido_segun_fecha_actual() = runTest {
        val prestamo = Prestamo(
            20,
            DatosSimulados.libros.first(),
            "2026-09-20",
            "2026-09-27",
            EstadoPrestamo.Activo(1)
        )
        val repository = crearRepository(listOf(prestamo))

        val actualizado = repository.obtenerPrestamos(DatosSimulados.estudiante.codigo).single()

        assertEquals(EstadoPrestamo.Vencido(3), actualizado.estado)
    }

    @Test
    fun permite_simular_error_del_catalogo() = runTest {
        val repository = crearRepository().apply { simularErrorCatalogo = true }
        assertFailsWith<IllegalStateException> { repository.obtenerCatalogo() }
    }

    private fun crearRepository(prestamos: List<Prestamo> = emptyList()) = BibliotecaRepositoryFake(
        prestamosIniciales = prestamos,
        proveedorFecha = { hoy },
        demoraMillis = 0
    )
}
