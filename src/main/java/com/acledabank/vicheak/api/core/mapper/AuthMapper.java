package com.acledabank.vicheak.api.core.mapper;

import com.acledabank.vicheak.api.core.dto.RegisterDto;
import com.acledabank.vicheak.api.core.dto.TransactionUserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    TransactionUserDto fromRegisterDtoToTransactionUserDto(RegisterDto registerDto);

}
