package com.acledabank.vicheak.api.core.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_uuid", unique = true, nullable = false)
    private String uuid;

    @Column(name = "user_email", length = 150, unique = true, nullable = false)
    private String email;

    @Column(name = "user_password", nullable = false)
    private String password;

    private Boolean accountNonExpired;

    private Boolean accountNonLocked;

    private Boolean credentialsNonExpired;

    private Boolean enabled;

}

