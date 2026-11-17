/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Carlos
 */

package com.culturatech.auth.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entidad que representa a un usuario registrado en el servicio web
 * de autenticación AA5-EV01.
 *
 * Conserva la estructura de usuarios del proyecto CulturaTech Bogotá.
 */
@Entity
@Table(name = "usuario")
public class Usuario {

    /** Identificador único del usuario, generado automáticamente. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    /** Nombres del usuario. */
    @Column(nullable = false, length = 100)
    private String nombres;

    /** Apellidos del usuario. */
    @Column(nullable = false, length = 100)
    private String apellidos;

    /** Número de documento de identidad, único para cada usuario. */
    @Column(nullable = false, unique = true, length = 20)
    private String documento;

    /** Fecha de nacimiento del usuario. */
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    /** Correo electrónico utilizado como identificador de acceso. */
    @Column(nullable = false, unique = true, length = 150)
    private String correo;

    /** Contraseña almacenada mediante cifrado BCrypt. */
    @Column(name = "contraseña", nullable = false, length = 255)
    private String contrasena;

    /** Rol asignado al usuario dentro de la aplicación. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol = Rol.USER;

    /**
     * Define los roles disponibles para los usuarios.
     */
    public enum Rol {
        USER,
        ADMIN
    }

    /**
     * Constructor vacío requerido por JPA para crear instancias
     * de la entidad.
     */
    public Usuario() {
    }

    /** Obtiene el identificador del usuario. */
    public Integer getIdUsuario() {
        return idUsuario;
    }

    /** Asigna el identificador del usuario. */
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    /** Obtiene los nombres del usuario. */
    public String getNombres() {
        return nombres;
    }

    /** Asigna los nombres del usuario. */
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    /** Obtiene los apellidos del usuario. */
    public String getApellidos() {
        return apellidos;
    }

    /** Asigna los apellidos del usuario. */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /** Obtiene el documento de identidad. */
    public String getDocumento() {
        return documento;
    }

    /** Asigna el documento de identidad. */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /** Obtiene la fecha de nacimiento. */
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    /** Asigna la fecha de nacimiento. */
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /** Obtiene el correo electrónico. */
    public String getCorreo() {
        return correo;
    }

    /** Asigna el correo electrónico. */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /** Obtiene la contraseña cifrada del usuario. */
    public String getContrasena() {
        return contrasena;
    }

    /** Asigna la contraseña cifrada del usuario. */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /** Obtiene el rol del usuario. */
    public Rol getRol() {
        return rol;
    }

    /** Asigna el rol del usuario. */
    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
