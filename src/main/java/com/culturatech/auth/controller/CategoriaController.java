package com.culturatech.auth.controller;

import com.culturatech.auth.dto.CategoriaRequest;
import com.culturatech.auth.model.Categoria;
import com.culturatech.auth.repository.CategoriaRepository;
import com.culturatech.auth.repository.EventoRepository;
import com.culturatech.auth.service.SesionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    private final CategoriaRepository repo; private final EventoRepository eventos; private final SesionService sesion;
    public CategoriaController(CategoriaRepository repo, EventoRepository eventos, SesionService sesion) { this.repo=repo; this.eventos=eventos; this.sesion=sesion; }
    @GetMapping public List<Categoria> listar() { return repo.findAll(); }
    @GetMapping("/{id}") public Categoria obtener(@PathVariable Integer id) { return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Categoría no encontrada")); }
    @PostMapping public ResponseEntity<Categoria> crear(@Valid @RequestBody CategoriaRequest request, HttpSession session) {
        sesion.administrador(session); validarNombre(request.nombreCategoria(), null);
        Categoria c=new Categoria(); c.setNombreCategoria(request.nombreCategoria().trim()); c.setDescripcion(request.descripcion());
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(c));
    }
    @PutMapping("/{id}") public Categoria actualizar(@PathVariable Integer id, @Valid @RequestBody CategoriaRequest request, HttpSession session) {
        sesion.administrador(session); Categoria c=obtener(id); validarNombre(request.nombreCategoria(),id);
        c.setNombreCategoria(request.nombreCategoria().trim()); c.setDescripcion(request.descripcion()); return repo.save(c);
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable Integer id, HttpSession session) {
        sesion.administrador(session); Categoria c=obtener(id);
        if(eventos.existsByCategoria_IdCategoria(id)) throw new ResponseStatusException(HttpStatus.CONFLICT,"No se puede eliminar: existen eventos asociados a esta categoría");
        repo.delete(c); return ResponseEntity.noContent().build();
    }
    private void validarNombre(String nombre,Integer id) {
        if(repo.existsByNombreCategoriaIgnoreCase(nombre.trim()) && (id==null || !obtener(id).getNombreCategoria().equalsIgnoreCase(nombre.trim())))
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Ya existe una categoría con ese nombre");
    }
}
