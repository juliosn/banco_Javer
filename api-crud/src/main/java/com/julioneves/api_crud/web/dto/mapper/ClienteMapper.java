package com.julioneves.api_crud.web.dto.mapper;

import com.julioneves.api_crud.entity.Cliente;
import com.julioneves.api_crud.web.dto.ClienteCreateDto;
import com.julioneves.api_crud.web.dto.ClienteResponseDto;
import com.julioneves.api_crud.web.dto.ClienteUpdateDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteMapper {

    public static Cliente toCliente(ClienteCreateDto dto) {
        return new ModelMapper().map(dto, Cliente.class);
    }

    public static ClienteResponseDto toDto(Cliente cliente) {
        return new ModelMapper().map(cliente, ClienteResponseDto.class);
    }

    public static Cliente toCliente(ClienteUpdateDto dto) {
        return new ModelMapper().map(dto, Cliente.class);
    }
}
