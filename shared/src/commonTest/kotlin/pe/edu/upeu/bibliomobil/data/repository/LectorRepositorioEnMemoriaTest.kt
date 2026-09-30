package pe.edu.upeu.bibliomobil.data.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.test.runTest
import pe.edu.upeu.bibliomobil.domain.model.Lector
import kotlin.test.Test
import kotlin.test.assertEquals

class LectorRepositorioEnMemoriaTest {
    @Test
    fun asignaIdsCorrelativosDesdeUno() = runTest {
        val repository = LectorRepositorioEnMemoria {}

        val primero = repository.registrar(lector("Primero"))
        val segundo = repository.registrar(lector("Segundo"))

        assertEquals(listOf(1L, 2L), listOf(primero.id, segundo.id))
    }

    @Test
    fun listaEnOrdenDeRegistro() = runTest {
        val repository = LectorRepositorioEnMemoria {}
        repository.registrar(lector("Primero"))
        repository.registrar(lector("Segundo"))

        assertEquals(listOf("Primero", "Segundo"), repository.listar().map { it.nombre })
    }

    @Test
    fun protegeIdsDuranteRegistrosConcurrentes() = runTest {
        val repository = LectorRepositorioEnMemoria {}

        val ids = List(40) { indice ->
            async { repository.registrar(lector("Lector $indice")).id }
        }.awaitAll()

        assertEquals((1L..40L).toList(), ids.sorted())
    }

    private fun lector(nombre: String) = Lector(
        id = 99L,
        nombre = nombre,
        correo = "lector@correo.pe",
        telefono = null,
    )
}
