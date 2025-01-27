package com.julioneves.requisicao.web.dto.mapper;

import com.julioneves.requisicao.entity.Cliente;
import com.julioneves.requisicao.web.dto.ClienteResponseDto;
import org.modelmapper.ModelMapper;

public class ClienteMapper {
    private ClienteMapper() {
    }

    public static Cliente toCliente(ClienteResponseDto dto) {
        return new ModelMapper().map(dto, Cliente.class);
    }
}
