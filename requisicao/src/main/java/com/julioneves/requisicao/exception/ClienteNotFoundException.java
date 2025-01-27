package com.julioneves.requisicao.exception;

public class ClienteNotFoundException extends RuntimeException {

    public ClienteNotFoundException(String message) {
        super(message);
    }
}