package com.culturatech.auth.controller;

import com.culturatech.auth.dto.AuthResponse;
import com.culturatech.auth.dto.LoginRequest;
import com.culturatech.auth.dto.RegistroRequest;
import com.culturatech.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

/**
 * Expone los endpoints REST para registrar usuarios e iniciar sesión.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Crea el controlador con el servicio de autenticación.
     *
     * @param authService servicio que ejecuta la lógica de autenticación
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Registra un usuario nuevo.
     *
     * @param request datos del usuario recibidos en el cuerpo JSON
     * @return respuesta pública del usuario y estado HTTP 201
     */
    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registrar(
            @Valid @RequestBody RegistroRequest request) {
        AuthResponse respuesta = authService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    /**
     * Comprueba las credenciales de acceso.
     *
     * @param request correo y contraseña recibidos en el cuerpo JSON
     * @return respuesta pública del usuario y estado HTTP 200
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> iniciarSesion(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse respuesta = authService.iniciarSesion(request);
        return ResponseEntity.ok(respuesta);
    }
    
        @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> manejarError(
            ResponseStatusException ex) {

        return ResponseEntity.status(ex.getStatusCode())
                .body(Map.of("mensaje", ex.getReason()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidacion(
            MethodArgumentNotValidException ex) {

        return ResponseEntity.badRequest()
                .body(Map.of("mensaje", "Bad Request"));
    }
}
