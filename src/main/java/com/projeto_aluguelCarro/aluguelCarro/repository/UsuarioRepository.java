package com.projeto_aluguelCarro.aluguelCarro.repository;

import com.projeto_aluguelCarro.aluguelCarro.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    // Usado no UsuarioService para impedir username duplicado
    boolean existsByUsername(String username);
}
