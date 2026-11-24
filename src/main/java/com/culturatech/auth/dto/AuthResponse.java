package com.culturatech.auth.dto;

import com.culturatech.auth.model.Usuario;
import java.time.LocalDate;

/** Respuesta pública de registro e inicio de sesión, sin contraseña. */
public class AuthResponse {
    private Integer idUsuario;
    private String nombres;
    private String apellidos;
    private String documento;
    private LocalDate fechaNacimiento;
    private String correo;
    private String rol;
    private String mensaje;

    public AuthResponse(Usuario usuario) { this(usuario, null); }
    public AuthResponse(Usuario usuario, String mensaje) {
        this.idUsuario = usuario.getIdUsuario();
        this.nombres = usuario.getNombres();
        this.apellidos = usuario.getApellidos();
        this.documento = usuario.getDocumento();
        this.fechaNacimiento = usuario.getFechaNacimiento();
        this.correo = usuario.getCorreo();
        this.rol = usuario.getRol().name();
        this.mensaje = mensaje;
    }
    public Integer getIdUsuario() { return idUsuario; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getDocumento() { return documento; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getCorreo() { return correo; }
    public String getRol() { return rol; }
    public String getMensaje() { return mensaje; }
}
