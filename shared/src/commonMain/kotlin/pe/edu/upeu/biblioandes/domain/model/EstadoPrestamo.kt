package pe.edu.upeu.biblioandes.domain.model

import kotlinx.serialization.Serializable

@Serializable
sealed class EstadoPrestamo {
 @Serializable
 data class Activo(val diasRestantes: Int) : EstadoPrestamo()
 @Serializable
 data class Devuelto(val fechaDevolucion: String) : EstadoPrestamo()
 @Serializable
 data class Vencido(val diasDeAtraso: Int) : EstadoPrestamo()
}
