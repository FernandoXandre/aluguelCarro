package com.projeto_aluguelCarro.aluguelCarro.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Serviço responsável pela geração e validação de tokens JWT.
 *
 * <p>Configuração em application.properties:
 * <pre>
 *   jwt.secret=sua-chave-secreta-longa-aqui
 *   jwt.expiracao-ms=86400000   # 24 horas em milissegundos
 * </pre>
 *
 * <p>O token deve ser enviado nas requisições no header:
 * {@code Authorization: Bearer <token>}
 */
@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiracao-ms}")
    private long expiracaoMs;

    /**
     * Gera um token JWT assinado com HMAC-SHA256.
     *
     * @param username identificador do usuário autenticado (subject do token)
     * @return string JWT compacta
     */
    public String gerarToken(String username) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + expiracaoMs);

        return Jwts.builder()
                .subject(username)
                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(getChave())
                .compact();
    }

    /**
     * Extrai o username (subject) de um token válido.
     *
     * @throws JwtException se o token for inválido ou expirado
     */
    public String extrairUsername(String token) {
        return Jwts.parser()
                .verifyWith(getChave())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Verifica se o token é válido (assinatura correta e não expirado).
     */
    public boolean isTokenValido(String token) {
        try {
            Jwts.parser().verifyWith(getChave()).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Retorna a data/hora de expiração do token a partir da data atual.
     */
    public LocalDateTime calcularExpiracao() {
        return LocalDateTime.now().plusSeconds(expiracaoMs / 1000);
    }

    // Converte a string de configuração em SecretKey (mínimo 32 bytes para HS256)
    private SecretKey getChave() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
