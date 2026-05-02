package com.projeto_aluguelCarro.aluguelCarro.dto;

import java.math.BigDecimal;

// DTO de entrada e saída para operações com carros
public record CarroDTO(
        Long id,
        String marca,
        String modelo,
        String placa,
        Integer ano,
        BigDecimal valorDiaria,
        Boolean disponivel,
        String categoria
) {}
