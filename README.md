# BiblioAndes

Este es un proyecto de aplicación móvil multiplataforma desarrollado con Kotlin Multiplatform (KMP), dirigido a Android e iOS. La aplicación permite la gestión de una biblioteca universitaria, con catálogos de libros y seguimiento de préstamos.

## Estructura de Paquetes

El proyecto sigue una organización clara orientada a separar las responsabilidades, ubicada en el módulo `shared/src/commonMain/kotlin/pe/edu/upeu/biblioandes/`:

- **`data`**: Capa de datos. Contiene la lógica de acceso a datos locales (`local/`) y los repositorios de datos (`repository/`).
- **`domain`**: Capa de dominio. Incluye las entidades principales de la aplicación (`model/`), las interfaces de los repositorios (`repository/`) y los casos de uso (`usecase/`), siendo independiente de otras capas.
- **`presentation`**: Capa de presentación. Agrupa los ViewModels y estados de interfaz (UI State) organizados por características (`catalogo`, `detalle`, `perfil`, `prestamos`).
- **`ui`**: Capa de interfaz de usuario. Contiene las pantallas desarrolladas en Compose Multiplatform (`catalogo`, `detalle`, `prestamos`) y la lógica de navegación (`navigation`).
- **`di`**: Capa de inyección de dependencias.

## Decisiones de Arquitectura (Clean y MVVM)

El proyecto ha sido diseñado bajo los principios de **Clean Architecture** y el patrón **MVVM** (Model-View-ViewModel):

1.  **Clean Architecture**: Se ha dividido el proyecto en capas de Dominio, Datos y Presentación. La lógica de negocio está completamente aislada en la capa `domain`, lo que permite testearla y modificarla sin afectar la interfaz gráfica ni los orígenes de datos.
2.  **MVVM**: En la capa `presentation`, cada pantalla cuenta con su propio `ViewModel` (por ejemplo, `CatalogoViewModel`) encargado de gestionar el estado de la vista (`CatalogoUiState`). Los ViewModels interactúan con los Casos de Uso del dominio para obtener los datos y exponerlos de forma reactiva hacia la UI (Compose).

## Descripción de Datos Simulados

Dado que el proyecto se encuentra en una fase inicial/demostrativa, se utilizan datos locales estáticos ubicados en `DatosSimulados.kt`. Estos incluyen:

- **Estudiante**: Información de perfil del usuario (nombre, carrera, correo).
- **Categorías**: Lista de géneros o áreas de estudio (Programación, Matemática, Redes, etc.).
- **Libros**: Catálogo con diferentes ejemplares, incluyendo título, autor, año, categoría, sede y stock disponible.
- **Préstamos**: Registros de libros prestados con fechas de solicitud/devolución y diferentes estados (`Activo`, `Devuelto`, `Vencido`).

## Instrucciones de ejecución en Android

Para ejecutar la aplicación en un dispositivo o emulador Android:

1.  Abre el proyecto en Android Studio (o IntelliJ IDEA).
2.  Utiliza la configuración de ejecución proporcionada por el IDE seleccionando `androidApp`.
3.  Alternativamente, desde la terminal puedes ejecutar:
    ```bash
    ./gradlew :androidApp:assembleDebug
    ```

## Instrucciones de ejecución en iOS

Para ejecutar la aplicación en iOS (se requiere una Mac):

1.  Abre el directorio `iosApp` en Xcode.
2.  Selecciona el simulador deseado (ej. iPhone 15) o un dispositivo físico.
3.  Presiona el botón de **Run** (o usa el atajo `Cmd + R`) directamente desde Xcode.
4.  _(Opcional)_ Si cuentas con el entorno configurado, también puedes ejecutar desde Fleet o mediante el plugin de KMP en Android Studio.

---

_Para aprender más sobre [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html), consulta la documentación oficial._

## Evidencias

- [Documento editable Word](docs/evidencias/Evidencias_Examen_BiblioAndes.docx.pdf)
