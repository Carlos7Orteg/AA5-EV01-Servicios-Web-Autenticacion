package com.culturatech.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Credenciales para actualizar la contraseña propia. */
public record CambioContrasenaRequest(
        @NotBlank String contrasenaActual,
        @NotBlank @Size(min = 8, max = 100) String nuevaContrasena,
        @NotBlank String confirmarContrasena) { }
