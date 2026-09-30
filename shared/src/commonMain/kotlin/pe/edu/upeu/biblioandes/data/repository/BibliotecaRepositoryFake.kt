package pe.edu.upeu.biblioandes.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.biblioandes.data.local.DatosSimulados
import pe.edu.upeu.biblioandes.data.local.fechaActualIso
import pe.edu.upeu.biblioandes.domain.model.EstadoPrestamo
import pe.edu.upeu.biblioandes.domain.model.Estudiante
import pe.edu.upeu.biblioandes.domain.model.Libro
import pe.edu.upeu.biblioandes.domain.model.Prestamo
import pe.edu.upeu.biblioandes.domain.model.ReglaNegocioException
import pe.edu.upeu.biblioandes.domain.repository.BibliotecaRepository
import pe.edu.upeu.biblioandes.domain.util.diasEntre
import pe.edu.upeu.biblioandes.domain.util.sumarDias

class BibliotecaRepositoryFake(
    private val estudiante: Estudiante = DatosSimulados.estudiante,
    librosIniciales: List<Libro> = DatosSimulados.libros,
    prestamosIniciales: List<Prestamo>? = null,
    private val proveedorFecha: () -> String = ::fechaActualIso,
    private val demoraMillis: Long = 800L
) : BibliotecaRepository {
    private val mutex = Mutex()
    private val libros = librosIniciales.toMutableList()
    private val prestamos = (prestamosIniciales ?: DatosSimulados.crearPrestamos(proveedorFecha())).toMutableList()

    var simularErrorCatalogo: Boolean = false

    override suspend fun obtenerEstudiante(): Estudiante {
        esperar()
        return estudiante
    }

    override suspend fun obtenerCatalogo(): List<Libro> {
        esperar()
        if (simularErrorCatalogo) error("No fue posible cargar el catálogo simulado")
        return mutex.withLock { libros.toList() }
    }

    override suspend fun obtenerDetalleLibro(id: Int): Libro? {
        esperar()
        return mutex.withLock { libros.firstOrNull { it.id == id } }
    }

    override suspend fun obtenerPrestamos(codigoEstudiante: String): List<Prestamo> {
        esperar()
        if (codigoEstudiante != estudiante.codigo) return emptyList()
        return mutex.withLock {
            actualizarVencimientos(proveedorFecha())
            prestamos.toList()
        }
    }

    override suspend fun registrarPrestamo(libroId: Int, codigoEstudiante: String): Result<Prestamo> {
        esperar()
        return mutex.withLock {
            runCatching {
                if (codigoEstudiante != estudiante.codigo) {
                    throw ReglaNegocioException("El estudiante no existe")
                }
                val indiceLibro = libros.indexOfFirst { it.id == libroId }
                if (indiceLibro < 0) throw ReglaNegocioException("El libro no existe")
                val libro = libros[indiceLibro]
                if (libro.ejemplaresDisponibles <= 0) {
                    throw ReglaNegocioException("No hay ejemplares disponibles")
                }

                val hoy = proveedorFecha()
                val libroActualizado = libro.copy(ejemplaresDisponibles = libro.ejemplaresDisponibles - 1)
                libros[indiceLibro] = libroActualizado
                val prestamo = Prestamo(
                    id = (prestamos.maxOfOrNull { it.id } ?: 0) + 1,
                    libro = libroActualizado,
                    fechaPrestamo = hoy,
                    fechaLimite = sumarDias(hoy, 7),
                    estado = EstadoPrestamo.Activo(diasRestantes = 7)
                )
                prestamos += prestamo
                prestamo
            }
        }
    }

    private fun actualizarVencimientos(hoy: String) {
        prestamos.indices.forEach { indice ->
            val prestamo = prestamos[indice]
            if (prestamo.estado is EstadoPrestamo.Activo) {
                val diferencia = diasEntre(hoy, prestamo.fechaLimite)
                prestamos[indice] = prestamo.copy(
                    estado = if (diferencia < 0) {
                        EstadoPrestamo.Vencido(diasDeAtraso = -diferencia)
                    } else {
                        EstadoPrestamo.Activo(diasRestantes = diferencia)
                    }
                )
            }
        }
    }

    private suspend fun esperar() {
        if (demoraMillis > 0) delay(demoraMillis)
    }
}
