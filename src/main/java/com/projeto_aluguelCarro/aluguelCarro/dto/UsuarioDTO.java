package com.projeto_aluguelCarro.aluguelCarro.dto;

import com.projeto_aluguelCarro.aluguelCarro.domain.enums.PerfilUsuario;

// DTO de saída para usuários — a senha é omitida intencionalmente
public record UsuarioDTO(
        Long id,
        String username,
        PerfilUsuario perfil
) {}
