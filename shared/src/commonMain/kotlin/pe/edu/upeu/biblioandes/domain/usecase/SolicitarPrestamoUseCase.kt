package pe.edu.upeu.biblioandes.domain.usecase

import pe.edu.upeu.biblioandes.domain.repository.BibliotecaRepository

class SolicitarPrestamoUseCase(
    private val repository: BibliotecaRepository,
    private val obtenerPrestamosUseCase: ObtenerPrestamosUseCase
) {
}
