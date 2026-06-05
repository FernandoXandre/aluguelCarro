package com.projeto_aluguelCarro.aluguelCarro.service;

import com.projeto_aluguelCarro.aluguelCarro.domain.Usuario;
import com.projeto_aluguelCarro.aluguelCarro.dto.UsuarioDTO;
import com.projeto_aluguelCarro.aluguelCarro.exception.RegraNegocioException;
import com.projeto_aluguelCarro.aluguelCarro.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream().map(this::toDTO).toList();
    }

    public UsuarioDTO buscarPorId(Long id) {
        return toDTO(usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id)));
    }

    // Senha recebida fora do DTO para que nunca seja retornada nas respostas da API
    public UsuarioDTO salvar(UsuarioDTO dto, String senha) {
        if (dto.username() == null || dto.username().isBlank()) {
            throw new RegraNegocioException("O username é obrigatório.");
        }
        if (senha == null || senha.isBlank()) {
            throw new RegraNegocioException("A senha é obrigatória.");
        }
        // Impede cadastro de dois usuários com o mesmo username
        if (usuarioRepository.existsByUsername(dto.username())) {
            throw new RegraNegocioException("Username já cadastrado: " + dto.username());
        }
        Usuario usuario = Usuario.builder()
                .username(dto.username())
                .senha(passwordEncoder.encode(senha))
                .perfil(dto.perfil())
                .build();
        return toDTO(usuarioRepository.save(usuario));
    }

    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }

    // Senha omitida intencionalmente no DTO retornado
    private UsuarioDTO toDTO(Usuario u) {
        return new UsuarioDTO(u.getId(), u.getUsername(), u.getPerfil());
    }
}
