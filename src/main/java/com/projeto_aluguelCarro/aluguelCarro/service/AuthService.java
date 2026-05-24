package com.projeto_aluguelCarro.aluguelCarro.service;

import com.projeto_aluguelCarro.aluguelCarro.domain.Usuario;
import com.projeto_aluguelCarro.aluguelCarro.dto.LoginRequestDTO;
import com.projeto_aluguelCarro.aluguelCarro.dto.TokenDTO;
import com.projeto_aluguelCarro.aluguelCarro.exception.RegraNegocioException;
import com.projeto_aluguelCarro.aluguelCarro.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Serviço de autenticação: valida credenciais e retorna um token JWT.
 */
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(UsuarioRepository usuarioRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    /**
     * Autentica o usuário e retorna um TokenDTO com o JWT e sua expiração.
     *
     * <p>A mensagem de erro é genérica ("usuário ou senha inválidos") para
     * não revelar se o username existe ou não no sistema (prevenção de enumeração).
     */
    public TokenDTO login(LoginRequestDTO request) {
        if (request.username() == null || request.senha() == null) {
            throw new RegraNegocioException("Username e senha são obrigatórios.");
        }

        Usuario usuario = usuarioRepository.findByUsername(request.username())
                .orElseThrow(() -> new RegraNegocioException("Usuário ou senha inválidos."));

        // Compara a senha recebida com o hash BCrypt armazenado
        if (!passwordEncoder.matches(request.senha(), usuario.getSenha())) {
            throw new RegraNegocioException("Usuário ou senha inválidos.");
        }

        String token = tokenService.gerarToken(usuario.getUsername());
        String expiracao = tokenService.calcularExpiracao().toString();

        return new TokenDTO(token, "Bearer", expiracao);
    }
}
