package com.projeto_aluguelCarro.aluguelCarro.domain;

import com.projeto_aluguelCarro.aluguelCarro.domain.enums.StatusAluguel;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

// Entidade que representa o contrato de locação entre um cliente e um carro
@Entity
@Table(name = "alugueis")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aluguel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Column(nullable = false)
    private LocalDate dataFim;

    // Calculado automaticamente no AluguelService: dias × valorDiaria
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    private StatusAluguel status;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "carro_id", nullable = false)
    private Carro carro;

    // Usuário que registrou o aluguel; opcional (pode ser null)
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
