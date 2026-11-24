-- Base de datos independiente para AA5-EV01.
CREATE DATABASE IF NOT EXISTS auth_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE auth_db;

-- Estructura equivalente a la entidad Usuario del proyecto AA5.
-- Hibernate también puede crear/actualizar esta tabla con ddl-auto=update.
CREATE TABLE IF NOT EXISTS usuario (
    id_usuario INT NOT NULL AUTO_INCREMENT,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    documento VARCHAR(20) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    correo VARCHAR(150) NOT NULL,
    `contraseña` VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL DEFAULT 'USER',
    PRIMARY KEY (id_usuario),
    UNIQUE KEY uk_usuario_documento (documento),
    UNIQUE KEY uk_usuario_correo (correo)
) ENGINE=InnoDB;


-- Tablas adicionales requeridas por los módulos administrativos de CulturaTech.
CREATE TABLE IF NOT EXISTS categoria (
    id_categoria INT NOT NULL AUTO_INCREMENT,
    nombre_categoria VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500),
    PRIMARY KEY (id_categoria),
    UNIQUE KEY uk_categoria_nombre (nombre_categoria)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS lugar (
    id_lugar INT NOT NULL AUTO_INCREMENT,
    nombre_lugar VARCHAR(150) NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    localidad VARCHAR(100),
    telefono VARCHAR(30),
    latitud DECIMAL(10,7),
    longitud DECIMAL(10,7),
    pagina_web VARCHAR(255),
    PRIMARY KEY (id_lugar)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS evento (
    id_evento INT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(180) NOT NULL,
    descripcion VARCHAR(5000),
    fecha_hora DATETIME NOT NULL,
    costo DECIMAL(12,2) NOT NULL DEFAULT 0,
    imagen VARCHAR(1000),
    estado VARCHAR(30) NOT NULL DEFAULT 'ACTIVO',
    id_categoria INT NOT NULL,
    id_lugar INT NOT NULL,
    PRIMARY KEY (id_evento),
    CONSTRAINT fk_evento_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria),
    CONSTRAINT fk_evento_lugar FOREIGN KEY (id_lugar) REFERENCES lugar(id_lugar),
    INDEX idx_evento_fecha (fecha_hora)
) ENGINE=InnoDB;
