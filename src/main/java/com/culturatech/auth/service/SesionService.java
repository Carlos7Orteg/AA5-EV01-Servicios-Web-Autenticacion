package com.culturatech.auth.service;

import com.culturatech.auth.model.Usuario;
import com.culturatech.auth.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/** Obtiene el usuario autenticado desde la sesión HTTP. */
@Service
public class SesionService {
    public static final String USUARIO_ID = "idUsuario";
    private final UsuarioRepository usuarioRepository;
    public SesionService(UsuarioRepository repo) { this.usuarioRepository = repo; }
    public Usuario usuarioActual(HttpSession session) {
        Object id = session.getAttribute(USUARIO_ID);
        if (!(id instanceof Integer idUsuario)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sesión no autenticada");
        }
        return usuarioRepository.findById(idUsuario).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sesión no válida"));
    }
    public Usuario administrador(HttpSession session) {
        Usuario usuario = usuarioActual(session);
        if (usuario.getRol() != Usuario.Rol.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso restringido a administradores");
        }
        return usuario;
    }
}
