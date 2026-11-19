package com.culturatech.auth.dto;

import com.culturatech.auth.model.Usuario;

/**
 * Información pública devuelta después de un registro o inicio de sesión.
 * Nunca expone la contraseña almacenada.
 */
public class AuthResponse {

    private Integer idUsuario;
    private String nombres;
    private String apellidos;
    private String correo;
    private String rol;
    private String mensaje;

    /**
     * Construye la respuesta a partir de un usuario persistido.
     *
     * @param usuario usuario autenticado o recién registrado
     */
    public AuthResponse(Usuario usuario) {
        this.idUsuario = usuario.getIdUsuario();
        this.nombres = usuario.getNombres();
        this.apellidos = usuario.getApellidos();
        this.correo = usuario.getCorreo();
        this.rol = usuario.getRol().name();
    }
    
    public AuthResponse(Usuario usuario, String mensaje) {
        this.idUsuario = usuario.getIdUsuario();
        this.nombres = usuario.getNombres();
        this.apellidos = usuario.getApellidos();
        this.correo = usuario.getCorreo();
        this.rol = usuario.getRol().name();
        this.mensaje = mensaje;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public String getRol() {
        return rol;
    }
    
    public String getMensaje() {
        return mensaje;
    }
}
