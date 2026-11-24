package com.culturatech.auth.repository;
import com.culturatech.auth.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EventoRepository extends JpaRepository<Evento, Integer> {
    boolean existsByCategoria_IdCategoria(Integer idCategoria);
    boolean existsByLugar_IdLugar(Integer idLugar);
}
