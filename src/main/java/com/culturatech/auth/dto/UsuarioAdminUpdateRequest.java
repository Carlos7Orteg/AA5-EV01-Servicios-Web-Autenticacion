package com.culturatech.auth.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/** Estructura compatible con el formulario administrativo React existente. */
public record UsuarioAdminUpdateRequest(
        @NotNull @Valid DatosUsuario usuario,
        String contrasenaSeguridad,
        String contrasenaActual,
        String nuevaContrasena,
        String confirmarContrasena) {
    public record DatosUsuario(
            @NotBlank @Size(max=100) String nombres,
            @NotBlank @Size(max=100) String apellidos,
            @NotBlank @Size(max=20) String documento,
            @NotNull LocalDate fechaNacimiento,
            @NotBlank @Email @Size(max=150) String correo,
            String contrasena,
            String rol) { }
}
