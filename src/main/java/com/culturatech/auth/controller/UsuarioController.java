package com.culturatech.auth.controller;

import com.culturatech.auth.dto.*;
import com.culturatech.auth.model.Usuario;
import com.culturatech.auth.repository.UsuarioRepository;
import com.culturatech.auth.service.AuthService;
import com.culturatech.auth.service.SesionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/** CRUD administrativo de usuarios y registro público compatible con React. */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioRepository repo;
    private final AuthService authService;
    private final SesionService sesionService;
    private final PasswordEncoder passwordEncoder;
    public UsuarioController(UsuarioRepository repo, AuthService authService, SesionService sesionService, PasswordEncoder passwordEncoder) {
        this.repo = repo; this.authService = authService; this.sesionService = sesionService; this.passwordEncoder = passwordEncoder;
    }

    /** Registro público: el rol siempre se asigna como USER. */
    @PostMapping
    public ResponseEntity<AuthResponse> crear(@Valid @RequestBody RegistroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(request));
    }

    @GetMapping
    public List<UsuarioResponse> listar(HttpSession session) {
        sesionService.administrador(session);
        return repo.findAll().stream().map(UsuarioResponse::desde).toList();
    }

    @GetMapping("/{id}")
    public UsuarioResponse obtener(@PathVariable Integer id, HttpSession session) {
        sesionService.administrador(session);
        return UsuarioResponse.desde(authService.obtener(id));
    }

    @PutMapping("/{id}")
    @Transactional
    public UsuarioResponse actualizar(@PathVariable Integer id, @Valid @RequestBody UsuarioAdminUpdateRequest request, HttpSession session) {
        Usuario administrador = sesionService.administrador(session);
        Usuario objetivo = authService.obtener(id);
        UsuarioAdminUpdateRequest.DatosUsuario datos = request.usuario();
        boolean esPropio = administrador.getIdUsuario().equals(id);

        if (!esPropio && objetivo.getRol() == Usuario.Rol.ADMIN) {
            if (request.contrasenaSeguridad() == null || !passwordEncoder.matches(request.contrasenaSeguridad(), administrador.getContrasena())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "La contraseña del administrador no es correcta");
            }
        }
        if (esPropio && request.nuevaContrasena() != null && !request.nuevaContrasena().isBlank()) {
            if (request.contrasenaActual() == null || !passwordEncoder.matches(request.contrasenaActual(), administrador.getContrasena())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "La contraseña actual no es correcta");
            }
            if (!request.nuevaContrasena().equals(request.confirmarContrasena())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La confirmación de contraseña no coincide");
            }
            objetivo.setContrasena(passwordEncoder.encode(request.nuevaContrasena()));
        }

        String correo = datos.correo().trim().toLowerCase();
        String documento = datos.documento().trim();
        if (repo.existsByCorreoAndIdUsuarioNot(correo, id) || repo.existsByDocumentoAndIdUsuarioNot(documento, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo o documento ya está registrado");
        }
        objetivo.setNombres(datos.nombres().trim());
        objetivo.setApellidos(datos.apellidos().trim());
        objetivo.setDocumento(documento);
        objetivo.setFechaNacimiento(datos.fechaNacimiento());
        objetivo.setCorreo(correo);
        if (!esPropio && datos.rol() != null && !datos.rol().isBlank()) {
            try { objetivo.setRol(Usuario.Rol.valueOf(datos.rol().trim().toUpperCase())); }
            catch (IllegalArgumentException ex) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol de usuario inválido"); }
        }
        return UsuarioResponse.desde(repo.save(objetivo));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminar(@PathVariable Integer id, HttpSession session) {
        Usuario administrador = sesionService.administrador(session);
        authService.eliminar(id);
        if (administrador.getIdUsuario().equals(id)) session.invalidate();
        return ResponseEntity.noContent().build();
    }
}
