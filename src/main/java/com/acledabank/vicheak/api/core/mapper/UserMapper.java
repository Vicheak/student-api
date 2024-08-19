package com.acledabank.vicheak.api.core.mapper;

import com.acledabank.vicheak.api.core.dto.TransactionUserDto;
import com.acledabank.vicheak.api.core.dto.UserDto;
import com.acledabank.vicheak.api.core.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromTransactionUserDtoToUser(TransactionUserDto transactionUserDto);

    @Mapping(target = "isEnabled", source = "enabled")
    UserDto fromUserToUserDto(User user);

    List<UserDto> fromUsersToUserDto(List<User> users);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromTransactionUserDtoToUser(@MappingTarget User user, TransactionUserDto transactionUserDto);

}
