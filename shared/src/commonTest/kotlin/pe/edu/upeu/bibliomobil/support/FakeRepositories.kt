package pe.edu.upeu.bibliomobil.support

import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

class FakeLibroRepository : LibroRepository {
    var fallo: Throwable? = null
    var ultimoLibroRecibido: Libro? = null
    val libros = mutableListOf<Libro>()

    override suspend fun registrar(libro: Libro): Libro {
        fallo?.let { throw it }
        ultimoLibroRecibido = libro
        return libro.copy(id = (libros.maxOfOrNull { it.id } ?: 0L) + 1L)
            .also(libros::add)
    }

    override suspend fun listar(): List<Libro> {
        fallo?.let { throw it }
        return libros.toList()
    }
}

class FakeLectorRepository : LectorRepository {
    var fallo: Throwable? = null
    var ultimoLectorRecibido: Lector? = null
    val lectores = mutableListOf<Lector>()

    override suspend fun registrar(lector: Lector): Lector {
        fallo?.let { throw it }
        ultimoLectorRecibido = lector
        return lector.copy(id = (lectores.maxOfOrNull { it.id } ?: 0L) + 1L)
            .also(lectores::add)
    }

    override suspend fun listar(): List<Lector> {
        fallo?.let { throw it }
        return lectores.toList()
    }
}
