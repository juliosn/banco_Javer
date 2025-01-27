package com.julioneves.requisicao.proxy;

import com.julioneves.requisicao.web.dto.ClienteCreateDto;
import com.julioneves.requisicao.web.dto.ClienteResponseDto;
import com.julioneves.requisicao.web.dto.ClienteUpdateDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "clienteProxy", url = "http://localhost:8081/api/v1/clientes")
public interface ClienteProxy {

    @PostMapping
    public ResponseEntity<ClienteCreateDto> create(@RequestBody @Valid ClienteCreateDto dto);

    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> getAll();

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> findByID(@PathVariable Long id);

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> update(@PathVariable Long id, @RequestBody @Valid ClienteUpdateDto dto);

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id);
}
