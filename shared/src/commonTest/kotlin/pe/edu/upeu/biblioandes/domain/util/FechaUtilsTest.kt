package pe.edu.upeu.biblioandes.domain.util

import kotlin.test.Test
import kotlin.test.assertEquals

class FechaUtilsTest {
    @Test
    fun suma_dias_respeta_cambios_de_mes_y_anio_bisiesto() {
        assertEquals("2024-02-29", sumarDias("2024-02-28", 1))
        assertEquals("2025-01-01", sumarDias("2024-12-31", 1))
    }

    @Test
    fun calcula_diferencia_positiva_y_negativa() {
        assertEquals(7, diasEntre("2026-09-30", "2026-10-07"))
        assertEquals(-5, diasEntre("2026-09-30", "2026-09-25"))
    }
}
