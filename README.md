# Servicio Web de Autenticación – GA7-AA5-EV01

## 1. Descripción del proyecto

Este proyecto desarrolla un servicio web de autenticación utilizando Spring Boot, Java y MySQL. Su propósito es permitir el registro de usuarios y la validación de credenciales mediante servicios REST.

El servicio implementa dos operaciones principales:

- **Registro:** recibe los datos de un usuario, verifica que no existan registros duplicados y almacena la contraseña cifrada.
- **Inicio de sesión:** consulta el usuario registrado y verifica la contraseña suministrada.

El desarrollo corresponde a la evidencia **GA7-AA5-EV01 – Diseño y codificación de un servicio web para registro e inicio de sesión**.

## 2. Tecnologías utilizadas

| Tecnología | Propósito |
|---|---|
| Java 21 | Lenguaje de programación. |
| Spring Boot | Construcción y ejecución del servicio web. |
| Spring Web MVC | Implementación de los endpoints REST. |
| Spring Data JPA | Acceso y persistencia de datos. |
| Hibernate | Implementación de JPA y mapeo objeto-relacional. |
| MySQL 8 | Sistema gestor de base de datos. |
| BCrypt | Cifrado seguro de contraseñas. |
| Maven | Gestión de dependencias y construcción del proyecto. |
| Git y GitHub | Control de versiones y alojamiento del repositorio. |

## 3. Arquitectura del proyecto

El servicio utiliza una arquitectura por capas para separar las responsabilidades y facilitar el mantenimiento del código.

```text
Cliente / Postman
       |
       v
AuthController
       |
       v
AuthService
       |
       v
UsuarioRepository
       |
       v
Base de datos MySQL
```

### Responsabilidad de cada capa

- **Controller:** recibe las solicitudes HTTP, valida los datos de entrada y devuelve las respuestas.
- **Service:** contiene la lógica de negocio para registrar usuarios y autenticar credenciales.
- **Repository:** realiza las operaciones de consulta y persistencia mediante Spring Data JPA.
- **Model:** representa la entidad que se almacena en la base de datos.
- **DTO:** define los datos que recibe y devuelve el servicio.
- **Config:** contiene la configuración necesaria para el cifrado de contraseñas.

## 4. Estructura del proyecto

```text
AA5-EV01 Servicios Web Autenticacion/
│
├── database/
│   └── auth_db.sql
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── culturatech/
│       │           └── auth/
│       │               ├── AuthApplication.java
│       │               │
│       │               ├── config/
│       │               │   └── PasswordConfig.java
│       │               │
│       │               ├── controller/
│       │               │   └── AuthController.java
│       │               │
│       │               ├── dto/
│       │               │   ├── AuthResponse.java
│       │               │   ├── LoginRequest.java
│       │               │   └── RegistroRequest.java
│       │               │
│       │               ├── model/
│       │               │   └── Usuario.java
│       │               │
│       │               ├── repository/
│       │               │   └── UsuarioRepository.java
│       │               │
│       │               └── service/
│       │                   └── AuthService.java
│       │
│       └── resources/
│           └── application.properties
│
├── .gitignore
├── pom.xml
└── README.md
```

## 5. Descripción de los componentes

### 5.1. Clase principal

**Archivo:** `AuthApplication.java`

Es el punto de entrada de la aplicación. Contiene el método `main` y utiliza `SpringApplication.run()` para iniciar el servicio Spring Boot.

### 5.2. Configuración de seguridad

**Archivo:** `config/PasswordConfig.java`

Define un bean `PasswordEncoder` que utiliza `BCryptPasswordEncoder`.

Su finalidad es cifrar las contraseñas antes de almacenarlas y permitir su posterior verificación sin guardar contraseñas en texto plano.

### 5.3. Controlador

**Archivo:** `controller/AuthController.java`

Expone los endpoints REST bajo la ruta `/api/auth`.

Sus responsabilidades son:

- Recibir solicitudes HTTP POST.
- Obtener los datos enviados en el cuerpo de la solicitud.
- Aplicar validaciones mediante `@Valid`.
- Delegar las operaciones al servicio de autenticación.
- Devolver los códigos HTTP correspondientes a las operaciones.

### 5.4. Servicio

**Archivo:** `service/AuthService.java`

Contiene la lógica de negocio del sistema.

Sus principales operaciones son:

- Registrar usuarios.
- Comprobar si el correo o documento ya están registrados.
- Normalizar los datos de registro.
- Cifrar la contraseña mediante BCrypt.
- Guardar el usuario en la base de datos.
- Buscar usuarios para validar el inicio de sesión.
- Comparar la contraseña recibida con la contraseña cifrada almacenada.

### 5.5. Repositorio

**Archivo:** `repository/UsuarioRepository.java`

Extiende `JpaRepository<Usuario, Integer>` para utilizar las operaciones de persistencia proporcionadas por Spring Data JPA.

Incluye consultas para:

- Buscar usuarios por correo.
- Comprobar si un correo ya existe.
- Comprobar si un documento ya existe.

### 5.6. Modelo

**Archivo:** `model/Usuario.java`

Representa la entidad Usuario y establece su correspondencia con la tabla `usuario` de MySQL mediante anotaciones JPA.

Contiene los atributos de identificación, nombres, apellidos, documento, fecha de nacimiento, correo, contraseña y rol.

Los campos de documento y correo tienen restricciones de unicidad.

### 5.7. Objetos de transferencia de datos (DTO)

| Archivo | Función |
|---|---|
| `RegistroRequest.java` | Recibe los datos necesarios para registrar un usuario y establece validaciones de entrada. |
| `LoginRequest.java` | Recibe el correo y la contraseña para iniciar sesión. |
| `AuthResponse.java` | Define la información pública que devuelve el servicio después de una operación satisfactoria, sin exponer la contraseña. |

Los DTO permiten separar los datos recibidos por los endpoints de la entidad utilizada para persistir información.

## 6. Base de datos

**Archivo:** `database/auth_db.sql`

Contiene el script SQL para crear la base de datos `auth_db` y la tabla `usuario`, junto con sus campos y restricciones de unicidad.

### Preparación

1. Iniciar el servidor MySQL.
2. Abrir un cliente de administración de MySQL.
3. Ejecutar el contenido de `database/auth_db.sql`.
4. Verificar que exista la base de datos `auth_db`.

La conexión se configura en:

`src/main/resources/application.properties`

Este archivo contiene los parámetros de conexión de la aplicación. Las credenciales locales deben mantenerse privadas y no deben publicarse en el repositorio.

## 7. Dependencias y configuración

**Archivo:** `pom.xml`

Define las dependencias y la configuración de construcción del proyecto Maven.

Entre sus dependencias principales se encuentran:

- `spring-boot-starter-webmvc`: soporte para controladores y servicios web.
- `spring-boot-starter-data-jpa`: persistencia mediante JPA.
- `spring-boot-starter-validation`: validación de datos de entrada.
- `spring-security-crypto`: funciones criptográficas utilizadas por BCrypt.
- `mysql-connector-j`: conexión con MySQL.
- Dependencias de pruebas de Spring.

Se utiliza la biblioteca criptográfica de Spring Security sin incorporar el módulo completo de seguridad de Spring Security.

## 8. Endpoints del servicio

La ruta base del servicio es:

```text
/api/auth
```

### 8.1. Registro de usuarios

| Propiedad | Valor |
|---|---|
| Método HTTP | POST |
| Endpoint | `/api/auth/registro` |
| Función | Registrar un usuario. |
| Respuesta satisfactoria | 201 Created |
| Respuesta por datos inválidos | 400 Bad Request |
| Respuesta por duplicidad | 409 Conflict |

Ejemplo de solicitud:

```json
{
  "nombres": "Juan",
  "apellidos": "Pérez",
  "documento": "123456789",
  "fechaNacimiento": "2000-01-15",
  "correo": "juan@example.com",
  "contrasena": "ClaveSegura123"
}
```

### 8.2. Inicio de sesión

| Propiedad | Valor |
|---|---|
| Método HTTP | POST |
| Endpoint | `/api/auth/login` |
| Función | Validar las credenciales del usuario. |
| Respuesta satisfactoria | 200 OK |
| Respuesta por credenciales incorrectas | 401 Unauthorized |

Ejemplo de solicitud:

```json
{
  "correo": "juan@example.com",
  "contrasena": "ClaveSegura123"
}
```

## 9. Flujo de funcionamiento

### 9.1. Registro

1. El cliente envía los datos del usuario al endpoint `/api/auth/registro`.
2. `AuthController` recibe la solicitud y aplica las validaciones.
3. `AuthService` comprueba si el correo o documento ya existen.
4. Si no hay duplicidad, cifra la contraseña con BCrypt.
5. El servicio solicita al repositorio guardar el usuario.
6. `UsuarioRepository` realiza la persistencia en MySQL.
7. El controlador devuelve la respuesta de registro.

### 9.2. Inicio de sesión

1. El cliente envía el correo y la contraseña al endpoint `/api/auth/login`.
2. `AuthController` recibe y valida la solicitud.
3. `AuthService` busca el usuario mediante el repositorio.
4. Se verifica la contraseña recibida con BCrypt.
5. Si las credenciales son correctas, se devuelve la información pública del usuario.
6. Si las credenciales no coinciden, se devuelve una respuesta de autenticación no autorizada.

## 10. Correspondencia con los requisitos de AA5-EV01

| Requisito de la guía | Implementación |
|---|---|
| Crear un servicio web de registro e inicio de sesión. | `AuthController.java` |
| Recibir datos del usuario. | `RegistroRequest.java` y `LoginRequest.java` |
| Registrar usuarios en MySQL. | `AuthService.java` y `UsuarioRepository.java` |
| Consultar usuarios para autenticar. | `UsuarioRepository.java` |
| Evitar usuarios duplicados. | `AuthService.java` |
| Cifrar contraseñas con BCrypt. | `PasswordConfig.java` y `AuthService.java` |
| Utilizar una arquitectura por capas. | Controller, Service, Repository y Model |
| Aplicar validaciones de entrada. | DTO y anotaciones de validación |
| Mantener código organizado y comentado. | Clases y métodos del proyecto |
| Utilizar herramientas de versionamiento. | Git y GitHub |

### Observaciones para la revisión de requisitos

La implementación actual presenta diferencias que deben tenerse en cuenta al contrastar los mensajes literales de la guía:

- Para credenciales incorrectas, el servicio devuelve actualmente el mensaje `Credenciales incorrectas`, mientras que la guía establece `Error en la autenticación`.
- Para registros duplicados, el servicio diferencia entre correo y documento duplicados, mientras que la guía presenta el mensaje general `El usuario ya existe`.
- Los DTO contienen validaciones y el controlador utiliza `@Valid`; el mensaje y la estructura exacta de la respuesta para datos vacíos o inválidos deben verificarse durante las pruebas.

Estas observaciones quedan identificadas para su revisión y no se presentan como resultados de pruebas ejecutadas.

## 11. Preparación de AA5-EV02 – Pruebas del servicio web

La evidencia AA5-EV02 corresponde a la comprobación del servicio desarrollado en AA5-EV01 mediante Postman.

En esta etapa se deberán preparar y ejecutar los siguientes escenarios:

| Caso | Operación | Resultado esperado según la guía |
|---|---|---|
| 1 | Registro exitoso | 201 – Usuario registrado correctamente |
| 2 | Registro duplicado | 409 – El usuario ya existe |
| 3 | Registro con datos vacíos | 400 – Bad Request |
| 4 | Inicio de sesión correcto | 200 – Autenticación satisfactoria |
| 5 | Contraseña incorrecta | 401 – Error en la autenticación |
| 6 | Usuario inexistente | 401 – Error en la autenticación |

El último caso debe utilizar una respuesta genérica para no revelar si el usuario existe o si la contraseña es incorrecta.

### Evidencias que se deberán recopilar en AA5-EV02

Para cada solicitud se deberá conservar:

- Método HTTP y URL del endpoint.
- Cuerpo JSON enviado.
- Código de estado HTTP recibido.
- Mensaje de respuesta del servicio.
- Captura de pantalla del resultado.

También se deberá preparar:

- Colección de solicitudes en Postman.
- Video que muestre la ejecución de las pruebas.
- Documento con las capturas y resultados.
- Archivo `ENDPOINT.txt` con las rutas del servicio.
- Proyecto comprimido y enlace al repositorio.

**Estado:** estos escenarios están documentados como actividades pendientes de AA5-EV02. No se presentan aquí como pruebas ejecutadas ni como resultados comprobados.

## 12. Ejecución del proyecto

### Requisitos previos

- JDK 21.
- MySQL 8.
- Maven o Maven Wrapper incluido en el proyecto.
- Base de datos `auth_db` creada.
- Parámetros de conexión configurados localmente en `application.properties`.

### Ejecución desde PowerShell

Ubicarse en la carpeta raíz del proyecto y ejecutar:

```powershell
.\mvnw.cmd spring-boot:run
```

La aplicación inicia el servidor web integrado de Spring Boot. La configuración actual utiliza el puerto 8080.

La dirección base local es:

```text
http://localhost:8080
```

Los endpoints disponibles son:

```text
POST http://localhost:8080/api/auth/registro
POST http://localhost:8080/api/auth/login
```

## 13. Control de versiones

El proyecto se administra mediante Git y se encuentra publicado en GitHub.

**Repositorio:**

https://github.com/Carlos7Orteg/AA5-EV01-Servicios-Web-Autenticacion

**Rama principal:** `main`

### Historial de versionamiento

| Commit | Descripción |
|---|---|
| `a380b64` | Inicializa servicio web de autenticación. |
| `5a1a310` | Agrega configuración de conexión a MySQL. |
| Pendiente | Documentación técnica del proyecto y preparación de AA5-EV02. |

El tercer commit corresponde a la actualización documental de este README.

### Procedimiento de versionamiento

Para revisar y publicar los cambios documentales:

```powershell
git status
git diff -- README.md
git add README.md
git commit -m "docs: documentar servicio web de autenticacion AA5-EV01"
git push origin main
```

Este procedimiento permite identificar los cambios, revisar el contenido antes de confirmarlo y publicarlo en el repositorio remoto.

## 14. Alcance y continuidad del proyecto

AA5-EV01 comprende el diseño, desarrollo y organización del servicio web de autenticación, su conexión con MySQL, la protección de contraseñas y el control de versiones.

AA5-EV02 continúa sobre el mismo proyecto y se enfoca en comprobar los endpoints mediante Postman, recopilar evidencias y documentar los resultados.

Por lo tanto, no se requiere crear otro servicio web para AA5-EV02: se utilizará la implementación desarrollada en esta evidencia.

---

**Proyecto:** Servicio Web de Autenticación
**Autor:** Carlos Rodrigo Ortegón L.  
**Evidencia:** GA7-AA5-EV01 SENA
**Tecnologías principales:** Java 21, Spring Boot, MySQL, JPA y BCrypt  
**Repositorio:** https://github.com/Carlos7Orteg/AA5-EV01-Servicios-Web-Autenticacion
