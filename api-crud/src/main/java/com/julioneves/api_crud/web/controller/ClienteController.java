package com.julioneves.api_crud.web.controller;

import com.julioneves.api_crud.entity.Cliente;
import com.julioneves.api_crud.repository.projection.ClienteProjection;
import com.julioneves.api_crud.service.ClienteService;
import com.julioneves.api_crud.web.dto.ClienteCreateDto;
import com.julioneves.api_crud.web.dto.ClienteProjectionDto;
import com.julioneves.api_crud.web.dto.ClienteResponseDto;
import com.julioneves.api_crud.web.dto.ClienteUpdateDto;
import com.julioneves.api_crud.web.dto.mapper.ClienteMapper;
import com.julioneves.api_crud.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.env.Environment;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Clientes", description = "Contém todas as operações relativas ao CRUD de um cliente, representando a segunda aplicação do projeto 'Banco Javer'")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {

    @Autowired
    private Environment environment;

    @Autowired
    private ClienteService clienteService;

    @Operation(summary = "Obter todos os clientes",
            description = "Recupera uma lista de todos os clientes cadastrados no banco.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteProjection.class)))
            })
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ClienteProjectionDto>> getAllClientes() {
        return ResponseEntity.ok(clienteService.getAllClientes());
    }


    @Operation(summary = "Obter cliente por ID",
            description = "Recupera os detalhes de um cliente específico com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ClienteResponseDto> getClienteById(@PathVariable Long id) {
        var port = environment.getProperty("local.server.port");
        Cliente cliente = clienteService.getClienteById(id);
        ClienteResponseDto clienteResponseDto = ClienteMapper.toDto(cliente);
        clienteResponseDto.setPort(port);
        return ResponseEntity.ok(clienteResponseDto);
    }

    @Operation(summary = "Criar um novo cliente",
            description = "Recurso para criar um novo cliente para o banco.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteCreateDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos ou faltando",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<ClienteResponseDto> addCliente(@RequestBody @Valid ClienteCreateDto dto) {
        var port = environment.getProperty("local.server.port");
        Cliente cliente = ClienteMapper.toCliente(dto);
        clienteService.addCliente(cliente);
        ClienteResponseDto clienteResponseDto = ClienteMapper.toDto(cliente);
        clienteResponseDto.setPort(port);
        return ResponseEntity.status(201).body(clienteResponseDto);
    }

    @Operation(summary = "Atualizar dados do cliente",
            description = "Permite a atualização das informações de um cliente existente.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteUpdateDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ClienteResponseDto> updateCliente(@PathVariable Long id, @RequestBody ClienteUpdateDto dto) {
        var port = environment.getProperty("local.server.port");
        Cliente cliente = ClienteMapper.toCliente(dto);

        if (dto.getSaldoCc() != null) {
            try {
                Float saldo_cc = dto.getSaldoCc();
                cliente.setSaldoCc(saldo_cc);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body(null); // Exemplo de resposta de erro
            }
        }

        clienteService.updateCliente(id, cliente);
        ClienteResponseDto clienteResponseDto = ClienteMapper.toDto(cliente);
        clienteResponseDto.setPort(port);
        return ResponseEntity.ok(clienteResponseDto);
    }

    @Operation(summary = "Deletar cliente",
            description = "Remove um cliente do banco de dados com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Cliente deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        clienteService.deleteCliente(id);
        return ResponseEntity.noContent().build();
    }
}
