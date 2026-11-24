package com.culturatech.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequest(
        @NotBlank @Size(max=100) String nombreCategoria,
        @Size(max=500) String descripcion) { }
