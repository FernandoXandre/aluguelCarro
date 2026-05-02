package com.projeto_aluguelCarro.aluguelCarro.domain;

import com.projeto_aluguelCarro.aluguelCarro.domain.enums.PerfilUsuario;
import jakarta.persistence.*;
import lombok.*;

// Entidade que representa um operador do sistema (atendente ou administrador)
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Username único — validado no UsuarioService antes de salvar
    @Column(unique = true, nullable = false)
    private String username;

    // Senha armazenada em texto puro (sem hash); melhoria futura com Spring Security
    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    private PerfilUsuario perfil;
}
