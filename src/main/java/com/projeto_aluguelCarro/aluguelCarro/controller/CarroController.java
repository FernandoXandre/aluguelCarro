package com.projeto_aluguelCarro.aluguelCarro.controller;

import com.projeto_aluguelCarro.aluguelCarro.dto.CarroDTO;
import com.projeto_aluguelCarro.aluguelCarro.service.CarroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Endpoints REST para gerenciamento da frota de veículos
@RestController
@RequestMapping("/carros")
public class CarroController {

    private final CarroService carroService;

    public CarroController(CarroService carroService) {
        this.carroService = carroService;
    }

    @GetMapping
    public List<CarroDTO> listarTodos() {
        return carroService.listarTodos();
    }

    // Retorna apenas carros com disponivel = true
    @GetMapping("/disponiveis")
    public List<CarroDTO> listarDisponiveis() {
        return carroService.listarDisponiveis();
    }

    @GetMapping("/{id}")
    public CarroDTO buscarPorId(@PathVariable Long id) {
        return carroService.buscarPorId(id);
    }

    @PostMapping
    public CarroDTO salvar(@RequestBody CarroDTO dto) {
        return carroService.salvar(dto);
    }

    @PutMapping("/{id}")
    public CarroDTO atualizar(@PathVariable Long id, @RequestBody CarroDTO dto) {
        return carroService.atualizar(id, dto);
    }

    // Retorna 204 No Content após exclusão bem-sucedida
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        carroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
