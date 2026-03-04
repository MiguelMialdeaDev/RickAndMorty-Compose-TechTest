# Rick and Morty App

  Aplicación Android que muestra información sobre los personajes de Rick and Morty usando la [API pública de Rick and Morty](https://rickandmortyapi.com/).

## Características

### Funcionalidades principales
  - Lista de personajes con nombre, imagen, estado y especie
  - Scroll infinito para cargar más personajes automáticamente
  - Buscador en tiempo real por nombre
  - Pantalla de detalle con toda la información del personaje:
    - Imagen, nombre, estado, especie y género
    - Ubicación actual y de origen
  - Soporte offline con caché local usando Room

  ---

## Arquitectura

  El proyecto sigue Clean Architecture 
  (en este caso sin modularización dado el tamaño del proyecto, si fuera a escalar o fuera mas grande si utilizaria distintos modulos) 
  con el patrón MVVM:

  app/
  ├── data/
  ├── di/
  ├── domain/
  └── ui/

### Por qué esta arquitectura

  He separado el código en tres capas bien diferenciadas:

  - **Data**: Gestiona las llamadas a la API y la base de datos local. Aquí vive todo lo relacionado con obtener y guardar datos.
  - **Domain**: La lógica de negocio pura, sin dependencias de Android. Los modelos y casos de uso que podrían reutilizarse en otras plataformas.
  - **UI**: Todo lo relacionado con la interfaz usando Jetpack Compose.

  Esta separación hace que el código sea más fácil de testear y mantener. Si mañana cambio de API o de base de datos, solo toco la capa de datos.

## Stack Tecnológico

### Principales librerías

  - Jetpack Compose con Material 3 para la UI
  - Hilt para inyección de dependencias
  - Retrofit + Gson para las llamadas a la API
  - Room para la base de datos local
  - Coil para cargar imágenes
  - Navigation Compose para la navegación

### Testing
  - JUnit 4
  - MockK para los mocks
  - Coroutines Test
  - Architecture Components Testing

### Requisitos
  - Android Studio Hedgehog o superior
  - JDK 11+
  - Un dispositivo o emulador con Android 7.0+

### Pasos

  1. Clonar el repositorio
  git clone https://github.com/MiguelMialdeaDev/RickAndMorty-Compose-TechTest.git

  2. Abrir en Android Studio y hacer sync de Gradle
  3. Ejecutar en un dispositivo/emulador
### Cómo funciona el caché

  La app guarda en Room los personajes que vas viendo. Si pierdes la conexión, te muestra los que ya tenías cacheados.

### Tests

  He añadido tests para las partes más críticas:
  - CharacterListViewModel: Testeo el estado, la paginación y la búsqueda
  - CharacterRepositoryImpl: Verifico que gestiona bien los datos de la API y el caché
  - UseCases: Tests simples para asegurar que la lógica funciona

  Uso MockK para mockear dependencias y Coroutines Test para las funciones suspend.

### Autor

  Miguel Mialdea
  GitHub: https://github.com/MiguelMialdeaDev