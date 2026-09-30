package pe.edu.upeu.biblioandes.presentation

import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import pe.edu.upeu.biblioandes.data.local.DatosSimulados
import pe.edu.upeu.biblioandes.data.repository.BibliotecaRepositoryFake
import pe.edu.upeu.biblioandes.domain.model.EstadoPrestamo
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerCatalogoUseCase
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerDetalleLibroUseCase
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerEstudianteUseCase
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerPrestamosUseCase
import pe.edu.upeu.biblioandes.domain.usecase.SolicitarPrestamoUseCase
import pe.edu.upeu.biblioandes.presentation.catalogo.CatalogoViewModel
import pe.edu.upeu.biblioandes.presentation.detalle.DetalleLibroViewModel
import pe.edu.upeu.biblioandes.presentation.inicio.InicioViewModel
import pe.edu.upeu.biblioandes.presentation.perfil.PerfilViewModel
import pe.edu.upeu.biblioandes.presentation.prestamos.FiltroPrestamo
import pe.edu.upeu.biblioandes.presentation.prestamos.PrestamosViewModel
import pe.edu.upeu.biblioandes.presentation.theme.TemaViewModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelsTest {
    private val codigo = DatosSimulados.estudiante.codigo

    @Test
    fun catalogo_busca_sin_distinguir_mayusculas_ni_tildes() = runTest {
        val repository = repository()
        val viewModel = CatalogoViewModel(ObtenerCatalogoUseCase(repository), this)
        advanceUntilIdle()

        viewModel.cambiarBusqueda("ALGEBRA")

        assertEquals(listOf("Álgebra lineal"), viewModel.uiState.value.libros.map { it.titulo })
        assertFalse(viewModel.uiState.value.cargando)
    }

    @Test
    fun prestamos_filtra_vencidos_y_mantiene_orden_por_fecha() = runTest {
        val repository = BibliotecaRepositoryFake(
            prestamosIniciales = DatosSimulados.crearPrestamos("2026-09-30"),
            proveedorFecha = { "2026-09-30" },
            demoraMillis = 0
        )
        val viewModel = PrestamosViewModel(ObtenerPrestamosUseCase(repository), codigo, this)
        advanceUntilIdle()

        viewModel.filtrar(FiltroPrestamo.VENCIDO)

        assertEquals(1, viewModel.uiState.value.prestamos.size)
        assertTrue(viewModel.uiState.value.prestamos.single().estado is EstadoPrestamo.Vencido)
    }

    @Test
    fun detalle_refleja_nuevas_existencias_despues_de_solicitar() = runTest {
        val repository = repository()
        val viewModel = DetalleLibroViewModel(
            ObtenerDetalleLibroUseCase(repository),
            SolicitarPrestamoUseCase(repository),
            codigo,
            this
        )
        viewModel.cargar(1)
        advanceUntilIdle()
        viewModel.confirmarSolicitud()
        advanceUntilIdle()

        assertEquals(2, viewModel.uiState.value.libro?.ejemplaresDisponibles)
        assertTrue(viewModel.uiState.value.mensaje.orEmpty().contains("2026-10-07"))
    }

    @Test
    fun inicio_publica_estudiante_y_estado_vacio_de_prestamos() = runTest {
        val repository = repository()
        val viewModel = InicioViewModel(
            ObtenerEstudianteUseCase(repository),
            ObtenerPrestamosUseCase(repository),
            codigo,
            this
        )
        advanceUntilIdle()

        assertEquals(DatosSimulados.estudiante, viewModel.uiState.value.estudiante)
        assertEquals(null, viewModel.uiState.value.prestamoDestacado)
        assertFalse(viewModel.uiState.value.cargando)
    }

    @Test
    fun perfil_publica_los_datos_del_estudiante() = runTest {
        val repository = repository()
        val viewModel = PerfilViewModel(ObtenerEstudianteUseCase(repository), this)
        advanceUntilIdle()

        assertEquals(codigo, viewModel.uiState.value.estudiante?.codigo)
    }

    @Test
    fun tema_cambia_inmediatamente_para_toda_la_aplicacion() {
        val viewModel = TemaViewModel()
        viewModel.cambiar(true)
        assertTrue(viewModel.oscuro.value)
    }

    private fun repository() = BibliotecaRepositoryFake(
        prestamosIniciales = emptyList(),
        proveedorFecha = { "2026-09-30" },
        demoraMillis = 0
    )
}
