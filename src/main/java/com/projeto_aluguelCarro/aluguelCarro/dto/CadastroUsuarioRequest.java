package com.projeto_aluguelCarro.aluguelCarro.dto;

public record CadastroUsuarioRequest(
        String username,
        String senha,
        String perfil
) {}
