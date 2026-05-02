package com.projeto_aluguelCarro.aluguelCarro.service;

import com.projeto_aluguelCarro.aluguelCarro.domain.*;
import com.projeto_aluguelCarro.aluguelCarro.domain.enums.StatusAluguel;
import com.projeto_aluguelCarro.aluguelCarro.dto.AluguelDTO;
import com.projeto_aluguelCarro.aluguelCarro.repository.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AluguelService {

    private final AluguelRepository aluguelRepository;
    private final CarroRepository carroRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;

    public AluguelService(AluguelRepository aluguelRepository, CarroRepository carroRepository,
                          ClienteRepository clienteRepository, UsuarioRepository usuarioRepository) {
        this.aluguelRepository = aluguelRepository;
        this.carroRepository = carroRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<AluguelDTO> listarTodos() {
        return aluguelRepository.findAll().stream().map(this::toDTO).toList();
    }

    public AluguelDTO buscarPorId(Long id) {
        return toDTO(aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado: " + id)));
    }

    public List<AluguelDTO> listarPorCliente(Long clienteId) {
        return aluguelRepository.findByClienteId(clienteId).stream().map(this::toDTO).toList();
    }

    public List<AluguelDTO> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return aluguelRepository.findByDataInicioBetween(inicio, fim).stream().map(this::toDTO).toList();
    }

    public AluguelDTO criar(AluguelDTO dto) {
        Carro carro = carroRepository.findById(dto.carroId())
                .orElseThrow(() -> new RuntimeException("Carro não encontrado: " + dto.carroId()));

        // Carro só pode ser alugado se estiver disponível
        if (!carro.getDisponivel()) {
            throw new RuntimeException("Carro não está disponível para aluguel.");
        }

        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado: " + dto.clienteId()));

        // Usuário é opcional — aluguel pode ser registrado sem atendente vinculado
        Usuario usuario = null;
        if (dto.usuarioId() != null) {
            usuario = usuarioRepository.findById(dto.usuarioId()).orElse(null);
        }

        // Calcula a quantidade de dias e valida o intervalo de datas
        long dias = ChronoUnit.DAYS.between(dto.dataInicio(), dto.dataFim());
        if (dias <= 0) {
            throw new RuntimeException("A data fim deve ser posterior à data de início.");
        }

        // valorTotal = dias × valorDiaria do carro
        BigDecimal valorTotal = carro.getValorDiaria().multiply(BigDecimal.valueOf(dias));

        Aluguel aluguel = Aluguel.builder()
                .dataInicio(dto.dataInicio())
                .dataFim(dto.dataFim())
                .valorTotal(valorTotal)
                .status(StatusAluguel.ATIVO)
                .cliente(cliente)
                .carro(carro)
                .usuario(usuario)
                .build();

        // Bloqueia o carro para evitar aluguéis simultâneos
        carro.setDisponivel(false);
        carroRepository.save(carro);

        return toDTO(aluguelRepository.save(aluguel));
    }

    public AluguelDTO concluir(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado: " + id));
        aluguel.setStatus(StatusAluguel.CONCLUIDO);
        // Libera o carro para novos aluguéis
        aluguel.getCarro().setDisponivel(true);
        carroRepository.save(aluguel.getCarro());
        return toDTO(aluguelRepository.save(aluguel));
    }

    public AluguelDTO cancelar(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado: " + id));
        aluguel.setStatus(StatusAluguel.CANCELADO);
        // Libera o carro para novos aluguéis
        aluguel.getCarro().setDisponivel(true);
        carroRepository.save(aluguel.getCarro());
        return toDTO(aluguelRepository.save(aluguel));
    }

    private AluguelDTO toDTO(Aluguel a) {
        return new AluguelDTO(
                a.getId(), a.getDataInicio(), a.getDataFim(), a.getValorTotal(), a.getStatus(),
                a.getCliente().getId(), a.getCarro().getId(),
                // usuarioId pode ser null se nenhum atendente foi vinculado ao aluguel
                a.getUsuario() != null ? a.getUsuario().getId() : null
        );
    }
}
