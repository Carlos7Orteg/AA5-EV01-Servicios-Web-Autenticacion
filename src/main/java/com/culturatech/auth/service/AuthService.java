package com.culturatech.auth.service;

import com.culturatech.auth.dto.*;
import com.culturatech.auth.model.Usuario;
import com.culturatech.auth.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AuthResponse registrar(RegistroRequest request) {
        String correo = request.getCorreo().trim().toLowerCase();
        String documento = request.getDocumento().trim();
        if (usuarioRepository.existsByCorreo(correo) || usuarioRepository.existsByDocumento(documento)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya existe");
        }
        Usuario usuario = new Usuario();
        usuario.setNombres(request.getNombres().trim());
        usuario.setApellidos(request.getApellidos().trim());
        usuario.setDocumento(documento);
        usuario.setFechaNacimiento(request.getFechaNacimiento());
        usuario.setCorreo(correo);
        usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        usuario.setRol(Usuario.Rol.USER);
        return new AuthResponse(usuarioRepository.save(usuario), "Usuario registrado correctamente");
    }

    @Transactional(readOnly = true)
    public AuthResponse iniciarSesion(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo().trim().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Error en la autenticación"));
        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Error en la autenticación");
        }
        return new AuthResponse(usuario, "Autenticación satisfactoria");
    }

    @Transactional(readOnly = true)
    public Usuario obtener(Integer id) {
        return usuarioRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    @Transactional
    public Usuario actualizarPerfil(Integer id, PerfilRequest request) {
        Usuario usuario = obtener(id);
        String correo = request.correo().trim().toLowerCase();
        String documento = request.documento().trim();
        if (usuarioRepository.existsByCorreoAndIdUsuarioNot(correo, id)
                || usuarioRepository.existsByDocumentoAndIdUsuarioNot(documento, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo o documento ya está registrado");
        }
        usuario.setNombres(request.nombres().trim());
        usuario.setApellidos(request.apellidos().trim());
        usuario.setDocumento(documento);
        usuario.setFechaNacimiento(request.fechaNacimiento());
        usuario.setCorreo(correo);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void cambiarContrasena(Integer id, CambioContrasenaRequest request) {
        Usuario usuario = obtener(id);
        if (!passwordEncoder.matches(request.contrasenaActual(), usuario.getContrasena())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "La contraseña actual no es correcta");
        }
        if (!request.nuevaContrasena().equals(request.confirmarContrasena())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La confirmación de contraseña no coincide");
        }
        usuario.setContrasena(passwordEncoder.encode(request.nuevaContrasena()));
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void eliminar(Integer id) {
        Usuario usuario = obtener(id);
        usuarioRepository.delete(usuario);
    }
}
