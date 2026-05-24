package com.projeto_aluguelCarro.aluguelCarro.dto;

/**
 * DTO de entrada para o endpoint POST /auth/login.
 * Recebe as credenciais do usuário em texto puro;
 * a comparação com o hash BCrypt é feita no AuthService.
 */
public record LoginRequestDTO(String username, String senha) {}
