package com.julioneves.requisicao.exception;

public class DuplicateClienteException extends RuntimeException {
    public DuplicateClienteException(String message) {
        super(message);
    }
}