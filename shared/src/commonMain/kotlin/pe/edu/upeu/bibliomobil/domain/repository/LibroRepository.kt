package pe.edu.upeu.bibliomobil.domain.repository

import pe.edu.upeu.bibliomobil.domain.model.Libro

/** Administra el registro y la consulta del catálogo de libros. */
interface LibroRepository {
    /** Registra un libro y devuelve la copia con el identificador asignado. */
    suspend fun registrar(libro: Libro): Libro

    /** Lista el catálogo respetando el orden de registro. */
    suspend fun listar(): List<Libro>
}
