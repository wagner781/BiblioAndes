package pe.edu.upeu.bibliomobil.domain.usecase

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ResultadoTest {
    @Test
    fun capturaFallosOrdinarios() = runTest {
        val resultado = resultadoDe<Int> { error("fallo") }
        assertEquals("fallo", resultado.exceptionOrNull()?.message)
    }

    @Test
    fun relanzaCancellationException() = runTest {
        assertFailsWith<CancellationException> {
            resultadoDe<Int> { throw CancellationException("cancelado") }
        }
    }
}
