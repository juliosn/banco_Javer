package com.julioneves.api_crud.repository;

import com.julioneves.api_crud.entity.Cliente;
import com.julioneves.api_crud.repository.projection.ClienteProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    @Query("SELECT c.id AS id, c.nome AS nome, c.telefone AS telefone, c.correntista AS correntista, c.saldoCc AS saldoCc FROM Cliente c")
    List<ClienteProjection> findAllBy();
}
