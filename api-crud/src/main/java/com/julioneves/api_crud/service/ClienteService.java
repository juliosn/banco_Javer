package com.julioneves.api_crud.service;

import com.julioneves.api_crud.entity.Cliente;
import com.julioneves.api_crud.exception.ClienteNotFoundException;
import com.julioneves.api_crud.exception.InvalidClienteDataException;
import com.julioneves.api_crud.repository.ClienteRepository;
import com.julioneves.api_crud.web.dto.ClienteProjectionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {

    private void validarCliente(Cliente cliente) {
        List<String> erros = new ArrayList<>();

        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            erros.add("Nome não pode ser nulo ou vazio.");
        }
        if (cliente.getTelefone() == null || String.valueOf(cliente.getTelefone()).length() < 10 || String.valueOf(cliente.getTelefone()).length() > 15) {
            erros.add("Telefone não pode ser nulo e/ou precisa ser válido (ter entre 10 e 15 dígitos).");
        }
        if (cliente.getCorrentista() == null) {
            erros.add("Correntista não pode ser nulo.");
        }
        if (cliente.getSaldoCc() == null || cliente.getSaldoCc() < 0) {
            erros.add("Saldo não pode ser nulo ou negativo.");
        }

        if (!erros.isEmpty()) {
            throw new InvalidClienteDataException("O envio possui os seguintes problemas: \n" + String.join("\n", erros));
        }
    }

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<ClienteProjectionDto> getAllClientes() {
        return clienteRepository.findAllBy().stream()
                .map(projection -> {
                    ClienteProjectionDto dto = new ClienteProjectionDto();
                    dto.setId(projection.getId());
                    dto.setNome(projection.getNome());
                    dto.setTelefone(projection.getTelefone());
                    dto.setCorrentista(projection.getCorrentista());
                    dto.setSaldoCc(projection.getSaldoCc());
                    return dto;
                })
                .toList();
    }


    @Transactional(readOnly = true)
    public Cliente getClienteById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente com ID " + id + " não encontrado"));
    }

    @Transactional
    public Cliente addCliente(Cliente cliente) {
        validarCliente(cliente);
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente updateCliente(Long id, Cliente cliente) {
        if (cliente.getId() != null && !cliente.getId().equals(id)) {
            throw new InvalidClienteDataException("O ID no corpo da requisição não corresponde ao ID da URL.");
        }
        Cliente existingCliente = getClienteById(id);
        existingCliente.setNome(cliente.getNome());
        existingCliente.setTelefone(cliente.getTelefone());
        existingCliente.setCorrentista(cliente.getCorrentista());
        existingCliente.setSaldoCc(cliente.getSaldoCc());
        validarCliente(existingCliente);
        return existingCliente;
    }

    @Transactional
    public void deleteCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ClienteNotFoundException("Cliente com ID " + id + " não encontrado");
        }
        clienteRepository.deleteById(id);
    }
}
