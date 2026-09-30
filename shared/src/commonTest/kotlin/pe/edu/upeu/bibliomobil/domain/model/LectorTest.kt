package pe.edu.upeu.bibliomobil.domain.model

import kotlin.test.Test
import kotlin.test.assertFailsWith

class LectorTest {
    @Test
    fun rechazaNombreVacio() {
        assertFailsWith<IllegalArgumentException> {
            Lector(0, " ", "lector@correo.pe", null)
        }
    }

    @Test
    fun rechazaCorreoVacio() {
        assertFailsWith<IllegalArgumentException> {
            Lector(0, "Lector", " ", null)
        }
    }

    @Test
    fun rechazaTelefonoVacioNoNulo() {
        assertFailsWith<IllegalArgumentException> {
            Lector(0, "Lector", "lector@correo.pe", " ")
        }
    }
}
