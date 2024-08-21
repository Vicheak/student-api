package com.acledabank.vicheak.api.core.service.impl;

import com.acledabank.vicheak.api.core.dto.ChangePasswordDto;
import com.acledabank.vicheak.api.core.dto.TransactionUserDto;
import com.acledabank.vicheak.api.core.dto.UserDto;
import com.acledabank.vicheak.api.core.entity.User;
import com.acledabank.vicheak.api.core.entity.UserType;
import com.acledabank.vicheak.api.core.mapper.UserMapper;
import com.acledabank.vicheak.api.core.repository.UserRepository;
import com.acledabank.vicheak.api.core.repository.UserTypeRepository;
import com.acledabank.vicheak.api.core.security.SecurityContextHelper;
import com.acledabank.vicheak.api.core.service.UserService;
import com.acledabank.vicheak.api.core.util.FormatUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SecurityContextHelper securityContextHelper;

    private final static String defaultNotFoundMessage = "User with uuid, %s has not been found in the system!";

    @Transactional
    @Override
    public void createNewUser(TransactionUserDto transactionUserDto) {
        validateTransactionUserDto(transactionUserDto);

        //check roles if exist
        boolean allExisted = transactionUserDto.roleIds().stream()
                .allMatch(userTypeRepository::existsById);

        if (!allExisted)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Role is not valid in the system! please check!");

        User user = setupNewUser(transactionUserDto);
        user.setVerified(true);
        user.setEnabled(true);

        updateUserRolesTransaction(user, transactionUserDto.roleIds());

        userRepository.save(user);
    }

    @Override
    public List<UserDto> loadAllUsers() {
        return userMapper.fromUsersToUserDto(userRepository.findAll());
    }

    @Override
    public UserDto loadUserByUuid(String uuid) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, defaultNotFoundMessage
                                        .formatted(uuid))
                );

        return userMapper.fromUserToUserDto(user);
    }

    @Transactional
    @Override
    public void updateUserByUuid(String uuid, TransactionUserDto transactionUserDto) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, defaultNotFoundMessage
                                        .formatted(uuid))
                );

        //check security context holder
        checkSecurityOperation(user);

        //check email if already exist
        checkEmailValidation(transactionUserDto.email(), user.getEmail());

        //check phone number format and existence
        checkPhoneNumberValidation(transactionUserDto.phoneNumber(), user.getPhoneNumber());

        //map from dto to entity but except the null value from dto
        userMapper.fromTransactionUserDtoToUser(user, transactionUserDto);

        //check if user is an admin
        User authenticatedUser = securityContextHelper.loadAuthenticatedUser();
        setUpRolesForUser(authenticatedUser, user, transactionUserDto.roleIds());

        userRepository.save(user);
    }

    @Transactional
    @Override
    public void updateUserIsEnabledByUuid(String uuid, Boolean isEnabled) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, defaultNotFoundMessage
                                        .formatted(uuid))
                );

        user.setEnabled(isEnabled);

        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUserByUuid(String uuid) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, defaultNotFoundMessage
                                        .formatted(uuid))
                );

        //check security context holder
        checkAdminRemoveOperation(user);

        userRepository.delete(user);
    }

    @Override
    public UserDto loadUserProfile() {
        User authenticatedUser = securityContextHelper.loadAuthenticatedUser();
        return userMapper.fromUserToUserDto(authenticatedUser);
    }

    @Transactional
    @Override
    public void changePassword(String uuid, ChangePasswordDto changePasswordDto) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, defaultNotFoundMessage
                                        .formatted(uuid))
                );

        checkSecurityOperation(user);

        if(!changePasswordDto.password().equals(changePasswordDto.passwordConfirmation()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Password and password confirmation must be matched!");

        if(!passwordEncoder.matches(changePasswordDto.oldPassword(), user.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Incorrect password, try again later!");

        user.setPassword(passwordEncoder.encode(changePasswordDto.password()));
        userRepository.save(user);
    }

    public void validateTransactionUserDto(TransactionUserDto transactionUserDto) {
        //check email if already exist
        if (userRepository.existsByEmailIgnoreCase(transactionUserDto.email()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Email conflicts resource in the system!");

        //check phone number format
        if (!FormatUtil.checkPhoneFormat(transactionUserDto.phoneNumber()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Phone Number is not in a valid format!");

        //check phone number is already exist
        if (userRepository.existsByPhoneNumber(transactionUserDto.phoneNumber()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Phone Number conflicts resource in the system!");
    }

    public User setupNewUser(TransactionUserDto transactionUserDto) {
        //map from dto to entity
        User user = userMapper.fromTransactionUserDtoToUser(transactionUserDto);
        user.setUuid(UUID.randomUUID().toString());
        user.setVerified(false);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }

    private void checkSecurityOperation(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = securityContextHelper.loadAuthenticatedUser();
        SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority("SCOPE_ADMIN");

        if (authentication.getAuthorities().contains(adminAuthority))
            return;

        if (!user.getUuid().equals(authenticatedUser.getUuid()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Permission denied!");
    }

    private boolean checkIfUserIsADMIN(User user) {
        Set<UserType> roles = user.getRoles();
        for (UserType role : roles)
            if (role.getRole().equals("ADMIN")) return true;
        return false;
    }

    public void updateUserRolesTransaction(User user, Set<Integer> roleIds) {
        Set<UserType> roles = new HashSet<>();

        roleIds.forEach(roleId -> roles.add(UserType.builder()
                .id(roleId)
                .build()));

        user.setRoles(roles);
    }

    private void checkAdminRemoveOperation(User user) {
        User authenticatedUser = securityContextHelper.loadAuthenticatedUser();

        if (user.getUuid().equals(authenticatedUser.getUuid()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "This is considered self-removed operation, permission denied!");
    }

    private void checkEmailValidation(String passedEmail, String userEmail){
        if(Objects.nonNull(passedEmail) && (!passedEmail.equalsIgnoreCase(userEmail) &&
                userRepository.existsByEmailIgnoreCase(passedEmail)))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Email conflicts resource in the system!");
    }

    private void checkPhoneNumberValidation(String passedPhoneNumber, String userPhoneNumber){
        if (Objects.nonNull(passedPhoneNumber)) {
            if (!FormatUtil.checkPhoneFormat(passedPhoneNumber))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Phone Number is not in a valid format!");

            if (!passedPhoneNumber.equals(userPhoneNumber) &&
                    userRepository.existsByPhoneNumber(passedPhoneNumber))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Phone Number conflicts resource in the system!");
        }
    }

    private void setUpRolesForUser(User authenticatedUser, User forUser, Set<Integer> roleIds){
        if (checkIfUserIsADMIN(authenticatedUser) && Objects.nonNull(roleIds))
            //check roles if exist
            if (roleIds.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "User must have at least one role!");
            } else {
                boolean allExisted = roleIds.stream()
                        .allMatch(userTypeRepository::existsById);

                if (!allExisted) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Role is not valid in the system! please check!");
                } else {
                    updateUserRolesTransaction(forUser, roleIds);
                }
            }
    }

}
