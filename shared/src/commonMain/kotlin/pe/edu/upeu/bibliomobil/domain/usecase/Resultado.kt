package pe.edu.upeu.bibliomobil.domain.usecase

import kotlinx.coroutines.CancellationException

suspend inline fun <T> resultadoDe(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (error: CancellationException) {
    throw error
} catch (error: Throwable) {
    Result.failure(error)
}
