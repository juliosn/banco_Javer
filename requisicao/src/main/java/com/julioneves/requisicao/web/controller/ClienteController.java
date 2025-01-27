package com.julioneves.requisicao.web.controller;

import com.julioneves.requisicao.entity.Cliente;
import com.julioneves.requisicao.exception.ClienteNotFoundException;
import com.julioneves.requisicao.exception.InvalidClienteDataException;
import com.julioneves.requisicao.proxy.ClienteProxy;
import com.julioneves.requisicao.util.ScoreUtils;
import com.julioneves.requisicao.web.dto.*;
import com.julioneves.requisicao.web.dto.mapper.ClienteMapper;
import com.julioneves.requisicao.web.exception.ErrorMessage;
import feign.FeignException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Requisição", description = "Contém a requisição de ponte para as operações relativas ao CRUD de um cliente e o cálculo de score de crédito, representando a primeira parte do projeto 'Banco Javer' \n\nEle puxa as requisições da API do Crud de clientes através do Feign Client via Proxy, e as utiliza em seus próprios endpoints")

@Slf4j
@RestController
@RequestMapping("api/v1/conta/clientes")
public class ClienteController {

    private final ClienteProxy clienteProxy;

    public ClienteController(ClienteProxy clienteProxy) {
        this.clienteProxy = clienteProxy;
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
    @PostMapping
    public ResponseEntity<ClienteCreateDto> create(@RequestBody ClienteCreateDto dto) {
        try {
            return clienteProxy.create(dto);
        } catch (FeignException.BadRequest e) {
            throw new InvalidClienteDataException("Dados inválidos fornecidos para a criação do cliente.");
        } catch (Exception e) {
            log.error("Erro inesperado ao criar cliente", e);
            throw new RuntimeException("Erro inesperado ao criar cliente.");
        }
    }

    @Operation(summary = "Obter todos os clientes",
            description = "Recupera uma lista de todos os clientes cadastrados no banco.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> getAll() {
        try {
            return clienteProxy.getAll();
        } catch (FeignException e) {
            log.error("Erro ao recuperar lista de clientes", e);
            throw new RuntimeException("Erro ao recuperar lista de clientes.");
        }
    }

    @Operation(summary = "Obter cliente por ID",
            description = "Recupera os detalhes de um cliente específico com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping(value = "/{id}")
    public ResponseEntity<ClienteResponseDto> getById(@PathVariable Long id) {
        try {
            return clienteProxy.findByID(id);
        } catch (FeignException.NotFound e) {
            throw new ClienteNotFoundException("Cliente com ID " + id + " não encontrado.");
        } catch (FeignException.BadRequest e) {
            throw new InvalidClienteDataException("Dados inválidos para o cliente com ID " + id);
        } catch (Exception e) {
            log.error("Erro ao buscar cliente com ID " + id, e);
            throw new RuntimeException("Erro ao buscar cliente.");
        }
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
    @PutMapping(value = "/{id}")
    public ResponseEntity<ClienteResponseDto> update(@PathVariable Long id, @RequestBody ClienteUpdateDto dto) {
        try {
            return clienteProxy.update(id, dto);
        } catch (FeignException.NotFound e) {
            throw new ClienteNotFoundException("Cliente com ID " + id + " não encontrado para atualização.");
        } catch (FeignException.BadRequest e) {
            throw new InvalidClienteDataException("Dados inválidos fornecidos para atualização do cliente.");
        } catch (Exception e) {
            log.error("Erro ao atualizar cliente com ID " + id, e);
            throw new RuntimeException("Erro ao atualizar cliente.");
        }
    }

    @Operation(summary = "Deletar cliente",
            description = "Remove um cliente do banco de dados com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Cliente deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            return clienteProxy.delete(id);
        } catch (FeignException.NotFound e) {
            throw new ClienteNotFoundException("Cliente com ID " + id + " não encontrado para exclusão.");
        } catch (FeignException.BadRequest e) {
            throw new InvalidClienteDataException("Erro de dados inválidos ao tentar excluir cliente.");
        } catch (Exception e) {
            log.error("Erro ao excluir cliente com ID " + id, e);
            throw new RuntimeException("Erro ao excluir cliente.");
        }
    }

    @Operation(summary = "Calcular Score de Crédito",
            description = "Realizar o cálculo de score de crédito do cliente.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Cálculo realizado com sucesso",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ScoreResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping(value = "/score/{id}", produces = "application/json")
    public ResponseEntity<ScoreResponseDto> calcularScore(@PathVariable Long id) {
        try {
            ClienteResponseDto clienteResponseDto = clienteProxy.findByID(id).getBody();
            if (clienteResponseDto == null) {
                throw new ClienteNotFoundException("Cliente não encontrado para calcular score.");
            }
            log.info("Saldo do cliente: {}", clienteResponseDto.getSaldoCc());
            Cliente cliente = ClienteMapper.toCliente(clienteResponseDto);

            ScoreResponseDto scoreResponseDto = new ScoreResponseDto();
            scoreResponseDto.setScoreCredito(ScoreUtils.calcularScore(cliente.getSaldoCc()));
            log.info("Score de crédito calculado: {}", scoreResponseDto.getScoreCredito());

            return ResponseEntity.ok(scoreResponseDto);
        } catch (FeignException.NotFound e) {
            throw new ClienteNotFoundException("Cliente não encontrado para cálculo de score.");
        } catch (FeignException.BadRequest e) {
            throw new InvalidClienteDataException("Dados inválidos para cálculo de score do cliente.");
        } catch (Exception e) {
            log.error("Erro ao calcular o score do cliente com ID " + id, e);
            throw new RuntimeException("Erro ao calcular o score do cliente.");
        }
    }
}
