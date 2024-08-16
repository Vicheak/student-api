package com.acledabank.vicheak.api.core.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stu_id")
    private Long studentId;

    @Column(name = "stu_name", length = 100, nullable = false)
    private String studentName;

    @Column(name = "stu_age", nullable = false)
    private Integer studentAge;

}
