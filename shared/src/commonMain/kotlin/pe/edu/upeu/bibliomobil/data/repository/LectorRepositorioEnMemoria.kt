package pe.edu.upeu.bibliomobil.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository
import kotlin.random.Random

class LectorRepositorioEnMemoria(
    private val simularLatencia: suspend () -> Unit = ::esperarLatenciaAleatoria,
) : LectorRepository {
    private val mutex = Mutex()
    private val lectores = mutableListOf<Lector>()
    private var siguienteId = 1L

    override suspend fun registrar(lector: Lector): Lector {
        simularLatencia()
        return mutex.withLock {
            lector.copy(id = siguienteId++)
                .also(lectores::add)
        }
    }

    override suspend fun listar(): List<Lector> {
        simularLatencia()
        return mutex.withLock { lectores.toList() }
    }
}

private suspend fun esperarLatenciaAleatoria() {
    delay(Random.nextLong(from = 300L, until = 801L))
}
