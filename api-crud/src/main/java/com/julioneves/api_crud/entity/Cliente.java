package com.julioneves.api_crud.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String nome;

    @Column
    private Long telefone;

    @Column
    private Boolean correntista;

    @Column(name = "saldoCc")
    private Float saldoCc;

    // Construtor padrão
    public Cliente() {
    }

    // Construtor com parâmetros
    public Cliente(String nome, Long telefone, Boolean correntista, Float saldoCc) {
        this.nome = nome;
        this.telefone = telefone;
        this.correntista = correntista;
        this.saldoCc = saldoCc;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
