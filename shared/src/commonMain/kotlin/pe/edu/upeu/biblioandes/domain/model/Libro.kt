package pe.edu.upeu.biblioandes.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Libro(
 val id: Int,
 val titulo: String,
 val autor: String,
 val anio: Int,
 val categoria: String,
 val sede: String,
 val ejemplaresDisponibles: Int
)
