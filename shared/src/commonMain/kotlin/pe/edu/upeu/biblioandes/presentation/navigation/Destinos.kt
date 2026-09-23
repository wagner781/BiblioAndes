package pe.edu.upeu.biblioandes.presentation.navigation

sealed class Destino(val ruta: String, val titulo: String) {
    object Inicio : Destino("inicio", "Inicio")
    object Catalogo : Destino("catalogo", "Catálogo")
    object Prestamos : Destino("prestamos", "Préstamos")
    object Detalle : Destino("detalle/{libroId}", "Detalle") {
        fun crearRuta(libroId: Int) = "detalle/$libroId"
    }
}
