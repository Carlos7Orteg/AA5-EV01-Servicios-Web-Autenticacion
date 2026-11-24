-- Promueve a ADMIN una cuenta que ya esté registrada en auth_db.
-- Sustituya el correo por el de la cuenta existente. La contraseña permanece
-- protegida con el hash BCrypt que ya está almacenado para ese usuario.
USE auth_db;
UPDATE usuario
SET rol = 'ADMIN'
WHERE correo = 'correo-del-administrador@ejemplo.com';

-- Verifique el resultado:
SELECT id_usuario, nombres, apellidos, correo, rol
FROM usuario
WHERE correo = 'correo-del-administrador@ejemplo.com';
