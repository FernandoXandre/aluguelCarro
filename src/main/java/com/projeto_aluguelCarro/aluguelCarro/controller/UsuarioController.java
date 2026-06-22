package com.projeto_aluguelCarro.aluguelCarro.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto_aluguelCarro.aluguelCarro.domain.enums.PerfilUsuario;
import com.projeto_aluguelCarro.aluguelCarro.dto.CadastroUsuarioRequest;
import com.projeto_aluguelCarro.aluguelCarro.dto.UsuarioDTO;
import com.projeto_aluguelCarro.aluguelCarro.service.UsuarioService;

// Endpoints REST para gerenciamento de usuários do sistema (atendentes e admins)
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioDTO> listarTodos() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public UsuarioDTO buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @PostMapping
    public UsuarioDTO salvar(@RequestBody CadastroUsuarioRequest body) {
        String perfilStr = body.perfil();
        if (perfilStr == null || perfilStr.isBlank()) {
            throw new com.projeto_aluguelCarro.aluguelCarro.exception.RegraNegocioException(
                    "O perfil é obrigatório. Valores aceitos: ADMIN, ATENDENTE.");
        }
        PerfilUsuario perfil;
        try {
            perfil = PerfilUsuario.valueOf(perfilStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new com.projeto_aluguelCarro.aluguelCarro.exception.RegraNegocioException(
                    "Perfil inválido: '" + perfilStr + "'. Valores aceitos: ADMIN, ATENDENTE.");
        }
        UsuarioDTO dto = new UsuarioDTO(null, body.username(), perfil);
        return usuarioService.salvar(dto, body.senha());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
