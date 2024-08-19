package com.acledabank.vicheak.api.core.repository;

import com.acledabank.vicheak.api.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByUuid(String uuid);

    Optional<User> findByEmailAndVerifiedTrueAndEnabledTrue(String email);

    @Modifying
    @Query("UPDATE User AS u SET u.verifiedCode = :verifiedCode WHERE u.email = :email")
    void updateVerifiedCode(String email, String verifiedCode);

    Optional<User> findByEmailAndVerifiedCodeAndVerifiedFalseAndEnabledFalse(String email, String verifiedCode);

}
