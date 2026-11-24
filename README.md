# CulturaTech Bogotá --- GA7-AA5-EV03

Aplicación web para la gestión y consulta de eventos culturales. Esta
entrega amplía el servicio de autenticación con una API REST para
administrar usuarios, eventos, categorías y lugares, e integra un
frontend desarrollado con React y Vite.

## 1. Tecnologías

-   Java 21
-   Spring Boot y Spring Web MVC
-   Maven y Maven Wrapper
-   Spring Data JPA / Hibernate
-   MySQL 8
-   React y Vite
-   Axios
-   BCrypt mediante `spring-security-crypto`
-   Jakarta Bean Validation

La aplicación utiliza sesiones HTTP (`JSESSIONID`) para mantener la
autenticación. No utiliza JWT ni incorpora
`spring-boot-starter-security`.

## 2. Funcionalidades

-   Registro e inicio/cierre de sesión.
-   Acceso diferenciado según el rol `USER` o `ADMIN`.
-   Consulta y actualización del perfil.
-   Cambio de contraseña y eliminación de la cuenta.
-   Consulta pública de eventos, categorías y lugares.
-   Administración de usuarios, eventos, categorías y lugares por parte
    del rol `ADMIN`.
-   Almacenamiento de contraseñas mediante BCrypt.
-   Favoritos y agenda gestionados en el frontend mediante
    `localStorage`.

Las notificaciones no forman parte del alcance de esta entrega.

## 3. Requisitos previos

Instalar y configurar:

-   JDK 21.
-   MySQL 8.
-   Node.js y npm.
-   Git (opcional, para clonar el repositorio).

## 4. Instalación y ejecución

### 4.1. Obtener el proyecto

Clonar el repositorio:

``` bash
git clone https://github.com/Carlos7Orteg/AA5-EV01-Servicios-Web-Autenticacion.git
cd AA5-EV01-Servicios-Web-Autenticacion
```

Para trabajar con esta entrega, cambiar a la rama:

``` bash
git checkout feature/AA5-EV03-servicios-web
```

### 4.2. Preparar la base de datos

1.  Iniciar el servicio de MySQL.
2.  Ejecutar el script `database/auth_db.sql` en MySQL para crear la
    base de datos y las tablas requeridas.
3.  Configurar la conexión local a MySQL en
    `src/main/resources/application.properties`.

El archivo `application.properties` contiene configuración propia del
entorno local y no debe sustituirse por el de otra instalación ni
publicarse con credenciales reales. Si no está presente al descargar el
repositorio, crear una configuración local compatible con la instalación
de MySQL.

### 4.3. Instalar las dependencias del frontend

Desde la carpeta raíz del proyecto:

``` bash
cd frontend
npm install
```

`npm install` descarga las dependencias definidas en `package.json` y
crea la carpeta `node_modules` localmente. **No se debe incluir
`node_modules` en el repositorio.**

Para ejecutar el frontend durante el desarrollo:

``` bash
npm run dev
```

Vite inicia el servidor de desarrollo y reenvía las solicitudes `/api`
al backend de Spring Boot, según la configuración del proyecto.

### 4.4. Ejecutar el backend

Desde la carpeta raíz:

``` bash
mvnw.cmd spring-boot:run
```

En Windows también se puede ejecutar:

``` powershell
.\mvnw.cmd spring-boot:run
```

El backend utiliza el puerto `8080`.

### 4.5. Generar el frontend para Spring Boot

Cuando se requiera servir React desde Spring Boot, ejecutar desde
`frontend`:

``` bash
npm run build
```

Vite genera los archivos compilados en `src/main/resources/static`, de
acuerdo con `frontend/vite.config.js`. Spring Boot sirve la aplicación
web desde esos recursos estáticos.

Para compilar el backend y generar el paquete:

``` powershell
.\mvnw.cmd clean package
```

## 5. Crear un usuario administrador

El registro público crea cuentas con rol `USER`. No se debe intentar
asignar el rol `ADMIN` desde el formulario ni enviando un campo
adicional en la petición de registro.

Para habilitar una cuenta administradora:

1.  Iniciar la aplicación y registrar normalmente un usuario desde la
    pantalla de registro.
2.  Abrir el archivo `database/crear_administrador.sql`.
3.  Reemplazar el correo de ejemplo del script por el correo exacto de
    la cuenta recién registrada.
4.  Ejecutar el script en la base de datos `auth_db`.
5.  Cerrar la sesión si estaba iniciada y volver a ingresar con esa
    cuenta para que la aplicación reconozca el rol actualizado.

El script cambia el rol de la cuenta existente. No crea el usuario ni
modifica su contraseña BCrypt.

## 6. API REST

**URL base local:** `http://localhost:8080`

Las operaciones protegidas utilizan la sesión HTTP creada al iniciar
sesión. El navegador o cliente REST debe conservar y enviar la cookie
`JSESSIONID`. Las operaciones administrativas requieren una sesión cuyo
usuario tenga rol `ADMIN`.

### 6.1. Autenticación y perfil

  ----------------------------------------------------------------------------------------
  Servicio          Método y endpoint                  Acceso            Descripción /
                                                                         historia de
                                                                         usuario
  ----------------- ---------------------------------- ----------------- -----------------
  Registro          `POST /api/auth/registro`          Público           Crear una cuenta
                                                                         de usuario
                                                                         (`USER`). HU:
                                                                         registro de
                                                                         nuevos usuarios.

  Inicio de sesión  `POST /api/auth/login`             Público           Validar
                                                                         credenciales e
                                                                         iniciar sesión.
                                                                         HU: autenticación
                                                                         de usuarios.

  Cierre de sesión  `POST /api/auth/logout`            Sesión            Invalidar la
                                                                         sesión activa.
                                                                         HU: cierre seguro
                                                                         de sesión.

  Consultar perfil  `GET /api/auth/perfil`             Sesión            Consultar los
                                                                         datos del usuario
                                                                         autenticado. HU:
                                                                         consulta del
                                                                         perfil.

  Actualizar perfil `PUT /api/auth/perfil/datos`       Sesión            Actualizar los
                                                                         datos personales.
                                                                         HU: edición del
                                                                         perfil.

  Cambiar           `PUT /api/auth/perfil`             Sesión            Verificar la
  contraseña                                                             contraseña actual
                                                                         y establecer una
                                                                         nueva. HU: cambio
                                                                         de contraseña.

  Eliminar cuenta   `POST /api/auth/perfil/eliminar`   Sesión            Eliminar la
                                                                         cuenta propia e
                                                                         invalidar la
                                                                         sesión. HU:
                                                                         eliminación de
                                                                         cuenta.
  ----------------------------------------------------------------------------------------

Ejemplo de cuerpo para registro:

``` json
{
  "nombres": "Ana",
  "apellidos": "Pérez",
  "documento": "1000000000",
  "fechaNacimiento": "2000-01-15",
  "correo": "ana@example.com",
  "contrasena": "ClaveSegura123!"
}
```

Ejemplo de cuerpo para cambio de contraseña:

``` json
{
  "contrasenaActual": "ClaveAnterior123!",
  "nuevaContrasena": "ClaveNueva123!",
  "confirmarContrasena": "ClaveNueva123!"
}
```

### 6.2. Usuarios

  -----------------------------------------------------------------------------------
  Servicio          Método y endpoint             Acceso            Descripción /
                                                                    historia de
                                                                    usuario
  ----------------- ----------------------------- ----------------- -----------------
  Registrar usuario `POST /api/usuarios`          Público           Registrar una
                                                                    cuenta con rol
                                                                    `USER`. HU:
                                                                    registro de
                                                                    usuarios.

  Listar usuarios   `GET /api/usuarios`           ADMIN             Consultar
                                                                    usuarios sin
                                                                    exponer
                                                                    contraseñas. HU:
                                                                    administración de
                                                                    usuarios.

  Consultar usuario `GET /api/usuarios/{id}`      ADMIN             Consultar un
                                                                    usuario por su
                                                                    identificador.
                                                                    HU:
                                                                    administración de
                                                                    usuarios.

  Actualizar        `PUT /api/usuarios/{id}`      ADMIN             Modificar los
  usuario                                                           datos de una
                                                                    cuenta. HU:
                                                                    administración de
                                                                    usuarios.

  Eliminar usuario  `DELETE /api/usuarios/{id}`   ADMIN             Eliminar una
                                                                    cuenta. HU:
                                                                    administración de
                                                                    usuarios.
  -----------------------------------------------------------------------------------

### 6.3. Eventos

  ----------------------------------------------------------------------------------
  Servicio          Método y endpoint            Acceso            Descripción /
                                                                   historia de
                                                                   usuario
  ----------------- ---------------------------- ----------------- -----------------
  Listar eventos    `GET /api/eventos`           Público           Consultar eventos
                                                                   ordenados por
                                                                   fecha. HU:
                                                                   explorar eventos
                                                                   culturales.

  Consultar evento  `GET /api/eventos/{id}`      Público           Consultar el
                                                                   detalle de un
                                                                   evento. HU: ver
                                                                   detalle del
                                                                   evento.

  Crear evento      `POST /api/eventos`          ADMIN             Registrar un
                                                                   evento. HU:
                                                                   administración de
                                                                   eventos.

  Actualizar evento `PUT /api/eventos/{id}`      ADMIN             Modificar un
                                                                   evento existente.
                                                                   HU:
                                                                   administración de
                                                                   eventos.

  Eliminar evento   `DELETE /api/eventos/{id}`   ADMIN             Eliminar un
                                                                   evento. HU:
                                                                   administración de
                                                                   eventos.
  ----------------------------------------------------------------------------------

Ejemplo de cuerpo para crear o actualizar un evento:

``` json
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

### 6.4. Categorías

  -------------------------------------------------------------------------------------
  Servicio          Método y endpoint               Acceso            Descripción /
                                                                      historia de
                                                                      usuario
  ----------------- ------------------------------- ----------------- -----------------
  Listar categorías `GET /api/categorias`           Público           Consultar las
                                                                      categorías
                                                                      disponibles. HU:
                                                                      explorar eventos
                                                                      por categoría.

  Consultar         `GET /api/categorias/{id}`      Público           Consultar una
  categoría                                                           categoría por
                                                                      identificador.
                                                                      HU: consulta de
                                                                      categorías.

  Crear categoría   `POST /api/categorias`          ADMIN             Registrar una
                                                                      categoría. HU:
                                                                      administración de
                                                                      categorías.

  Actualizar        `PUT /api/categorias/{id}`      ADMIN             Modificar una
  categoría                                                           categoría. HU:
                                                                      administración de
                                                                      categorías.

  Eliminar          `DELETE /api/categorias/{id}`   ADMIN             Eliminar una
  categoría                                                           categoría si no
                                                                      tiene eventos
                                                                      asociados. HU:
                                                                      administración de
                                                                      categorías.
  -------------------------------------------------------------------------------------

Ejemplo de cuerpo:

``` json
{
  "nombreCategoria": "Música",
  "descripcion": "Eventos musicales"
}
```

### 6.5. Lugares

  ----------------------------------------------------------------------------------
  Servicio          Método y endpoint            Acceso            Descripción /
                                                                   historia de
                                                                   usuario
  ----------------- ---------------------------- ----------------- -----------------
  Listar lugares    `GET /api/lugares`           Público           Consultar los
                                                                   lugares
                                                                   disponibles. HU:
                                                                   consultar lugares
                                                                   de eventos.

  Consultar lugar   `GET /api/lugares/{id}`      Público           Consultar un
                                                                   lugar por
                                                                   identificador.
                                                                   HU: detalle de
                                                                   lugar.

  Crear lugar       `POST /api/lugares`          ADMIN             Registrar un
                                                                   lugar. HU:
                                                                   administración de
                                                                   lugares.

  Actualizar lugar  `PUT /api/lugares/{id}`      ADMIN             Modificar un
                                                                   lugar. HU:
                                                                   administración de
                                                                   lugares.

  Eliminar lugar    `DELETE /api/lugares/{id}`   ADMIN             Eliminar un lugar
                                                                   si no tiene
                                                                   eventos
                                                                   asociados. HU:
                                                                   administración de
                                                                   lugares.
  ----------------------------------------------------------------------------------

Ejemplo de cuerpo:

``` json
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

### 6.6. Respuestas HTTP

  -----------------------------------------------------------------------
  Código                              Significado
  ----------------------------------- -----------------------------------
  `200 OK`                            Consulta o actualización
                                      satisfactoria.

  `201 Created`                       Registro o creación satisfactoria.

  `204 No Content`                    Operación completada sin contenido
                                      de respuesta, como eliminación o
                                      cierre de sesión.

  `400 Bad Request`                   Datos ausentes o inválidos.

  `401 Unauthorized`                  Credenciales no válidas o sesión no
                                      autenticada.

  `403 Forbidden`                     Usuario autenticado sin permisos de
                                      administrador.

  `404 Not Found`                     Recurso solicitado inexistente.

  `409 Conflict`                      Duplicidad o conflicto con
                                      relaciones existentes.
  -----------------------------------------------------------------------

Los errores de la API utilizan el campo JSON `mensaje`. Las contraseñas
no se incluyen en las respuestas. Las solicitudes que contienen datos se
validan mediante Jakarta Bean Validation.

## 7. Pruebas de servicios

Los endpoints pueden verificarse con Postman o un cliente REST
equivalente.

Para probar las rutas protegidas:

1.  Ejecutar `POST /api/auth/login` con las credenciales de una cuenta
    registrada.
2.  Conservar la cookie `JSESSIONID` que devuelve el servidor.
3.  Enviar las siguientes solicitudes utilizando esa misma sesión.
4.  Para las operaciones administrativas, iniciar sesión con una cuenta
    que tenga rol `ADMIN`.
5.  Comprobar el código HTTP y el cuerpo de respuesta de cada operación.

No se debe declarar una prueba como satisfactoria sin verificar la
respuesta real del servidor.

## 8. Estructura principal del proyecto

``` text
.
├── database/
│   ├── auth_db.sql
│   └── crear_administrador.sql
├── docs/
│   └── evidencias/
├── frontend/
│   ├── src/
│   │   ├── api/
│   │   ├── components/
│   │   ├── context/
│   │   └── views/
│   ├── package.json
│   └── vite.config.js
├── src/
│   └── main/
│       ├── java/com/culturatech/auth/
│       │   ├── config/
│       │   ├── controller/
│       │   ├── dto/
│       │   ├── model/
│       │   ├── repository/
│       │   └── service/
│       └── resources/
│           └── static/
├── DOCUMENTACION_TECNICA_GA7-AA5-EV03.md
├── ENDPOINTS_GA7-AA5-EV03.txt
├── pom.xml
└── README.md
```

## 9. Evidencias de la aplicación

### Acceso de administrador

![Acceso administrador](src/main/resources/static/assets/images/README/acceso-admin.png)

### Acceso de usuario

![Eventos usuario](src/main/resources/static/assets/images/README/eventos-usuario.png)

### Panel administrativo

![Panel administrativo de eventos](src/main/resources/static/assets/images/README/admin-eventos.png)

## 10. Consideraciones para Git

No incluir en el repositorio:

-   `frontend/node_modules/`: se genera localmente con `npm install`.
-   `target/`: se genera al compilar Maven.
-   Archivos locales con credenciales o configuraciones privadas,
    incluido `src/main/resources/application.properties`.

Para reconstruir el frontend en otra instalación, basta con ejecutar
`npm install` dentro de `frontend` y luego `npm run build`.

## 11. Autoría

**Proyecto:** CulturaTech Bogotá.   
**Autor:** Carlos Rodrigo Ortegón L.   
**Correo:** crortegon37@hotmail.com    
**Evidencia:**Diseño y desarrollo de servicios web GA7-AA5-EV03 — SENA.  
**Año:** 2026.