package com.acledabank.vicheak.api.core.mapper;

import com.acledabank.vicheak.api.core.dto.StudentDto;
import com.acledabank.vicheak.api.core.entity.Student;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    Student fromStudentDtoToStudent(StudentDto studentDto);

    StudentDto fromStudentToStudentDto(Student student);

    List<StudentDto> fromStudentToStudentDto(List<Student> students);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromStudentDtoToStudent(@MappingTarget Student student, StudentDto studentDto);

}
