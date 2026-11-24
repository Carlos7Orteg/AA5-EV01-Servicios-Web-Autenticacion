package com.culturatech.auth.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Conserva el contrato anidado que envía AdminView.jsx. */
public record EventoRequest(
        @NotBlank @Size(max=180) String titulo,
        @Size(max=5000) String descripcion,
        @NotNull LocalDateTime fechaHora,
        @NotNull @PositiveOrZero BigDecimal costo,
        @Size(max=1000) String imagen,
        @NotBlank @Size(max=30) String estado,
        @NotNull @Valid RelacionCategoria categoria,
        @NotNull @Valid RelacionLugar lugar) {
    public record RelacionCategoria(@NotNull Integer idCategoria) { }
    public record RelacionLugar(@NotNull Integer idLugar) { }
}
