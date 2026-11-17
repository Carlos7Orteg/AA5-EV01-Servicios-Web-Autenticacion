# AA5-EV01 - Servicio Web de Autenticación

Servicio web REST para el registro de usuarios y el inicio de sesión, desarrollado con Java 21, Spring Boot, Spring Data JPA, Maven y MySQL.

## Requisitos

- JDK 21.
- MySQL 8.
- Maven Wrapper incluido en el proyecto.

## Configuración

1. Crear la base de datos ejecutando `database/auth_db.sql` en MySQL Workbench.
2. Abrir `src/main/resources/application.properties`.
3. Configurar el usuario y la contraseña local de MySQL. No publicar credenciales reales.
4. Abrir el proyecto en NetBeans o una terminal ubicada en la carpeta raíz.

## Ejecutar

En Windows PowerShell:

```powershell
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```

La aplicación utiliza el puerto `8080`.

## Endpoints

| Método | Endpoint | Descripción | Respuesta esperada |
|---|---|---|---|
| POST | `/api/auth/registro` | Registra un usuario | 201 Created |
| POST | `/api/auth/login` | Inicia sesión | 200 OK |

### Registro

Solicitud `POST /api/auth/registro`:

```json
{
  "nombres": "Ana",
  "apellidos": "Pérez",
  "documento": "123456789",
  "fechaNacimiento": "1998-05-20",
  "correo": "ana@example.com",
  "contrasena": "ClaveSegura123"
}
```

Respuestas contempladas:
- `201 Created`: usuario registrado.
- `400 Bad Request`: datos obligatorios ausentes o inválidos.
- `409 Conflict`: correo o documento ya registrado.

Los usuarios nuevos reciben el rol `USER`. La contraseña se almacena mediante BCrypt.

### Inicio de sesión

Solicitud `POST /api/auth/login`:

```json
{
  "correo": "ana@example.com",
  "contrasena": "ClaveSegura123"
}
```

Respuestas contempladas:
- `200 OK`: credenciales correctas.
- `400 Bad Request`: datos ausentes o inválidos.
- `401 Unauthorized`: usuario inexistente o contraseña incorrecta.

La respuesta no expone la contraseña.

## Estructura principal

```text
src/main/java/com/culturatech/auth/
├── config/
│   └── PasswordConfig.java
├── controller/
│   └── AuthController.java
├── dto/
│   ├── AuthResponse.java
│   ├── LoginRequest.java
│   └── RegistroRequest.java
├── model/
│   └── Usuario.java
├── repository/
│   └── UsuarioRepository.java
├── service/
│   └── AuthService.java
└── AuthApplication.java
```

## Versionamiento

El proyecto queda preparado para su primer versionamiento. El commit inicial, la creación del repositorio remoto y la publicación en GitHub deben realizarse únicamente cuando el usuario lo autorice.

Antes de publicar, comprobar que `application.properties` no contenga credenciales reales.
