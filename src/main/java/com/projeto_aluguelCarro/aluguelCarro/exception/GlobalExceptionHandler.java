package com.projeto_aluguelCarro.aluguelCarro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Intercepta exceções lançadas pelos controllers/services e
 * devolve respostas HTTP padronizadas com status e mensagem de erro.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Regras de negócio violadas → 400 Bad Request
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Map<String, Object>> handleRegraNegocio(RegraNegocioException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "status", 400,
                "erro", "Regra de negócio violada",
                "mensagem", ex.getMessage(),
                "timestamp", LocalDateTime.now().toString()
        ));
    }

    // Recursos não encontrados (RuntimeException genérica) → 404 Not Found
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        String msg = ex.getMessage() != null ? ex.getMessage() : "Erro interno.";
        // Heurística: mensagens de "não encontrado" viram 404
        int status = msg.toLowerCase().contains("não encontrado") ? 404 : 500;
        return ResponseEntity.status(status).body(Map.of(
                "status", status,
                "erro", status == 404 ? "Recurso não encontrado" : "Erro interno",
                "mensagem", msg,
                "timestamp", LocalDateTime.now().toString()
        ));
    }
}
