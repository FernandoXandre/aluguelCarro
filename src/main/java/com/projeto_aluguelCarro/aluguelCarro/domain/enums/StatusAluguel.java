package com.projeto_aluguelCarro.aluguelCarro.domain.enums;

// Ciclo de vida de um aluguel
public enum StatusAluguel {
    PENDENTE,   // Reservado, ainda não iniciado
    ATIVO,      // Em andamento — carro indisponível
    CONCLUIDO,  // Finalizado normalmente — carro liberado
    CANCELADO   // Interrompido antes do fim — carro liberado
}
