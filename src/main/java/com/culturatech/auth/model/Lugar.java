package com.culturatech.auth.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "lugar")
public class Lugar {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lugar")
    private Integer idLugar;
    @Column(name = "nombre_lugar", nullable = false, length = 150)
    private String nombreLugar;
    @Column(nullable = false, length = 255)
    private String direccion;
    @Column(length = 100)
    private String localidad;
    @Column(length = 100)
    private String telefono;
    @Column(precision = 10, scale = 7)
    private BigDecimal latitud;
    @Column(precision = 10, scale = 7)
    private BigDecimal longitud;
    @Column(name = "pagina_web", length = 255)
    private String paginaWeb;
    public Integer getIdLugar() { return idLugar; }
    public void setIdLugar(Integer id) { this.idLugar = id; }
    public String getNombreLugar() { return nombreLugar; }
    public void setNombreLugar(String v) { this.nombreLugar = v; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String v) { this.direccion = v; }
    public String getLocalidad() { return localidad; }
    public void setLocalidad(String v) { this.localidad = v; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String v) { this.telefono = v; }
    public BigDecimal getLatitud() { return latitud; }
    public void setLatitud(BigDecimal v) { this.latitud = v; }
    public BigDecimal getLongitud() { return longitud; }
    public void setLongitud(BigDecimal v) { this.longitud = v; }
    public String getPaginaWeb() { return paginaWeb; }
    public void setPaginaWeb(String v) { this.paginaWeb = v; }
}
