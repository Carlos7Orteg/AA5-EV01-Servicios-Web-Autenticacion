package com.culturatech.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record LugarRequest(
        @NotBlank @Size(max=150) String nombreLugar,
        @NotBlank @Size(max=255) String direccion,
        @Size(max=100) String localidad,
        @Size(max=30) String telefono,
        BigDecimal latitud,
        BigDecimal longitud,
        @Size(max=255) String paginaWeb) { }
