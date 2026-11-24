package com.culturatech.auth.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categoria")
public class Categoria {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;
    @Column(name = "nombre_categoria", nullable = false, unique = true, length = 100)
    private String nombreCategoria;
    @Column(length = 500)
    private String descripcion;
    public Integer getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Integer id) { this.idCategoria = id; }
    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombre) { this.nombreCategoria = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
