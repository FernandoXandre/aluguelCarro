package com.projeto_aluguelCarro.aluguelCarro.service;

import com.projeto_aluguelCarro.aluguelCarro.domain.Cliente;
import com.projeto_aluguelCarro.aluguelCarro.dto.ClienteDTO;
import com.projeto_aluguelCarro.aluguelCarro.exception.RegraNegocioException;
import com.projeto_aluguelCarro.aluguelCarro.repository.AluguelRepository;
import com.projeto_aluguelCarro.aluguelCarro.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class ClienteService {

    // Regex RFC-5321 simplificada: local@dominio.extensao
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static boolean cpfValido(String cpf) {
        String nums = cpf.replaceAll("\\D", "");
        if (nums.length() != 11 || nums.chars().distinct().count() == 1) return false;
        int d1 = 0, d2 = 0;
        for (int i = 0; i < 9; i++) d1 += (nums.charAt(i) - '0') * (10 - i);
        d1 = (d1 * 10 % 11) % 10;
        for (int i = 0; i < 10; i++) d2 += (nums.charAt(i) - '0') * (11 - i);
        d2 = (d2 * 10 % 11) % 10;
        return d1 == (nums.charAt(9) - '0') && d2 == (nums.charAt(10) - '0');
    }

    private final ClienteRepository clienteRepository;
    private final AluguelRepository aluguelRepository;

    public ClienteService(ClienteRepository clienteRepository, AluguelRepository aluguelRepository) {
        this.clienteRepository = clienteRepository;
        this.aluguelRepository = aluguelRepository;
    }

    public List<ClienteDTO> listarTodos() {
        return clienteRepository.findAll().stream().map(this::toDTO).toList();
    }

    public ClienteDTO buscarPorId(Long id) {
        return toDTO(clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado: " + id)));
    }

    public ClienteDTO salvar(ClienteDTO dto) {
        validarCliente(dto);

        // Impede cadastro de dois clientes com o mesmo CPF
        if (clienteRepository.existsByCpf(dto.cpf())) {
            throw new RegraNegocioException("CPF já cadastrado: " + dto.cpf());
        }
        return toDTO(clienteRepository.save(toEntity(dto)));
    }

    public ClienteDTO atualizar(Long id, ClienteDTO dto) {
        validarCliente(dto);

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado: " + id));
        // CPF não é atualizado para preservar o vínculo com aluguéis existentes
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setTelefone(dto.telefone());
        cliente.setEndereco(dto.endereco());
        cliente.setCnh(dto.cnh());
        return toDTO(clienteRepository.save(cliente));
    }

    public void deletar(Long id) {
        // Bloqueia exclusão se o cliente tiver aluguéis registrados
        if (aluguelRepository.existsByClienteId(id)) {
            throw new RegraNegocioException("Cliente possui aluguéis registrados e não pode ser removido.");
        }
        clienteRepository.deleteById(id);
    }

    // ─── Validações de regra de negócio ────────────────────────────────────────

    private void validarCliente(ClienteDTO dto) {
        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new RegraNegocioException("O nome do cliente é obrigatório.");
        }
        if (dto.cpf() == null || dto.cpf().isBlank()) {
            throw new RegraNegocioException("O CPF do cliente é obrigatório.");
        }
        if (!cpfValido(dto.cpf())) {
            throw new RegraNegocioException("CPF inválido: " + dto.cpf());
        }
        // NOVA REGRA: e-mail deve ter formato válido (quando informado)
        if (dto.email() != null && !dto.email().isBlank()) {
            if (!EMAIL_PATTERN.matcher(dto.email()).matches()) {
                throw new RegraNegocioException("E-mail inválido: " + dto.email());
            }
        }
    }

    // ─── Conversão DTO ↔ Entidade ───────────────────────────────────────────────

    private ClienteDTO toDTO(Cliente c) {
        return new ClienteDTO(c.getId(), c.getNome(), c.getCpf(),
                c.getEmail(), c.getTelefone(), c.getEndereco(), c.getCnh());
    }

    private Cliente toEntity(ClienteDTO dto) {
        return Cliente.builder()
                .id(dto.id())
                .nome(dto.nome())
                .cpf(dto.cpf())
                .email(dto.email())
                .telefone(dto.telefone())
                .endereco(dto.endereco())
                .cnh(dto.cnh())
                .build();
    }
}
