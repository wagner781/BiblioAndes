package pe.edu.upeu.bibliomobil.domain.usecase

import kotlinx.coroutines.test.runTest
import pe.edu.upeu.bibliomobil.support.FakeLibroRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class RegistrarLibroUseCaseTest {
    private val repository = FakeLibroRepository()
    private val useCase = RegistrarLibroUseCase(repository)

    @Test
    fun registraLibroValidoRecortandoTextosYConIdDelRepositorio() = runTest {
        val resultado = useCase("  El Quijote  ", "  Cervantes ", "1605", "3")

        val libro = resultado.getOrThrow()
        assertEquals(1L, libro.id)
        assertEquals("El Quijote", libro.titulo)
        assertEquals("Cervantes", libro.autor)
        assertEquals(0L, repository.ultimoLibroRecibido?.id)
    }

    @Test
    fun validaTituloObligatorio() = runTest {
        assertError("", "Autor", "2026", "3") {
            assertEquals("El título es obligatorio", it.titulo)
        }
    }

    @Test
    fun validaAutorObligatorio() = runTest {
        assertError("Título", "", "2026", "3") {
            assertEquals("El autor es obligatorio", it.autor)
        }
    }

    @Test
    fun validaAnioObligatorio() = runTest {
        assertError("Título", "Autor", "", "3") {
            assertEquals("El año es obligatorio", it.anio)
        }
    }

    @Test
    fun validaAnioEntero() = runTest {
        assertError("Título", "Autor", "dos mil", "3") {
            assertEquals("El año debe ser un número entero", it.anio)
        }
    }

    @Test
    fun validaRangoDelAnio() = runTest {
        assertError("Título", "Autor", "1449", "3") {
            assertEquals("El año debe estar entre 1450 y 2026", it.anio)
        }
    }

    @Test
    fun validaEjemplaresObligatorios() = runTest {
        assertError("Título", "Autor", "2026", "") {
            assertEquals("Los ejemplares son obligatorios", it.ejemplares)
        }
    }

    @Test
    fun validaEjemplaresEnteros() = runTest {
        assertError("Título", "Autor", "2026", "tres") {
            assertEquals("Los ejemplares deben ser un número entero", it.ejemplares)
        }
    }

    @Test
    fun validaEjemplaresNoNegativos() = runTest {
        assertError("Título", "Autor", "2026", "-1") {
            assertEquals("Los ejemplares no pueden ser negativos", it.ejemplares)
        }
    }

    @Test
    fun acumulaErroresDeLosCuatroCampos() = runTest {
        val error = assertIs<LibroInvalidoException>(
            useCase("", "", "", "").exceptionOrNull(),
        )
        assertTrue(error.errores.tieneErrores)
        assertEquals(4, listOf(
            error.errores.titulo,
            error.errores.autor,
            error.errores.anio,
            error.errores.ejemplares,
        ).count { it != null })
    }

    @Test
    fun propagaFalloDelRepositorioComoResultFailure() = runTest {
        val fallo = IllegalStateException("Repositorio no disponible")
        repository.fallo = fallo

        assertEquals(fallo, useCase("Título", "Autor", "2026", "3").exceptionOrNull())
    }

    private suspend fun assertError(
        titulo: String,
        autor: String,
        anio: String,
        ejemplares: String,
        verificar: (ErroresDeLibro) -> Unit,
    ) {
        val error = assertIs<LibroInvalidoException>(
            useCase(titulo, autor, anio, ejemplares).exceptionOrNull(),
        )
        verificar(error.errores)
    }
}
