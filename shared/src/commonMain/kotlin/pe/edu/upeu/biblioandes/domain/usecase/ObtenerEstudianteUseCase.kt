package pe.edu.upeu.biblioandes.domain.usecase

import pe.edu.upeu.biblioandes.domain.model.Estudiante
import pe.edu.upeu.biblioandes.domain.repository.BibliotecaRepository

class ObtenerEstudianteUseCase(private val repository: BibliotecaRepository) {
    suspend operator fun invoke(): Estudiante {
        return repository.getEstudiante()
    }
}
