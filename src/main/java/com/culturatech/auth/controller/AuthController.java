package com.culturatech.auth.controller;

import com.culturatech.auth.dto.*;
import com.culturatech.auth.model.Usuario;
import com.culturatech.auth.service.AuthService;
import com.culturatech.auth.service.SesionService;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST encargado del registro, inicio y cierre de sesión,
 * consulta y actualización del perfil y eliminación de cuentas.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final SesionService sesionService;

    /**
     * Inyecta los servicios necesarios para autenticación y gestión de sesiones.
     */
    public AuthController(AuthService authService, SesionService sesionService) {
        this.authService = authService;
        this.sesionService = sesionService;
    }

    /**
     * Registra un nuevo usuario y devuelve HTTP 201 cuando la operación es exitosa.
     */
    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(request));
    }

    /**
     * Valida las credenciales, invalida una sesión anterior y crea una nueva
     * sesión autenticada, almacenando el identificador del usuario.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> iniciarSesion(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        AuthResponse respuesta = authService.iniciarSesion(request);
        HttpSession sesionAnterior = httpRequest.getSession(false);
        if (sesionAnterior != null) sesionAnterior.invalidate();
        HttpSession nuevaSesion = httpRequest.getSession(true);
        nuevaSesion.setAttribute(SesionService.USUARIO_ID, respuesta.getIdUsuario());
        return ResponseEntity.ok(respuesta);
    }

    /**
     * Cierra la sesión actual e invalida sus datos.
     * Devuelve HTTP 204 cuando finaliza la operación.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> cerrarSesion(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) session.invalidate();
        return ResponseEntity.noContent().build();
    }

    /**
     * Consulta los datos del perfil del usuario autenticado.
     */
    @GetMapping("/perfil")
    public UsuarioResponse obtenerPerfil(HttpSession session) {
        return UsuarioResponse.desde(sesionService.usuarioActual(session));
    }

    /**
     * Actualiza los datos personales del usuario autenticado.
     */
    @PutMapping("/perfil/datos")
    public UsuarioResponse actualizarDatos(@Valid @RequestBody PerfilRequest request, HttpSession session) {
        Usuario actual = sesionService.usuarioActual(session);
        return UsuarioResponse.desde(authService.actualizarPerfil(actual.getIdUsuario(), request));
    }

    /**
     * Cambia la contraseña del usuario autenticado.
     */
    @PutMapping("/perfil")
    public ResponseEntity<Void> cambiarContrasena(@Valid @RequestBody CambioContrasenaRequest request, HttpSession session) {
        Usuario actual = sesionService.usuarioActual(session);
        authService.cambiarContrasena(actual.getIdUsuario(), request);
        return ResponseEntity.noContent().build();
    }

    /**
     * Elimina la cuenta del usuario autenticado y cierra su sesión.
     */
    @PostMapping("/perfil/eliminar")
    public ResponseEntity<Void> eliminarCuenta(HttpSession session) {
        Usuario actual = sesionService.usuarioActual(session);
        authService.eliminar(actual.getIdUsuario());
        session.invalidate();
        return ResponseEntity.noContent().build();
    }
}
