package com.projeto_aluguelCarro.aluguelCarro.exception;

/**
 * Exceção lançada quando uma regra de negócio é violada.
 * Resulta em HTTP 400 Bad Request via GlobalExceptionHandler.
 */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
