package com.acledabank.vicheak.api.core.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_uuid", unique = true, nullable = false)
    private String uuid;

    @Column(name = "user_firstname", length = 50, nullable = false)
    private String firstName;

    @Column(name = "user_lastname", length = 50, nullable = false)
    private String lastName;

    @Column(name = "user_email", length = 150, unique = true, nullable = false)
    private String email;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "user_phone", length = 20, unique = true, nullable = false)
    private String phoneNumber;

    private Boolean verified;

    private String verifiedCode;

    private String passwordToken;

    private Boolean accountNonExpired;

    private Boolean accountNonLocked;

    private Boolean credentialsNonExpired;

    private Boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_type_id", referencedColumnName = "user_type_id"))
    private Set<UserType> roles;

}

