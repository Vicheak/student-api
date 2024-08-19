package com.acledabank.vicheak.api.core.service;

import com.acledabank.vicheak.api.core.dto.ChangePasswordDto;
import com.acledabank.vicheak.api.core.dto.TransactionUserDto;
import com.acledabank.vicheak.api.core.dto.UserDto;

import java.util.List;

public interface UserService {

    /**
     * This method is used to create new user resource into the system
     * @param transactionUserDto is the request from client
     */
    void createNewUser(TransactionUserDto transactionUserDto);

    /**
     * This method is used to load all the user resources from the system
     * @return List<UserDto>
     */
    List<UserDto> loadAllUsers();

    /**
     * This method is used to load specific user by uuid
     * @param uuid is the path parameter from client
     * @return UserDto
     */
    UserDto loadUserByUuid(String uuid);

    /**
     * This method is used to update specific user by uuid
     * @param uuid is the path parameter from client
     * @param transactionUserDto is the request from client
     */
    void updateUserByUuid(String uuid, TransactionUserDto transactionUserDto);

    /**
     * This method is used to disable specific user by uuid from the system
     * @param uuid is the path parameter from client
     * @param isEnabled is the request from client
     */
    void updateUserIsEnabledByUuid(String uuid, Boolean isEnabled);

    /**
     * This method is used to delete specific user by uuid from the system
     * @param uuid is the path parameter from client
     */
    void deleteUserByUuid(String uuid);

    /**
     * This method is used to load user profile by authenticated user
     * @return UserDto
     */
    UserDto loadUserProfile();

    /**
     * This method is used to change password of the authenticated user
     * @param uuid is the path parameter from client
     * @param changePasswordDto is the request from client
     */
    void changePassword(String uuid, ChangePasswordDto changePasswordDto);

}
