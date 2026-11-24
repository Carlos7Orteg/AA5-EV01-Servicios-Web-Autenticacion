package com.culturatech.auth.repository;

import com.culturatech.auth.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Proporciona operaciones de persistencia y consulta para los usuarios.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Busca un usuario mediante su correo electrónico.
     *
     * @param correo correo electrónico registrado
     * @return usuario encontrado, si existe
     */
    Optional<Usuario> findByCorreo(String correo);

    /**
     * Comprueba si ya existe un usuario con el correo indicado.
     *
     * @param correo correo electrónico que se desea comprobar
     * @return true si el correo ya está registrado
     */
    boolean existsByCorreo(String correo);

    /**
     * Comprueba si ya existe un usuario con el documento indicado.
     *
     * @param documento número de documento que se desea comprobar
     * @return true si el documento ya está registrado
     */
    boolean existsByDocumento(String documento);

    boolean existsByCorreoAndIdUsuarioNot(String correo, Integer idUsuario);

    boolean existsByDocumentoAndIdUsuarioNot(String documento, Integer idUsuario);
}
