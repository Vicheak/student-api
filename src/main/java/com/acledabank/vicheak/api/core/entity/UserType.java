package com.acledabank.vicheak.api.core.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_types")
public class UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_type_id")
    private Integer id;

    @Column(name = "user_type_role")
    private String role;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

}
