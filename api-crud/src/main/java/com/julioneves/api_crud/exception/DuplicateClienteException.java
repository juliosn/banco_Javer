package com.julioneves.api_crud.exception;

public class DuplicateClienteException extends RuntimeException {
    public DuplicateClienteException(String message) {
        super(message);
    }
}