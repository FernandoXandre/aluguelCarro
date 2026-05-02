package com.projeto_aluguelCarro.aluguelCarro.controller;

import com.projeto_aluguelCarro.aluguelCarro.dto.ClienteDTO;
import com.projeto_aluguelCarro.aluguelCarro.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Endpoints REST para gerenciamento de clientes da locadora
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<ClienteDTO> listarTodos() {
        return clienteService.listarTodos();
    }

    @GetMapping("/{id}")
    public ClienteDTO buscarPorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id);
    }

    @PostMapping
    public ClienteDTO salvar(@RequestBody ClienteDTO dto) {
        return clienteService.salvar(dto);
    }

    @PutMapping("/{id}")
    public ClienteDTO atualizar(@PathVariable Long id, @RequestBody ClienteDTO dto) {
        return clienteService.atualizar(id, dto);
    }

    // Falha se o cliente possuir aluguéis registrados
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
