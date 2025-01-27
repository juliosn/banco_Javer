package com.julioneves.requisicao.web.dto;

public class ScoreResponseDto {
    private Float scoreCredito;

    // Construtor padrão (sem argumentos)
    public ScoreResponseDto() {
    }

    // Construtor com argumento
    public ScoreResponseDto(Float scoreCredito) {
        this.scoreCredito = scoreCredito;
    }

    // Getter
    public Float getScoreCredito() {
        return scoreCredito;
    }

    // Setter
    public void setScoreCredito(Float scoreCredito) {
        this.scoreCredito = scoreCredito;
    }
}
