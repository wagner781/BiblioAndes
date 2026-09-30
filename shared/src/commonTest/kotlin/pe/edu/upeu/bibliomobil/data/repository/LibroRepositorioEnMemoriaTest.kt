package pe.edu.upeu.bibliomobil.data.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.test.runTest
import pe.edu.upeu.bibliomobil.domain.model.Libro
import kotlin.test.Test
import kotlin.test.assertEquals

class LibroRepositorioEnMemoriaTest {
    @Test
    fun asignaIdsCorrelativosDesdeUno() = runTest {
        val repository = LibroRepositorioEnMemoria {}

        val primero = repository.registrar(libro("Primero"))
        val segundo = repository.registrar(libro("Segundo"))

        assertEquals(listOf(1L, 2L), listOf(primero.id, segundo.id))
    }

    @Test
    fun listaEnOrdenDeRegistro() = runTest {
        val repository = LibroRepositorioEnMemoria {}
        repository.registrar(libro("Primero"))
        repository.registrar(libro("Segundo"))

        assertEquals(listOf("Primero", "Segundo"), repository.listar().map { it.titulo })
    }

    @Test
    fun protegeIdsDuranteRegistrosConcurrentes() = runTest {
        val repository = LibroRepositorioEnMemoria {}

        val ids = List(40) { indice ->
            async { repository.registrar(libro("Libro $indice")).id }
        }.awaitAll()

        assertEquals((1L..40L).toList(), ids.sorted())
    }

    private fun libro(titulo: String) = Libro(
        id = 99L,
        titulo = titulo,
        autor = "Autor",
        anio = 2026,
        ejemplares = 3,
    )
}
