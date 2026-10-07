package pe.edu.upeu.biblioandes.presentation.navigation

sealed class Destino(val ruta: String, val etiqueta: String, val simbolo: String) {
    data object Inicio : Destino("inicio", "Inicio", "I")
    data object Catalogo : Destino("catalogo", "Catálogo", "C")
    data object Prestamos : Destino("prestamos", "Préstamos", "P")
    data object Perfil : Destino("perfil", "Perfil", "U")
    data object Detalle : Destino("detalle/{libroId}", "Detalle", "D") {
        fun crearRuta(libroId: Int) = "detalle/$libroId"
    }

    companion object {
        val barraInferior = listOf(Inicio, Catalogo, Prestamos)
    }
}
