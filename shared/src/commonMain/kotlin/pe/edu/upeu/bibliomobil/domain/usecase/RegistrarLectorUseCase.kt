package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository

data class ErroresDeLector(
    val nombre: String? = null,
    val correo: String? = null,
    val telefono: String? = null,
) {
    val tieneErrores: Boolean
        get() = nombre != null || correo != null || telefono != null
}

class LectorInvalidoException(val errores: ErroresDeLector) :
    IllegalArgumentException("Los datos del lector no son válidos")

class RegistrarLectorUseCase(private val repository: LectorRepository) {
    suspend operator fun invoke(
        nombre: String,
        correo: String,
        telefono: String,
    ): Result<Lector> = resultadoDe {
        val nombreLimpio = nombre.trim()
        val correoLimpio = correo.trim()
        val telefonoLimpio = telefono.trim().ifBlank { null }

        val errores = ErroresDeLector(
            nombre = if (nombreLimpio.isBlank()) "El nombre es obligatorio" else null,
            correo = when {
                correoLimpio.isBlank() -> "El correo es obligatorio"
                !FORMATO_CORREO.matches(correoLimpio) -> "El correo no tiene un formato válido"
                else -> null
            },
            telefono = when {
                telefonoLimpio == null -> null
                telefonoLimpio.length !in 6..9 || telefonoLimpio.any { !it.isDigit() } ->
                    "El teléfono debe tener entre 6 y 9 dígitos"
                else -> null
            },
        )

        if (errores.tieneErrores) throw LectorInvalidoException(errores)

        repository.registrar(
            Lector(
                id = 0L,
                nombre = nombreLimpio,
                correo = correoLimpio,
                telefono = telefonoLimpio,
            ),
        )
    }

    companion object {
        private val FORMATO_CORREO =
            Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
