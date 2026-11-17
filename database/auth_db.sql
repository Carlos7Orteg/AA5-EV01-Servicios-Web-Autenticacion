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
