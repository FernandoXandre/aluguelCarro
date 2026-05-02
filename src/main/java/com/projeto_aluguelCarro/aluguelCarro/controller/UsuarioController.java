package com.projeto_aluguelCarro.aluguelCarro.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto_aluguelCarro.aluguelCarro.domain.enums.PerfilUsuario;
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

    // Recebe Map para extrair a senha sem incluí-la no UsuarioDTO, evitando que seja retornada na resposta
    @PostMapping
    public UsuarioDTO salvar(@RequestBody Map<String, String> body) {
        UsuarioDTO dto = new UsuarioDTO(null, body.get("username"),
                PerfilUsuario.valueOf(body.get("perfil")));
        return usuarioService.salvar(dto, body.get("senha"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
