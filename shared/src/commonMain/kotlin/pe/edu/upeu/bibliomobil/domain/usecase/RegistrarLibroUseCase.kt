package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

data class ErroresDeLibro(
    val titulo: String? = null,
    val autor: String? = null,
    val anio: String? = null,
    val ejemplares: String? = null,
) {
    val tieneErrores: Boolean
        get() = titulo != null || autor != null || anio != null || ejemplares != null
}

class LibroInvalidoException(val errores: ErroresDeLibro) :
    IllegalArgumentException("Los datos del libro no son válidos")

class RegistrarLibroUseCase(private val repository: LibroRepository) {
    suspend operator fun invoke(
        titulo: String,
        autor: String,
        anio: String,
        ejemplares: String,
    ): Result<Libro> = resultadoDe {
        val tituloLimpio = titulo.trim()
        val autorLimpio = autor.trim()
        val anioLimpio = anio.trim()
        val ejemplaresLimpios = ejemplares.trim()
        val anioNumero = anioLimpio.toIntOrNull()
        val ejemplaresNumero = ejemplaresLimpios.toIntOrNull()

        val errores = ErroresDeLibro(
            titulo = if (tituloLimpio.isBlank()) "El título es obligatorio" else null,
            autor = if (autorLimpio.isBlank()) "El autor es obligatorio" else null,
            anio = when {
                anioLimpio.isBlank() -> "El año es obligatorio"
                anioNumero == null -> "El año debe ser un número entero"
                anioNumero !in Libro.ANIO_MINIMO..Libro.ANIO_MAXIMO ->
                    "El año debe estar entre 1450 y 2026"
                else -> null
            },
            ejemplares = when {
                ejemplaresLimpios.isBlank() -> "Los ejemplares son obligatorios"
                ejemplaresNumero == null -> "Los ejemplares deben ser un número entero"
                ejemplaresNumero < 0 -> "Los ejemplares no pueden ser negativos"
                else -> null
            },
        )

        if (errores.tieneErrores) throw LibroInvalidoException(errores)

        repository.registrar(
            Libro(
                id = 0L,
                titulo = tituloLimpio,
                autor = autorLimpio,
                anio = checkNotNull(anioNumero),
                ejemplares = checkNotNull(ejemplaresNumero),
            ),
        )
    }
}
