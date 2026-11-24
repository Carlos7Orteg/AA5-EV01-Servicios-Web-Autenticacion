package com.culturatech.auth.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "evento")
public class Evento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private Integer idEvento;
    @Column(nullable = false, length = 180)
    private String titulo;
    @Column(length = 5000)
    private String descripcion;
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal costo;
    @Column(length = 1000)
    private String imagen;
    @Column(nullable = false, length = 30)
    private String estado;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_lugar", nullable = false)
    private Lugar lugar;
    public Integer getIdEvento() { return idEvento; }
    public void setIdEvento(Integer v) { this.idEvento = v; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String v) { this.titulo = v; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String v) { this.descripcion = v; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime v) { this.fechaHora = v; }
    public BigDecimal getCosto() { return costo; }
    public void setCosto(BigDecimal v) { this.costo = v; }
    public String getImagen() { return imagen; }
    public void setImagen(String v) { this.imagen = v; }
    public String getEstado() { return estado; }
    public void setEstado(String v) { this.estado = v; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria v) { this.categoria = v; }
    public Lugar getLugar() { return lugar; }
    public void setLugar(Lugar v) { this.lugar = v; }
}
