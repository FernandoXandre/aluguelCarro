package com.projeto_aluguelCarro.aluguelCarro.service;

import com.projeto_aluguelCarro.aluguelCarro.domain.*;
import com.projeto_aluguelCarro.aluguelCarro.domain.enums.StatusAluguel;
import com.projeto_aluguelCarro.aluguelCarro.dto.AluguelDTO;
import com.projeto_aluguelCarro.aluguelCarro.dto.RelatorioMensalDTO;
import com.projeto_aluguelCarro.aluguelCarro.exception.RegraNegocioException;
import com.projeto_aluguelCarro.aluguelCarro.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class AluguelService {

    /**
     * Número mínimo de carros que devem permanecer disponíveis na frota.
     * Configurável em application.properties: aluguel.estoque.minimo=1
     * Padrão: 1 (sempre mantém ao menos um carro disponível).
     */
    @Value("${aluguel.estoque.minimo:1}")
    private int estoqueMinimo;

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

    // ─── Consultas ─────────────────────────────────────────────────────────────

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

    // ─── Relatório anual (representação gráfica) ───────────────────────────────

    /**
     * Retorna os 12 meses do ano com totalAlugueis e receitaTotal por mês.
     * Meses sem movimento recebem valores zerados — ideal para gráficos de barras/linhas.
     */
    public List<RelatorioMensalDTO> relatorioAnual(int ano) {
        LocalDate inicio = LocalDate.of(ano, 1, 1);
        LocalDate fim = LocalDate.of(ano, 12, 31);
        List<Aluguel> alugueis = aluguelRepository.findByDataInicioBetween(inicio, fim);

        // Agrupa aluguéis pelo número do mês (1–12)
        // Cast explícito para Integer necessário — sem ele o compilador infere Object
        Map<Integer, List<Aluguel>> porMes = alugueis.stream()
                .collect(Collectors.groupingBy(a -> (Integer) a.getDataInicio().getMonthValue()));

        return IntStream.rangeClosed(1, 12).mapToObj(mes -> {
            List<Aluguel> doMes = porMes.getOrDefault(mes, List.of());
            BigDecimal receita = doMes.stream()
                    .map(Aluguel::getValorTotal)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            String nomeMes = Month.of(mes).getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pt-BR"));
            return new RelatorioMensalDTO(mes, nomeMes, doMes.size(), receita);
        }).toList();
    }

    // ─── Criação e ciclo de vida do aluguel ────────────────────────────────────

    @Transactional
    public AluguelDTO criar(AluguelDTO dto) {
        // NOVA REGRA: não permitir aluguel sem os campos obrigatórios (sem "itens")
        validarCamposObrigatorios(dto);

        Carro carro = carroRepository.findById(dto.carroId())
                .orElseThrow(() -> new RuntimeException("Carro não encontrado: " + dto.carroId()));

        // Carro só pode ser alugado se estiver disponível
        if (!carro.getDisponivel()) {
            throw new RegraNegocioException("Carro não está disponível para aluguel.");
        }

        // Verifica estoque mínimo antes de permitir o aluguel
        long disponiveis = carroRepository.countByDisponivelTrue();
        if (disponiveis <= estoqueMinimo) {
            throw new RegraNegocioException(
                    "Estoque insuficiente: restam apenas " + disponiveis
                    + " carro(s) disponível(is) e o mínimo obrigatório é " + estoqueMinimo + ".");
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
            throw new RegraNegocioException("A data fim deve ser posterior à data de início.");
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

    @Transactional
    public AluguelDTO concluir(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado: " + id));
        aluguel.setStatus(StatusAluguel.CONCLUIDO);
        // Libera o carro para novos aluguéis
        aluguel.getCarro().setDisponivel(true);
        carroRepository.save(aluguel.getCarro());
        return toDTO(aluguelRepository.save(aluguel));
    }

    @Transactional
    public AluguelDTO cancelar(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado: " + id));
        aluguel.setStatus(StatusAluguel.CANCELADO);
        // Libera o carro para novos aluguéis
        aluguel.getCarro().setDisponivel(true);
        carroRepository.save(aluguel.getCarro());
        return toDTO(aluguelRepository.save(aluguel));
    }

    // ─── Validações internas ────────────────────────────────────────────────────

    /**
     * Garante que todos os campos obrigatórios estejam presentes.
     * Um aluguel sem cliente, sem carro ou sem datas não pode ser criado.
     */
    private void validarCamposObrigatorios(AluguelDTO dto) {
        if (dto.clienteId() == null) {
            throw new RegraNegocioException("O cliente é obrigatório para criar um aluguel.");
        }
        if (dto.carroId() == null) {
            throw new RegraNegocioException("O carro é obrigatório para criar um aluguel.");
        }
        if (dto.dataInicio() == null) {
            throw new RegraNegocioException("A data de início é obrigatória.");
        }
        if (dto.dataFim() == null) {
            throw new RegraNegocioException("A data de fim é obrigatória.");
        }
    }

    // ─── Conversão DTO ↔ Entidade ───────────────────────────────────────────────

    private AluguelDTO toDTO(Aluguel a) {
        return new AluguelDTO(
                a.getId(), a.getDataInicio(), a.getDataFim(), a.getValorTotal(), a.getStatus(),
                a.getCliente().getId(), a.getCarro().getId(),
                // usuarioId pode ser null se nenhum atendente foi vinculado ao aluguel
                a.getUsuario() != null ? a.getUsuario().getId() : null
        );
    }
}
