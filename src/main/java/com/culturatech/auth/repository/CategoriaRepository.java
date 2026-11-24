package com.culturatech.auth.repository;
import com.culturatech.auth.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    boolean existsByNombreCategoriaIgnoreCase(String nombreCategoria);
}
