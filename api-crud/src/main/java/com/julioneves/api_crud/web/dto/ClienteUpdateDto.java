package com.julioneves.api_crud.web.dto;

public class ClienteUpdateDto {

    private String nome;
    private Long telefone;
    private Boolean correntista;
    private Float saldoCc;

    // Construtor padrão
    public ClienteUpdateDto() {
    }

    // Construtor com todos os atributos
    public ClienteUpdateDto(String nome, Long telefone, Boolean correntista, Float saldoCc) {
        this.nome = nome;
        this.telefone = telefone;
        this.correntista = correntista;
        this.saldoCc = saldoCc;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getTelefone() {
        return telefone;
    }

    public void setTelefone(Long telefone) {
        this.telefone = telefone;
    }

    public Boolean getCorrentista() {
        return correntista;
    }

    public void setCorrentista(Boolean correntista) {
        this.correntista = correntista;
    }

    public Float getSaldoCc() {
        return saldoCc;
    }

    public void setSaldoCc(Float saldoCc) {
        this.saldoCc = saldoCc;
    }
}
