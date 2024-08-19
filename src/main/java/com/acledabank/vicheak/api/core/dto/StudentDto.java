package com.acledabank.vicheak.api.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.Range;

@Builder
public record StudentDto(Long studentId,

                         @NotBlank(message = "Student's name must not be blank!")
                         @Size(max = 100, message = "Student's name must be less than 100 characters!")
                         String studentName,

                         @NotNull(message = "Student's age must not be null!")
                         @Range(min = 1, max = 100, message = "Student's age must be between 1 and 100!")
                         Integer studentAge) {
}
