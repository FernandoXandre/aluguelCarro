package com.projeto_aluguelCarro.aluguelCarro.domain;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entidade que representa um veículo da frota
@Entity
@Table(name = "carros")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Carro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    // Placa única — validada no CarroService antes de salvar
    @Column(unique = true, nullable = false)
    private String placa;

    private Integer ano;

    // Usado para calcular o valorTotal do aluguel (dias × valorDiaria)
    @Column(nullable = false)
    private BigDecimal valorDiaria;

    // Controlado pelo AluguelService: false ao alugar, true ao concluir/cancelar
    @Builder.Default
    private Boolean disponivel = true;

    private String categoria;
}
