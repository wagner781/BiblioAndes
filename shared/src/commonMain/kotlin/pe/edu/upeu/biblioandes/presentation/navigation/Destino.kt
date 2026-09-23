package pe.edu.upeu.biblioandes.presentation.navigation

sealed class Destino(val ruta: String) {
    object Inicio : Destino("inicio")
    object Catalogo : Destino("catalogo")
    object Prestamos : Destino("prestamos")
    object Perfil : Destino("perfil")
    object Detalle : Destino("detalle/{libroId}") {
        fun crearRuta(libroId: Int) = "detalle/$libroId"
    }
}
