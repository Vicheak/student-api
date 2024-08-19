package com.acledabank.vicheak.api.core.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record UserDto(String uuid,
                      String firstName,
                      String lastName,
                      String email,
                      String phoneNumber,
                      Boolean isEnabled,
                      Set<UserTypeDto> roles) {
}
