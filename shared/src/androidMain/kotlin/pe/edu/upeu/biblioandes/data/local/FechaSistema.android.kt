package pe.edu.upeu.biblioandes.data.local

import java.time.LocalDate

actual fun fechaActualIso(): String = LocalDate.now().toString()
