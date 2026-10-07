package pe.edu.upeu.biblioandes.data.local

import pe.edu.upeu.biblioandes.domain.model.EstadoPrestamo
import pe.edu.upeu.biblioandes.domain.model.Estudiante
import pe.edu.upeu.biblioandes.domain.model.Libro
import pe.edu.upeu.biblioandes.domain.model.Prestamo
import pe.edu.upeu.biblioandes.domain.util.sumarDias

object DatosSimulados {
    val estudiante = Estudiante(
        codigo = "E-2291",
        nombre = "Diego Huamán Ccama",
        carrera = "Ingeniería de Sistemas",
        correo = "diego.huaman@correo.pe"
    )

    val categorias = listOf("Programación", "Matemática", "Redes", "Gestión", "Literatura")

    val libros = listOf(
        Libro(1, "Kotlin en profundidad", "M. Salazar", 2023, "Programación", "Central", 3),
        Libro(2, "Estructuras de datos", "R. Peña", 2021, "Programación", "Central", 0),
        Libro(3, "Cálculo aplicado", "L. Ortega", 2019, "Matemática", "Sede Norte", 2),
        Libro(4, "Redes de computadoras", "A. Medina", 2022, "Redes", "Sede Sur", 4),
        Libro(5, "Seguridad en redes", "P. Ríos", 2024, "Redes", "Central", 0),
        Libro(6, "Gestión de proyectos", "S. Delgado", 2021, "Gestión", "Sede Norte", 2),
        Libro(7, "Bases de datos", "J. Pérez", 2020, "Programación", "Sede Sur", 5),
        Libro(8, "Álgebra lineal", "C. Gómez", 2018, "Matemática", "Central", 1),
        Libro(9, "Sistemas operativos", "L. Torvalds", 2022, "Programación", "Sede Norte", 3),
        Libro(10, "Don Quijote", "M. Cervantes", 2015, "Literatura", "Central", 2),
        Libro(11, "Cien años de soledad", "G. García", 2012, "Literatura", "Sede Sur", 1),
        Libro(12, "Marketing digital", "P. Kotler", 2023, "Gestión", "Central", 4)
    )

    fun crearPrestamos(fechaActual: String): List<Prestamo> = listOf(
        Prestamo(1, libros[0], sumarDias(fechaActual, -2), sumarDias(fechaActual, 5), EstadoPrestamo.Activo(5)),
        Prestamo(2, libros[3], sumarDias(fechaActual, -1), sumarDias(fechaActual, 6), EstadoPrestamo.Activo(6)),
        Prestamo(3, libros[2], sumarDias(fechaActual, -25), sumarDias(fechaActual, -18), EstadoPrestamo.Devuelto(sumarDias(fechaActual, -19))),
        Prestamo(4, libros[1], sumarDias(fechaActual, -35), sumarDias(fechaActual, -28), EstadoPrestamo.Devuelto(sumarDias(fechaActual, -29))),
        Prestamo(5, libros[5], sumarDias(fechaActual, -12), sumarDias(fechaActual, -5), EstadoPrestamo.Vencido(5))
    )
}
