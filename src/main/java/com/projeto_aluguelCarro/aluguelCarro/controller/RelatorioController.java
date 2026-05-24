package com.projeto_aluguelCarro.aluguelCarro.controller;

import com.projeto_aluguelCarro.aluguelCarro.dto.RelatorioMensalDTO;
import com.projeto_aluguelCarro.aluguelCarro.service.AluguelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Endpoints de relatórios e representação gráfica dos dados.
 */
@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final AluguelService aluguelService;

    public RelatorioController(AluguelService aluguelService) {
        this.aluguelService = aluguelService;
    }

    /**
     * Retorna os 12 meses do ano com quantidade de aluguéis e receita por mês.
     * Estrutura pronta para alimentar gráficos de barras ou linhas no frontend.
     *
     * <p>Exemplo: GET /relatorios/alugueis-anuais?ano=2026
     *
     * <p>Resposta:
     * <pre>
     * [
     *   {"mes": 1, "nomeMes": "janeiro",  "totalAlugueis": 5,  "receitaTotal": 2500.00},
     *   {"mes": 2, "nomeMes": "fevereiro", "totalAlugueis": 3,  "receitaTotal": 1200.00},
     *   ...
     *   {"mes": 12, "nomeMes": "dezembro", "totalAlugueis": 0, "receitaTotal": 0.00}
     * ]
     * </pre>
     *
     * @param ano Ano desejado; padrão: ano atual
     */
    @GetMapping("/alugueis-anuais")
    public List<RelatorioMensalDTO> relatorioAnual(
            @RequestParam(required = false) Integer ano) {
        // Se não informar o ano, usa o ano corrente
        int anoConsulta = (ano != null) ? ano : LocalDate.now().getYear();
        return aluguelService.relatorioAnual(anoConsulta);
    }
}
