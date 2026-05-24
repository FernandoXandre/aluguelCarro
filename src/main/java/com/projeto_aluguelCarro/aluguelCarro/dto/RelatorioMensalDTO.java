package com.projeto_aluguelCarro.aluguelCarro.dto;

import java.math.BigDecimal;

/**
 * DTO de saída para o relatório anual de aluguéis.
 * Retorna um registro por mês, com totais e receita — pronto para uso em gráficos.
 *
 * @param mes           Número do mês (1=Janeiro ... 12=Dezembro)
 * @param nomeMes       Nome por extenso em português (ex: "Janeiro")
 * @param totalAlugueis Quantidade de aluguéis iniciados no mês
 * @param receitaTotal  Soma dos valorTotal de todos os aluguéis do mês
 */
public record RelatorioMensalDTO(
        int mes,
        String nomeMes,
        int totalAlugueis,
        BigDecimal receitaTotal
) {}
