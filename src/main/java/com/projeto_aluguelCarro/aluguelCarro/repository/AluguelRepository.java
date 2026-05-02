package com.projeto_aluguelCarro.aluguelCarro.repository;

import com.projeto_aluguelCarro.aluguelCarro.domain.Aluguel;
import com.projeto_aluguelCarro.aluguelCarro.domain.enums.StatusAluguel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AluguelRepository extends JpaRepository<Aluguel, Long> {

    // Usado em GET /alugueis/cliente/{clienteId}
    List<Aluguel> findByClienteId(Long clienteId);

    List<Aluguel> findByStatus(StatusAluguel status);

    // Filtra pelo campo dataInicio no intervalo informado; usado em GET /alugueis/periodo
    List<Aluguel> findByDataInicioBetween(LocalDate inicio, LocalDate fim);

    // Usado no ClienteService para bloquear exclusão de cliente com aluguéis
    boolean existsByClienteId(Long clienteId);
}
