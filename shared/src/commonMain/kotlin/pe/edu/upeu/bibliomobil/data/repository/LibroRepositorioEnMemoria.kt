package pe.edu.upeu.bibliomobil.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository
import kotlin.random.Random

class LibroRepositorioEnMemoria(
    private val simularLatencia: suspend () -> Unit = ::esperarLatenciaAleatoria,
) : LibroRepository {
    private val mutex = Mutex()
    private val libros = mutableListOf<Libro>()
    private var siguienteId = 1L

    override suspend fun registrar(libro: Libro): Libro {
        simularLatencia()
        return mutex.withLock {
            libro.copy(id = siguienteId++)
                .also(libros::add)
        }
    }

    override suspend fun listar(): List<Libro> {
        simularLatencia()
        return mutex.withLock { libros.toList() }
    }
}

private suspend fun esperarLatenciaAleatoria() {
    delay(Random.nextLong(from = 300L, until = 801L))
}
