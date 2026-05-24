package com.projeto_aluguelCarro.aluguelCarro.controller;

import com.projeto_aluguelCarro.aluguelCarro.dto.LoginRequestDTO;
import com.projeto_aluguelCarro.aluguelCarro.dto.TokenDTO;
import com.projeto_aluguelCarro.aluguelCarro.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint de autenticação. Valida credenciais e devolve um token JWT.
 *
 * <p>Exemplo de requisição:
 * <pre>
 * POST /auth/login
 * {
 *   "username": "atendente01",
 *   "senha": "minhasenha"
 * }
 * </pre>
 *
 * <p>Resposta:
 * <pre>
 * {
 *   "token": "eyJhbGciOiJIUzI1NiJ9...",
 *   "tipo": "Bearer",
 *   "expiracao": "2026-05-25T10:30:00"
 * }
 * </pre>
 *
 * <p>Use o token nas próximas requisições:
 * {@code Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...}
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public TokenDTO login(@RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }
}
