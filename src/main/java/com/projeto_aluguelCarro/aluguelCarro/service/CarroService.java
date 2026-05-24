package com.projeto_aluguelCarro.aluguelCarro.service;

import com.projeto_aluguelCarro.aluguelCarro.domain.Carro;
import com.projeto_aluguelCarro.aluguelCarro.dto.CarroDTO;
import com.projeto_aluguelCarro.aluguelCarro.exception.RegraNegocioException;
import com.projeto_aluguelCarro.aluguelCarro.repository.CarroRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CarroService {

    private final CarroRepository carroRepository;

    public CarroService(CarroRepository carroRepository) {
        this.carroRepository = carroRepository;
    }

    public List<CarroDTO> listarTodos() {
        return carroRepository.findAll().stream().map(this::toDTO).toList();
    }

    public List<CarroDTO> listarDisponiveis() {
        return carroRepository.findByDisponivelTrue().stream().map(this::toDTO).toList();
    }

    public CarroDTO buscarPorId(Long id) {
        return toDTO(carroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carro não encontrado: " + id)));
    }

    public CarroDTO salvar(CarroDTO dto) {
        validarCarro(dto);

        // Impede cadastro de dois carros com a mesma placa
        if (carroRepository.existsByPlaca(dto.placa())) {
            throw new RegraNegocioException("Já existe um carro com a placa: " + dto.placa());
        }
        return toDTO(carroRepository.save(toEntity(dto)));
    }

    public CarroDTO atualizar(Long id, CarroDTO dto) {
        validarCarro(dto);

        Carro carro = carroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carro não encontrado: " + id));
        // Placa não é atualizada para evitar inconsistências com aluguéis existentes
        carro.setMarca(dto.marca());
        carro.setModelo(dto.modelo());
        carro.setAno(dto.ano());
        carro.setValorDiaria(dto.valorDiaria());
        carro.setCategoria(dto.categoria());
        carro.setDisponivel(dto.disponivel());
        return toDTO(carroRepository.save(carro));
    }

    public void deletar(Long id) {
        carroRepository.deleteById(id);
    }

    // ─── Validações de regra de negócio ────────────────────────────────────────

    private void validarCarro(CarroDTO dto) {
        if (dto.marca() == null || dto.marca().isBlank()) {
            throw new RegraNegocioException("A marca do carro é obrigatória.");
        }
        if (dto.modelo() == null || dto.modelo().isBlank()) {
            throw new RegraNegocioException("O modelo do carro é obrigatório.");
        }
        if (dto.placa() == null || dto.placa().isBlank()) {
            throw new RegraNegocioException("A placa do carro é obrigatória.");
        }
        // NOVA REGRA: preço não pode ser nulo nem negativo
        if (dto.valorDiaria() == null) {
            throw new RegraNegocioException("O valor da diária é obrigatório.");
        }
        if (dto.valorDiaria().compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraNegocioException("O valor da diária não pode ser negativo.");
        }
    }

    // ─── Conversão DTO ↔ Entidade ───────────────────────────────────────────────

    private CarroDTO toDTO(Carro c) {
        return new CarroDTO(c.getId(), c.getMarca(), c.getModelo(), c.getPlaca(),
                c.getAno(), c.getValorDiaria(), c.getDisponivel(), c.getCategoria());
    }

    private Carro toEntity(CarroDTO dto) {
        return Carro.builder()
                .id(dto.id())
                .marca(dto.marca())
                .modelo(dto.modelo())
                .placa(dto.placa())
                .ano(dto.ano())
                .valorDiaria(dto.valorDiaria())
                // Garante disponivel = true quando o campo vier null no request
                .disponivel(dto.disponivel() != null ? dto.disponivel() : true)
                .categoria(dto.categoria())
                .build();
    }
}
