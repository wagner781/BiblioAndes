package pe.edu.upeu.biblioandes

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform