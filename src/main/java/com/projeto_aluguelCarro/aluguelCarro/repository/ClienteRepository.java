package com.projeto_aluguelCarro.aluguelCarro.repository;

import com.projeto_aluguelCarro.aluguelCarro.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByCpf(String cpf);

    // Usado no ClienteService para impedir CPF duplicado
    boolean existsByCpf(String cpf);
}
