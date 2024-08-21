package com.acledabank.vicheak.api.core.web.api;

import com.acledabank.vicheak.api.core.base.BaseApi;
import com.acledabank.vicheak.api.core.dto.*;
import com.acledabank.vicheak.api.core.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BaseApi<Object> createNewUser(@RequestBody @Valid TransactionUserDto transactionUserDto) {

        userService.createNewUser(transactionUserDto);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.CREATED.value())
                .message("A new user has been added successfully!")
                .timestamp(LocalDateTime.now())
                .payload("User created! No content!")
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public BaseApi<Object> loadAllUsers() {

        List<UserDto> userDtoList = userService.loadAllUsers();

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("All users loaded successfully!")
                .timestamp(LocalDateTime.now())
                .payload(userDtoList)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{uuid}")
    public BaseApi<Object> loadUserByUuid(@PathVariable String uuid) {

        UserDto userDto = userService.loadUserByUuid(uuid);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("User with uuid, %s loaded successfully!".formatted(uuid))
                .timestamp(LocalDateTime.now())
                .payload(userDto)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{uuid}")
    public BaseApi<Object> updateUserByUuid(@PathVariable String uuid,
                                       @RequestBody TransactionUserDto transactionUserDto) {

        userService.updateUserByUuid(uuid, transactionUserDto);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("A user has been updated successfully!")
                .timestamp(LocalDateTime.now())
                .payload("User updated! No content!")
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{uuid}")
    public BaseApi<Object> updateUserIsEnabledByUuid(@PathVariable String uuid,
                                          @RequestBody @Valid IsEnabledDto isEnabledDto) {

        userService.updateUserIsEnabledByUuid(uuid, isEnabledDto.isEnabled());

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("A user status has been updated successfully!")
                .timestamp(LocalDateTime.now())
                .payload("User updated! No content!")
                .build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{uuid}")
    public BaseApi<Object> deleteUserByUuid(@PathVariable String uuid) {

        userService.deleteUserByUuid(uuid);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.NO_CONTENT.value())
                .message("A user has been deleted successfully!")
                .timestamp(LocalDateTime.now())
                .payload("User deleted! No content!")
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me")
    public BaseApi<Object> loadUserProfile() {

        UserDto userDto = userService.loadUserProfile();

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("User profile loaded successfully!")
                .timestamp(LocalDateTime.now())
                .payload(userDto)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/change-password/{uuid}")
    public Map<String, String> changePassword(@PathVariable String uuid,
                                              @RequestBody @Valid ChangePasswordDto changePasswordDto){
        userService.changePassword(uuid, changePasswordDto);
        return Map.of("message", "The account's password has been changed successfully!");
    }

}
