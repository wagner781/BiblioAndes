package pe.edu.upeu.biblioandes.data.local

import pe.edu.upeu.biblioandes.domain.model.*

object DatosSimulados {
 val estudiante = Estudiante("E-2291", "Diego Huamán Ccama", "Ingeniería de Sistemas", "diego.huaman@correo.pe")
 val categorias = listOf("Programación", "Matemática", "Redes", "Gestión", "Literatura")
 val libros = listOf(
 Libro(1, "Kotlin en profundidad", "M. Salazar", 2023, "Programación", "Central", 3),
 Libro(2, "Estructuras de datos", "R. Peña", 2021, "Programación", "Central", 0),
 Libro(3, "Cálculo aplicado", "L. Ortega", 2019, "Matemática", "Sede Norte", 2),
 Libro(4, "Redes de computadoras", "A. Medina", 2022, "Redes", "Sede Sur", 4),
 Libro(5, "Seguridad en redes", "P. Ríos", 2024, "Redes", "Central", 0),
 Libro(6, "Gestión de proyectos", "S. Delgado", 2021, "Gestión", "Sede Norte", 2),
 Libro(7, "Clean Architecture", "R. Martin", 2017, "Programación", "Sede Norte", 1),
 Libro(8, "Física I", "M. Alonso", 2018, "Matemática", "Central", 5),
 Libro(9, "Administración de Empresas", "I. Chiavenato", 2020, "Gestión", "Sede Sur", 2),
 Libro(10, "Cien años de soledad", "G. García Márquez", 1967, "Literatura", "Central", 1),
 Libro(11, "El Aleph", "J.L. Borges", 1949, "Literatura", "Sede Norte", 0),
 Libro(12, "Sistemas Operativos", "A. Silberschatz", 2022, "Programación", "Central", 4)
 )
 val prestamos = listOf(
 Prestamo(1, libros[0], "2026-09-14", "2026-09-21", EstadoPrestamo.Activo(5)),
 Prestamo(2, libros[3], "2026-09-15", "2026-09-22", EstadoPrestamo.Activo(6)),
 Prestamo(3, libros[2], "2026-08-20", "2026-08-27", EstadoPrestamo.Devuelto("2026-08-26")),
 Prestamo(4, libros[1], "2026-08-05", "2026-08-12", EstadoPrestamo.Devuelto("2026-08-11")),
 Prestamo(5, libros[5], "2026-08-28", "2026-09-04", EstadoPrestamo.Vencido(18))
 )
}
