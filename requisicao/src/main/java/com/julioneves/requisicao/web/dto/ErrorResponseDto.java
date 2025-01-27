package com.julioneves.requisicao.web.dto;

public class ErrorResponseDto {
    private String error;
    private String message;

    public ErrorResponseDto(String error, String message) {
        this.error = error;
        this.message = message;
    }

    // Getters e Setters
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
