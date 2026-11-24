# CulturaTech Bogotá — Documentación técnica GA7-AA5-EV03

## 1. Alcance

Esta ampliación conserva el proyecto Java 21, Spring Boot, Spring Web MVC, Spring Data JPA/Hibernate, MySQL y el frontend React + Vite existentes. Expone los servicios que consume el frontend para autenticación, perfil y administración. Las contraseñas se almacenan y verifican exclusivamente con BCrypt (`spring-security-crypto`). No se incorpora `spring-boot-starter-security`.

La persistencia se realiza en `auth_db`. El script `database/auth_db.sql` contiene las tablas `usuario`, `categoria`, `lugar` y `evento`. Hibernate mantiene la actualización del esquema de acuerdo con la configuración local existente.

## 2. Sesión y autorización

- `POST /api/auth/login` valida correo y contraseña y almacena `idUsuario` en la sesión HTTP (`JSESSIONID`).
- `GET /api/auth/perfil` devuelve los datos del usuario autenticado.
- `POST /api/auth/logout` invalida la sesión.
- Las operaciones de escritura administrativa y el CRUD de usuarios requieren una sesión con rol `ADMIN`.
- La consulta pública de eventos, categorías y lugares permanece disponible para el catálogo del frontend.
- El registro público siempre asigna el rol `USER`; el cliente no puede elevar sus privilegios enviando `rol: ADMIN`.
- Las respuestas nunca incluyen la contraseña.

En desarrollo, Vite reenvía `/api` a Spring Boot. `apiClient.js` utiliza `withCredentials: true` para enviar la cookie de sesión.

## 3. Endpoints

### 3.1 Autenticación y perfil

| Método | Ruta | Acceso | Resultado |
|---|---|---|---|
| POST | `/api/auth/registro` | Público | 201, registra usuario USER |
| POST | `/api/auth/login` | Público | 200, inicia sesión y devuelve usuario |
| POST | `/api/auth/logout` | Sesión | 204, cierra sesión |
| GET | `/api/auth/perfil` | Sesión | 200, consulta perfil |
| PUT | `/api/auth/perfil/datos` | Sesión | 200, actualiza datos personales |
| PUT | `/api/auth/perfil` | Sesión | 204, cambia contraseña tras verificar la actual |
| POST | `/api/auth/perfil/eliminar` | Sesión | 204, elimina cuenta y sesión |

Ejemplo de registro:

```json
{
  "nombres": "Ana",
  "apellidos": "Pérez",
  "documento": "1000000000",
  "fechaNacimiento": "2000-01-15",
  "correo": "ana@example.com",
  "contrasena": "ClaveSegura123!"
}
```

Ejemplo de cambio de contraseña:

```json
{
  "contrasenaActual": "ClaveAnterior123!",
  "nuevaContrasena": "ClaveNueva123!",
  "confirmarContrasena": "ClaveNueva123!"
}
```

### 3.2 Usuarios

| Método | Ruta | Acceso | Resultado |
|---|---|---|---|
| GET | `/api/usuarios` | ADMIN | 200, lista usuarios sin contraseñas |
| GET | `/api/usuarios/{id}` | ADMIN | 200, consulta usuario |
| POST | `/api/usuarios` | Público | 201, registra usuario USER |
| PUT | `/api/usuarios/{id}` | ADMIN | 200, actualiza usuario |
| DELETE | `/api/usuarios/{id}` | ADMIN | 204, elimina usuario |

El cuerpo de actualización conserva el formato usado por `AdminView.jsx`: objeto `usuario` y, cuando aplique, los campos de confirmación de contraseña. Al editar otra cuenta ADMIN se solicita la contraseña del administrador autenticado. La contraseña nunca se devuelve en las consultas.

### 3.3 Eventos

| Método | Ruta | Acceso | Resultado |
|---|---|---|---|
| GET | `/api/eventos` | Público | 200, lista eventos por fecha |
| GET | `/api/eventos/{id}` | Público | 200, consulta evento |
| POST | `/api/eventos` | ADMIN | 201, crea evento |
| PUT | `/api/eventos/{id}` | ADMIN | 200, actualiza evento |
| DELETE | `/api/eventos/{id}` | ADMIN | 204, elimina evento |

Ejemplo de cuerpo:

```json
{
  "titulo": "Festival cultural",
  "descripcion": "Encuentro cultural de la ciudad",
  "fechaHora": "2026-12-15T18:00:00",
  "costo": 0,
  "imagen": "/assets/images/evento.jpg",
  "estado": "ACTIVO",
  "categoria": { "idCategoria": 1 },
  "lugar": { "idLugar": 1 }
}
```

### 3.4 Categorías

| Método | Ruta | Acceso | Resultado |
|---|---|---|---|
| GET | `/api/categorias` | Público | 200, lista categorías |
| GET | `/api/categorias/{id}` | Público | 200, consulta categoría |
| POST | `/api/categorias` | ADMIN | 201, crea categoría |
| PUT | `/api/categorias/{id}` | ADMIN | 200, actualiza categoría |
| DELETE | `/api/categorias/{id}` | ADMIN | 204, elimina categoría si no tiene eventos asociados |

Cuerpo: `{"nombreCategoria":"Música","descripcion":"Eventos musicales"}`.

### 3.5 Lugares

| Método | Ruta | Acceso | Resultado |
|---|---|---|---|
| GET | `/api/lugares` | Público | 200, lista lugares |
| GET | `/api/lugares/{id}` | Público | 200, consulta lugar |
| POST | `/api/lugares` | ADMIN | 201, crea lugar |
| PUT | `/api/lugares/{id}` | ADMIN | 200, actualiza lugar |
| DELETE | `/api/lugares/{id}` | ADMIN | 204, elimina lugar si no tiene eventos asociados |

Cuerpo:

```json
{
  "nombreLugar": "Teatro Municipal",
  "direccion": "Carrera 10 # 20-30",
  "localidad": "Santa Fe",
  "telefono": "6010000000",
  "latitud": 4.6097,
  "longitud": -74.0817,
  "paginaWeb": "https://ejemplo.com"
}
```

## 4. Estados HTTP y validaciones

- `200 OK`: consultas y actualizaciones satisfactorias.
- `201 Created`: registros y creaciones.
- `204 No Content`: eliminaciones y cierre/cambio de contraseña.
- `400 Bad Request`: datos ausentes o inválidos.
- `401 Unauthorized`: sesión ausente o credenciales no válidas.
- `403 Forbidden`: usuario autenticado sin rol ADMIN.
- `404 Not Found`: recurso inexistente.
- `409 Conflict`: correo/documento/nombre duplicado o eliminación con relaciones existentes.

Las respuestas de error utilizan el campo JSON `mensaje`. Los datos se validan mediante Jakarta Bean Validation y las contraseñas mediante BCrypt.

## 5. Base de datos

Ejecute `database/auth_db.sql` en MySQL para crear las tablas requeridas. Si `auth_db` y `usuario` ya existen, las instrucciones `CREATE TABLE IF NOT EXISTS` conservan los registros existentes y agregan las tablas nuevas. No se requiere cambiar el archivo local `src/main/resources/application.properties`.

Para habilitar el panel administrativo, registre una cuenta y ejecute el script `database/crear_administrador.sql`, reemplazando el correo de ejemplo por el correo de esa cuenta. El script solo cambia el rol; no modifica la contraseña BCrypt.

## 6. Integración con React

Se conservan los servicios existentes en `frontend/src/api/`: `auth.service.js`, `users.service.js`, `events.service.js`, `categories.service.js` y `places.service.js`. El contrato de respuestas de login se alinea con `AuthContext.jsx`, que utiliza `idUsuario` y los datos públicos recibidos. Los favoritos y la agenda continúan en `localStorage`. Las notificaciones no forman parte del alcance de estos servicios.

## 7. Ejecución local

1. Mantenga el `application.properties` local con su configuración actual de MySQL.
2. Verifique que MySQL esté activo y que exista `auth_db`.
3. En la raíz del proyecto, ejecute `mvnw.cmd spring-boot:run`.
4. Para desarrollo React, desde `frontend`, ejecute `npm install` y `npm run dev`.
5. Para integrar React en Spring Boot, ejecute `npm run build`; Vite deposita el resultado en `src/main/resources/static` según `vite.config.js`.

La verificación funcional final de los endpoints debe realizarse en Postman, registrando las respuestas reales y los códigos HTTP antes de declarar las pruebas como aprobadas.
