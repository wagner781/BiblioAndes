package pe.edu.upeu.bibliomobil.domain.repository

import pe.edu.upeu.bibliomobil.domain.model.Lector

/** Administra el registro y la consulta de lectores de la biblioteca. */
interface LectorRepository {
    /** Registra un lector y devuelve la copia con el identificador asignado. */
    suspend fun registrar(lector: Lector): Lector

    /** Lista la cartera de lectores respetando el orden de registro. */
    suspend fun listar(): List<Lector>
}
