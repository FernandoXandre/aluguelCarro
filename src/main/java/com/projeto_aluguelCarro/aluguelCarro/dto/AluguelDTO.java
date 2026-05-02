package com.projeto_aluguelCarro.aluguelCarro.dto;

import com.projeto_aluguelCarro.aluguelCarro.domain.enums.StatusAluguel;
import java.math.BigDecimal;
import java.time.LocalDate;

// DTO de entrada e saída para aluguéis
// Na criação, valorTotal e status são ignorados — o AluguelService os define automaticamente
public record AluguelDTO(
        Long id,
        LocalDate dataInicio,
        LocalDate dataFim,
        BigDecimal valorTotal,
        StatusAluguel status,
        Long clienteId,
        Long carroId,
        Long usuarioId
) {}
