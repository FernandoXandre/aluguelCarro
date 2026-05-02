package com.projeto_aluguelCarro.aluguelCarro.controller;

import com.projeto_aluguelCarro.aluguelCarro.dto.AluguelDTO;
import com.projeto_aluguelCarro.aluguelCarro.service.AluguelService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

// Endpoints REST para criação e controle do ciclo de vida dos aluguéis
@RestController
@RequestMapping("/alugueis")
public class AluguelController {

    private final AluguelService aluguelService;

    public AluguelController(AluguelService aluguelService) {
        this.aluguelService = aluguelService;
    }

    @GetMapping
    public List<AluguelDTO> listarTodos() {
        return aluguelService.listarTodos();
    }

    @GetMapping("/{id}")
    public AluguelDTO buscarPorId(@PathVariable Long id) {
        return aluguelService.buscarPorId(id);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<AluguelDTO> listarPorCliente(@PathVariable Long clienteId) {
        return aluguelService.listarPorCliente(clienteId);
    }

    // Filtra pelo campo dataInicio; ex: ?inicio=2026-01-01&fim=2026-12-31
    @GetMapping("/periodo")
    public List<AluguelDTO> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return aluguelService.listarPorPeriodo(inicio, fim);
    }

    // Calcula valorTotal e marca o carro como indisponível
    @PostMapping
    public AluguelDTO criar(@RequestBody AluguelDTO dto) {
        return aluguelService.criar(dto);
    }

    // Muda status para CONCLUIDO e libera o carro
    @PatchMapping("/{id}/concluir")
    public AluguelDTO concluir(@PathVariable Long id) {
        return aluguelService.concluir(id);
    }

    // Muda status para CANCELADO e libera o carro
    @PatchMapping("/{id}/cancelar")
    public AluguelDTO cancelar(@PathVariable Long id) {
        return aluguelService.cancelar(id);
    }
}
