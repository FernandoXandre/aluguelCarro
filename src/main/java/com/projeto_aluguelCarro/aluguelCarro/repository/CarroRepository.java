package com.projeto_aluguelCarro.aluguelCarro.repository;

import com.projeto_aluguelCarro.aluguelCarro.domain.Carro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CarroRepository extends JpaRepository<Carro, Long> {

    // Usado em GET /carros/disponiveis
    List<Carro> findByDisponivelTrue();

    Optional<Carro> findByPlaca(String placa);

    // Usado no CarroService para impedir placa duplicada
    boolean existsByPlaca(String placa);
}
