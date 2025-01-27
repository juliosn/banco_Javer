package com.julioneves.api_crud.repository.projection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public interface ClienteProjection {
    Long getId();
    String getNome();
    Long getTelefone();
    Boolean getCorrentista();
    Float getSaldoCc();
}
