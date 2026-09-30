package pe.edu.upeu.bibliomobil.domain.usecase

import kotlinx.coroutines.test.runTest
import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.support.FakeLectorRepository
import pe.edu.upeu.bibliomobil.support.FakeLibroRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class ListarUseCasesTest {
    @Test
    fun listaLibrosDelRepositorio() = runTest {
        val repository = FakeLibroRepository()
        repository.libros += Libro(1, "Título", "Autor", 2026, 3)

        assertEquals(repository.libros, ListarLibrosUseCase(repository)().getOrThrow())
    }

    @Test
    fun convierteFalloAlListarLibrosEnResultFailure() = runTest {
        val repository = FakeLibroRepository().apply {
            fallo = IllegalStateException("fallo")
        }

        assertEquals("fallo", ListarLibrosUseCase(repository)().exceptionOrNull()?.message)
    }

    @Test
    fun listaLectoresDelRepositorio() = runTest {
        val repository = FakeLectorRepository()
        repository.lectores += Lector(1, "Ana", "ana@correo.pe", null)

        assertEquals(repository.lectores, ListarLectoresUseCase(repository)().getOrThrow())
    }

    @Test
    fun convierteFalloAlListarLectoresEnResultFailure() = runTest {
        val repository = FakeLectorRepository().apply {
            fallo = IllegalStateException("fallo")
        }

        assertEquals("fallo", ListarLectoresUseCase(repository)().exceptionOrNull()?.message)
    }
}
