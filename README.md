# BiblioAndes

Aplicación Kotlin Multiplatform para consultar el catálogo de una biblioteca, solicitar préstamos y revisar fechas de devolución. Android e iOS comparten dominio, datos, ViewModels y UI con Compose Multiplatform.

## Alcance de esta versión

- Inicio con saludo, préstamo de vencimiento más próximo y accesos rápidos.
- Catálogo de 12 libros con búsqueda sin distinción de mayúsculas ni tildes y filtro por categoría.
- Detalle del libro y confirmación de solicitud.
- Préstamos ordenados por fecha límite y filtrados por estado.
- Perfil del estudiante y tema claro/oscuro inmediato.
- Navegación inferior con exactamente Inicio, Catálogo y Préstamos; Perfil se abre desde la barra superior.
- Datos exclusivamente en memoria, sin servicios web ni base de datos.

## Arquitectura

El código compartido se encuentra en `shared/src/commonMain/kotlin/pe/edu/upeu/biblioandes`:

```text
data/
  local/          Datos simulados y fecha por plataforma
  repository/     Implementación en memoria
domain/
  model/          Libro, Estudiante, Prestamo y EstadoPrestamo
  repository/     Contrato intercambiable
  usecase/        Casos de uso y reglas de negocio
  util/           Cálculos de fechas
presentation/
  inicio/
  catalogo/
  detalle/
  prestamos/
  perfil/
  navigation/
  theme/
di/               Módulo común de Koin
```

La UI depende de casos de uso; los casos de uso dependen de `BibliotecaRepository`. Para reemplazar los datos simulados por otra fuente se crea una nueva implementación del contrato y se cambia su registro en `AppModule`, sin modificar ViewModels ni pantallas.

## Reglas de negocio

- RN-01: un estudiante puede tener como máximo tres préstamos activos.
- RN-02: no se puede solicitar un libro sin ejemplares disponibles.
- RN-03: un préstamo nuevo dura siete días; los préstamos activos se recalculan con la fecha del dispositivo.
- RN-04: cualquier préstamo vencido bloquea una nueva solicitud.

`BibliotecaRepositoryFake` mantiene una copia mutable de los libros y préstamos. Al registrar una solicitud reduce una existencia y guarda el nuevo préstamo bajo exclusión mutua. El catálogo incluye un indicador `simularErrorCatalogo` para probar su estado de error.

## Ejecución

Requisitos: JDK 11 o superior, Android SDK configurado y, para iOS, macOS con Xcode.

```bash
# Compilar APK Android
./gradlew :androidApp:assembleDebug

# Ejecutar las pruebas compartidas disponibles en el host
./gradlew :shared:allTests
```

Para iOS, abrir `iosApp/iosApp.xcodeproj` en Xcode y ejecutar el esquema `iosApp`. Koin se inicializa desde `BiblioAndesApplication` en Android y desde `MainViewController` en iOS.

## Pruebas

Las pruebas de `shared/src/commonTest` cubren:

- cálculo de fechas y transición a vencido;
- persistencia en memoria y reducción de existencias;
- error simulado del catálogo;
- RN-01, RN-02, RN-03 y RN-04;
- búsqueda sin tildes, filtros de préstamos y actualización del detalle.

Las pruebas de simulador iOS se compilan en Windows, pero solo se ejecutan en macOS.
