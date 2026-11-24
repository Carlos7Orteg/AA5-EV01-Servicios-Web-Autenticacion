package com.culturatech.auth.controller;

import com.culturatech.auth.dto.LugarRequest;
import com.culturatech.auth.model.Lugar;
import com.culturatech.auth.repository.EventoRepository;
import com.culturatech.auth.repository.LugarRepository;
import com.culturatech.auth.service.SesionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/lugares")
public class LugarController {
    private final LugarRepository repo; private final EventoRepository eventos; private final SesionService sesion;
    public LugarController(LugarRepository repo, EventoRepository eventos, SesionService sesion) { this.repo=repo; this.eventos=eventos; this.sesion=sesion; }
    @GetMapping public List<Lugar> listar() { return repo.findAll(); }
    @GetMapping("/{id}") public Lugar obtener(@PathVariable Integer id) { return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Lugar no encontrado")); }
    @PostMapping public ResponseEntity<Lugar> crear(@Valid @RequestBody LugarRequest request,HttpSession session) {
        sesion.administrador(session); Lugar l=new Lugar(); aplicar(l,request); return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(l));
    }
    @PutMapping("/{id}") public Lugar actualizar(@PathVariable Integer id,@Valid @RequestBody LugarRequest request,HttpSession session) {
        sesion.administrador(session); Lugar l=obtener(id); aplicar(l,request); return repo.save(l);
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable Integer id,HttpSession session) {
        sesion.administrador(session); Lugar l=obtener(id);
        if(eventos.existsByLugar_IdLugar(id)) throw new ResponseStatusException(HttpStatus.CONFLICT,"No se puede eliminar: existen eventos asociados a este lugar");
        repo.delete(l); return ResponseEntity.noContent().build();
    }
    private void aplicar(Lugar l,LugarRequest r) {
        l.setNombreLugar(r.nombreLugar().trim()); l.setDireccion(r.direccion().trim()); l.setLocalidad(r.localidad());
        l.setTelefono(r.telefono()); l.setLatitud(r.latitud()); l.setLongitud(r.longitud()); l.setPaginaWeb(r.paginaWeb());
    }
}
