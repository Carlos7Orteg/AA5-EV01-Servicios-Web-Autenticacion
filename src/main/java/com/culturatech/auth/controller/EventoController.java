package com.culturatech.auth.controller;

import com.culturatech.auth.dto.EventoRequest;
import com.culturatech.auth.model.Categoria;
import com.culturatech.auth.model.Evento;
import com.culturatech.auth.model.Lugar;
import com.culturatech.auth.repository.CategoriaRepository;
import com.culturatech.auth.repository.EventoRepository;
import com.culturatech.auth.repository.LugarRepository;
import com.culturatech.auth.service.SesionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {
    private final EventoRepository repo; private final CategoriaRepository categorias; private final LugarRepository lugares; private final SesionService sesion;
    public EventoController(EventoRepository repo,CategoriaRepository categorias,LugarRepository lugares,SesionService sesion) {
        this.repo=repo; this.categorias=categorias; this.lugares=lugares; this.sesion=sesion;
    }
    @GetMapping public List<Evento> listar() { return repo.findAll(Sort.by(Sort.Direction.ASC,"fechaHora")); }
    @GetMapping("/{id}") public Evento obtener(@PathVariable Integer id) { return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Evento no encontrado")); }
    @PostMapping public ResponseEntity<Evento> crear(@Valid @RequestBody EventoRequest request,HttpSession session) {
        sesion.administrador(session); Evento e=new Evento(); aplicar(e,request); return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(e));
    }
    @PutMapping("/{id}") public Evento actualizar(@PathVariable Integer id,@Valid @RequestBody EventoRequest request,HttpSession session) {
        sesion.administrador(session); Evento e=obtener(id); aplicar(e,request); return repo.save(e);
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable Integer id,HttpSession session) {
        sesion.administrador(session); repo.delete(obtener(id)); return ResponseEntity.noContent().build();
    }
    private void aplicar(Evento e,EventoRequest r) {
        Categoria c=categorias.findById(r.categoria().idCategoria()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Categoría no encontrada"));
        Lugar l=lugares.findById(r.lugar().idLugar()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Lugar no encontrado"));
        e.setTitulo(r.titulo().trim()); e.setDescripcion(r.descripcion()); e.setFechaHora(r.fechaHora()); e.setCosto(r.costo());
        e.setImagen(r.imagen()); e.setEstado(r.estado().trim().toUpperCase()); e.setCategoria(c); e.setLugar(l);
    }
}
