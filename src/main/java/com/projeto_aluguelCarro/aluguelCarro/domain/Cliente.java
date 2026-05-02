package com.projeto_aluguelCarro.aluguelCarro.domain;

import jakarta.persistence.*;
import lombok.*;

// Entidade que representa o locatário do veículo
@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    // CPF único — validado no ClienteService antes de salvar
    @Column(unique = true, nullable = false)
    private String cpf;

    private String email;

    private String telefone;

    private String endereco;

    // Carteira Nacional de Habilitação
    private String cnh;
}
