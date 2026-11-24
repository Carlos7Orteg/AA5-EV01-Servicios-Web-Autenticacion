package com.culturatech.auth.dto;

import com.culturatech.auth.model.Usuario;
import java.time.LocalDate;

/** Datos públicos de usuario; nunca incluye la contraseña. */
public record UsuarioResponse(
        Integer idUsuario,
        String nombres,
        String apellidos,
        String documento,
        LocalDate fechaNacimiento,
        String correo,
        String rol) {
    public static UsuarioResponse desde(Usuario usuario) {
        return new UsuarioResponse(usuario.getIdUsuario(), usuario.getNombres(),
                usuario.getApellidos(), usuario.getDocumento(), usuario.getFechaNacimiento(),
                usuario.getCorreo(), usuario.getRol().name());
    }
}
