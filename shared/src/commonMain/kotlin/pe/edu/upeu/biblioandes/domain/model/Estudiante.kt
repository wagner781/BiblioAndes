package pe.edu.upeu.biblioandes.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Estudiante(
 val codigo: String,
 val nombre: String,
 val carrera: String,
 val correo: String
)
