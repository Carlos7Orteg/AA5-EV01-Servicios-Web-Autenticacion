package com.culturatech.auth.service;

import com.culturatech.auth.dto.AuthResponse;
import com.culturatech.auth.dto.LoginRequest;
import com.culturatech.auth.dto.RegistroRequest;
import com.culturatech.auth.model.Usuario;
import com.culturatech.auth.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * Contiene la lógica de negocio para el registro y la autenticación.
 */
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Crea el servicio con sus dependencias.
     *
     * @param usuarioRepository repositorio de usuarios
     * @param passwordEncoder componente BCrypt para las contraseñas
     */
    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra un usuario nuevo y almacena su contraseña con BCrypt.
     *
     * @param request datos validados del registro
     * @return datos públicos del usuario registrado
     */
    @Transactional
    public AuthResponse registrar(RegistroRequest request) {
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El usuario ya existe");
        }

        if (usuarioRepository.existsByDocumento(request.getDocumento())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El usuario ya existe");
        }

        Usuario usuario = new Usuario();
        usuario.setNombres(request.getNombres().trim());
        usuario.setApellidos(request.getApellidos().trim());
        usuario.setDocumento(request.getDocumento().trim());
        usuario.setFechaNacimiento(request.getFechaNacimiento());
        usuario.setCorreo(request.getCorreo().trim().toLowerCase());
        usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        usuario.setRol(Usuario.Rol.USER);

        Usuario guardado = usuarioRepository.save(usuario);
        return new AuthResponse(guardado, "Usuario registrado correctamente");
    }

    /**
     * Verifica el correo y la contraseña de un usuario registrado.
     *
     * @param request credenciales recibidas
     * @return datos públicos del usuario autenticado
     */
    @Transactional(readOnly = true)
    public AuthResponse iniciarSesion(LoginRequest request) {
        Usuario usuario = usuarioRepository
                .findByCorreo(request.getCorreo().trim().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Error en la autenticación"));

        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Error en la autenticación");
        }

        return new AuthResponse(usuario, "Autenticación satisfactoria");
    }
}
