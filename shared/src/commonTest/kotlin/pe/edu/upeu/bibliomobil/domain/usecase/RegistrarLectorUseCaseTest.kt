package pe.edu.upeu.bibliomobil.domain.usecase

import kotlinx.coroutines.test.runTest
import pe.edu.upeu.bibliomobil.support.FakeLectorRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class RegistrarLectorUseCaseTest {
    private val repository = FakeLectorRepository()
    private val useCase = RegistrarLectorUseCase(repository)

    @Test
    fun registraLectorValidoYConvierteTelefonoVacioEnNull() = runTest {
        val lector = useCase("  Ana Torres ", " ana@correo.pe ", "   ").getOrThrow()

        assertEquals(1L, lector.id)
        assertEquals("Ana Torres", lector.nombre)
        assertEquals("ana@correo.pe", lector.correo)
        assertNull(lector.telefono)
        assertEquals(0L, repository.ultimoLectorRecibido?.id)
    }

    @Test
    fun validaNombreObligatorio() = runTest {
        val errores = erroresDe("", "ana@correo.pe", "")
        assertEquals("El nombre es obligatorio", errores.nombre)
    }

    @Test
    fun validaCorreoObligatorio() = runTest {
        val errores = erroresDe("Ana", "", "")
        assertEquals("El correo es obligatorio", errores.correo)
    }

    @Test
    fun validaFormatoDeCorreo() = runTest {
        val errores = erroresDe("Ana", "correo-invalido", "")
        assertEquals("El correo no tiene un formato válido", errores.correo)
    }

    @Test
    fun validaTelefonoCorto() = runTest {
        val errores = erroresDe("Ana", "ana@correo.pe", "12345")
        assertEquals("El teléfono debe tener entre 6 y 9 dígitos", errores.telefono)
    }

    @Test
    fun validaQueTelefonoContengaSoloDigitos() = runTest {
        val errores = erroresDe("Ana", "ana@correo.pe", "12345A")
        assertEquals("El teléfono debe tener entre 6 y 9 dígitos", errores.telefono)
    }

    @Test
    fun propagaFalloDelRepositorioComoResultFailure() = runTest {
        val fallo = IllegalStateException("Repositorio no disponible")
        repository.fallo = fallo

        assertEquals(fallo, useCase("Ana", "ana@correo.pe", "987654321").exceptionOrNull())
    }

    private suspend fun erroresDe(
        nombre: String,
        correo: String,
        telefono: String,
    ): ErroresDeLector = assertIs<LectorInvalidoException>(
        useCase(nombre, correo, telefono).exceptionOrNull(),
    ).errores
}
