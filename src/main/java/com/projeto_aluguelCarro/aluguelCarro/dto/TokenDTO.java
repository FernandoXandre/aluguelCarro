package com.projeto_aluguelCarro.aluguelCarro.dto;

/**
 * DTO de resposta para o endpoint POST /auth/login.
 *
 * @param token     String JWT assinado com HMAC-SHA256
 * @param tipo      Sempre "Bearer" — padrão para Authorization header
 * @param expiracao Data/hora de expiração no formato ISO-8601
 */
public record TokenDTO(String token, String tipo, String expiracao) {}
