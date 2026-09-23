package pe.edu.upeu.biblioandes.domain.usecase

import pe.edu.upeu.biblioandes.domain.model.EstadoPrestamo
import pe.edu.upeu.biblioandes.domain.model.Prestamo
import pe.edu.upeu.biblioandes.domain.repository.BibliotecaRepository

class ObtenerPrestamosUseCase(private val repository: BibliotecaRepository) {
    suspend operator fun invoke(): List<Prestamo> {
        val prestamos = repository.getPrestamos()
        val currentDate = "2026-09-23"
        
        return prestamos.map { prestamo ->
            // RN-03: Todo préstamo dura siete días y un préstamo cuya fecha límite ya pasó debe considerarse Vencido.
            if (prestamo.estado is EstadoPrestamo.Activo && currentDate > prestamo.fechaLimite) {
                prestamo.copy(estado = EstadoPrestamo.Vencido(1)) 
            } else {
                prestamo
            }
        }
    }
}
