package pe.edu.upeu.biblioandes.domain.util

private data class Fecha(val anio: Int, val mes: Int, val dia: Int)

fun sumarDias(fechaIso: String, dias: Int): String =
    desdeDiaEpoch(haciaDiaEpoch(parsear(fechaIso)) + dias).toIso()

fun diasEntre(desdeIso: String, hastaIso: String): Int =
    (haciaDiaEpoch(parsear(hastaIso)) - haciaDiaEpoch(parsear(desdeIso))).toInt()

private fun parsear(valor: String): Fecha {
    require(valor.length == 10 && valor[4] == '-' && valor[7] == '-') {
        "La fecha debe usar el formato yyyy-MM-dd"
    }
    return Fecha(
        anio = valor.substring(0, 4).toInt(),
        mes = valor.substring(5, 7).toInt(),
        dia = valor.substring(8, 10).toInt()
    )
}

private fun Fecha.toIso(): String =
    "${anio.toString().padStart(4, '0')}-${mes.toString().padStart(2, '0')}-${dia.toString().padStart(2, '0')}"

// Conversión gregoriana basada en días civiles; funciona igual en Android e iOS.
private fun haciaDiaEpoch(fecha: Fecha): Long {
    var y = fecha.anio
    val m = fecha.mes
    y -= if (m <= 2) 1 else 0
    val era = if (y >= 0) y / 400 else (y - 399) / 400
    val yoe = y - era * 400
    val mp = m + if (m > 2) -3 else 9
    val doy = (153 * mp + 2) / 5 + fecha.dia - 1
    val doe = yoe * 365 + yoe / 4 - yoe / 100 + doy
    return era.toLong() * 146097L + doe - 719468L
}

private fun desdeDiaEpoch(diaEpoch: Long): Fecha {
    val z = diaEpoch + 719468L
    val era = if (z >= 0) z / 146097L else (z - 146096L) / 146097L
    val doe = z - era * 146097L
    val yoe = (doe - doe / 1460L + doe / 36524L - doe / 146096L) / 365L
    var y = yoe.toInt() + era.toInt() * 400
    val doy = doe - (365L * yoe + yoe / 4L - yoe / 100L)
    val mp = (5L * doy + 2L) / 153L
    val d = (doy - (153L * mp + 2L) / 5L + 1L).toInt()
    val m = (mp + if (mp < 10) 3 else -9).toInt()
    y += if (m <= 2) 1 else 0
    return Fecha(y, m, d)
}
