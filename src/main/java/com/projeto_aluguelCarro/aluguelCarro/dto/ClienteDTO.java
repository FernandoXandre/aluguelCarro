package com.projeto_aluguelCarro.aluguelCarro.dto;

// DTO de entrada e saída para operações com clientes
public record ClienteDTO(
        Long id,
        String nome,
        String cpf,
        String email,
        String telefone,
        String endereco,
        String cnh
) {}
