package pe.edu.upeu.bibliomobil.domain.model

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LibroTest {
    @Test
    fun rechazaTituloVacio() {
        assertFailsWith<IllegalArgumentException> {
            Libro(0, "   ", "Autor", 2026, 3)
        }
    }

    @Test
    fun rechazaAnioFueraDeRango() {
        assertFailsWith<IllegalArgumentException> {
            Libro(0, "Título", "Autor", 1449, 3)
        }
    }

    @Test
    fun identificaSiRequiereReposicion() {
        assertTrue(Libro(0, "Título", "Autor", 2026, 2).requiereReposicion)
        assertFalse(Libro(0, "Título", "Autor", 2026, 3).requiereReposicion)
    }
}
