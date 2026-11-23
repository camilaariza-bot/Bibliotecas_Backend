# Biblioteca_Bckend 

API REST en **Java + Spring Boot** para gestionar una red de bibliotecas y consultar los libros asociados a cada una.

- **Regla del modelo:** una biblioteca puede tener varios libros, pero cada libro pertenece a una única biblioteca (relación **1 → N**).
- **Base de datos:** H2 en memoria, así que no hay que instalar nada adicional. Los datos de prueba se cargan solos al iniciar.

## Tecnologías

| Herramienta | Versión |
|---|---|
| Java | 17 o superior |
| Spring Boot | 3.3.5 (Web, Data JPA, Validation) |
| Base de datos | H2 (en memoria) |
| Build | Maven 3.9+ |

## Arquitectura por capas (estructura mínima obligatoria)

```
src/main/java/com/bibliotecas
├── BibliotecasBackendApplication.java   → clase principal
├── entidades/                           → ENTIDADES (JPA)
│   ├── Biblioteca.java                  → @OneToMany(mappedBy = "biblioteca")
│   └── Libro.java                       → @ManyToOne(optional = false) + @JoinColumn(biblioteca_id, nullable = false)
├── repositorios/                        → REPOSITORIOS (uno para cada entidad)
│   ├── BibliotecaRepository.java        → consulta las bibliotecas con sus libros (LEFT JOIN FETCH)
│   └── LibroRepository.java
├── servicios/                           → SERVICIOS
│   └── BibliotecaService.java           → registrar y consultar bibliotecas
├── controladores/                       → CONTROLADORES REST
│   └── BibliotecaController.java        → POST /bibliotecas y GET /bibliotecas
├── dto/                                 → respuesta JSON anidada sin referencias circulares
│   ├── BibliotecaRequestDTO.java
│   ├── BibliotecaResponseDTO.java
│   └── LibroDTO.java
└── config/
    ├── DataInitializer.java             → CommandLineRunner: carga los datos de prueba con los repositorios
    └── CorsConfig.java                  → permite peticiones desde el frontend (localhost:4200)
```

| Estructura mínima del enunciado | Implementación |
|---|---|
| Entidades (Biblioteca y Libro) | `entidades/Biblioteca.java`, `entidades/Libro.java` |
| Repositorios (uno para cada entidad) | `repositorios/BibliotecaRepository.java`, `repositorios/LibroRepository.java` |
| Servicios (mínimo uno de Biblioteca) | `servicios/BibliotecaService.java` |
| Controladores (mínimo uno REST de Biblioteca) | `controladores/BibliotecaController.java` |

Flujo de una petición: `Controlador → Servicio → Repositorio → Base de datos`.

### Modelo de datos

| Biblioteca | Libro |
|---|---|
| id, nombre, direccion, ciudad | id, titulo, autor, anio_publicacion |
| Relación 1 → N (`libros`) | `biblioteca_id`: referencia **obligatoria** a Biblioteca |

## Cómo ejecutar

Requisitos: **JDK 17 o superior** y **Maven** instalados.


1. Clonar el repositorio:
   ```bash
   git clone https://github.com/camilaariza-bot/bibliotecas-backend.git
   cd bibliotecas-backend
   ```
2. Ejecutar con Maven:
   ```bash
   mvn spring-boot:run
   ```
   En **VS Code**: abrir la carpeta `bibliotecas-backend`, instalar las extensiones recomendadas (*Extension Pack for Java* y *Spring Boot Extension Pack*) y ejecutar `BibliotecasBackendApplication` con el botón **Run**, o usar la terminal con `mvn spring-boot:run`.
3. La API queda disponible en **http://localhost:8080/bibliotecas**.

Para correr las pruebas: `mvn test`.

## Endpoints

| Método | Ruta | Acción |
|---|---|---|
| POST | `/bibliotecas` | Registrar biblioteca |
| GET | `/bibliotecas` | Consultar bibliotecas, incluyendo sus libros asociados |

### POST /bibliotecas

Cuerpo de la petición:

```json
{
  "nombre": "Biblioteca Occidente",
  "direccion": "Calle 13 # 68-20",
  "ciudad": "Bogotá"
}
```

Respuesta `201 Created`:

```json
{
  "id": 4,
  "nombre": "Biblioteca Occidente",
  "direccion": "Calle 13 # 68-20",
  "ciudad": "Bogotá",
  "libros": []
}
```

Si falta algún campo (o viene vacío) responde `400 Bad Request` gracias a las validaciones `@NotBlank` del DTO.

Con curl:

```bash
curl -X POST http://localhost:8080/bibliotecas \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Biblioteca Occidente","direccion":"Calle 13 # 68-20","ciudad":"Bogotá"}'
```

### GET /bibliotecas

Respuesta `200 OK` (JSON anidado, sin referencias circulares):

```json
[
  {
    "id": 1,
    "nombre": "Biblioteca Central",
    "direccion": "Calle 26 # 13-15",
    "ciudad": "Bogotá",
    "libros": [
      { "id": 1, "titulo": "Cien años de soledad", "autor": "Gabriel García Márquez", "anioPublicacion": 1967 },
      { "id": 2, "titulo": "María", "autor": "Jorge Isaacs", "anioPublicacion": 1867 }
    ]
  },
  {
    "id": 2,
    "nombre": "Biblioteca Norte",
    "direccion": "Carrera 15 # 127-30",
    "ciudad": "Bogotá",
    "libros": [
      { "id": 3, "titulo": "El coronel no tiene quien le escriba", "autor": "Gabriel García Márquez", "anioPublicacion": 1961 },
      { "id": 4, "titulo": "La vorágine", "autor": "José Eustasio Rivera", "anioPublicacion": 1924 }
    ]
  }
]
```

## Datos de prueba

`DataInitializer` (un `CommandLineRunner`) se ejecuta al arrancar y, usando los repositorios, guarda:

| Biblioteca | Libros |
|---|---|
| Biblioteca Central | Cien años de soledad (1967), María (1867) |
| Biblioteca Norte | El coronel no tiene quien le escriba (1961), La vorágine (1924) |
| Biblioteca Sur | Delirio (2004), El amor en los tiempos del cólera (1985) |

Primero se guardan las bibliotecas y luego cada libro con la referencia a su biblioteca (`biblioteca_id`).

## Consola de la base de datos

Mientras la aplicación corre: **http://localhost:8080/h2-console**

- JDBC URL: `jdbc:h2:mem:bibliotecasdb`
- Usuario: `sa` · Contraseña: *(vacía)*

Consulta útil para ver la relación:

```sql
SELECT l.titulo, l.autor, l.anio_publicacion, b.nombre AS biblioteca
FROM libro l JOIN biblioteca b ON b.id = l.biblioteca_id;
```

> Al ser una base en memoria, las bibliotecas registradas se pierden al detener la aplicación; al volver a iniciar se cargan de nuevo los datos de prueba.

## Alcance

Según el enunciado, `Libro` se implementa como entidad y repositorio. No se incluyen: edición, eliminación, autenticación, formulario de libros ni servicio/controlador REST para `Libro`.

## Frontend relacionado

La interfaz web está en el repositorio **bibliotecas-frontend** y consume esta API en `http://localhost:8080`.
