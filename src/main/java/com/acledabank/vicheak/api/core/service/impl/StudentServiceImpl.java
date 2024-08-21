package com.acledabank.vicheak.api.core.service.impl;

import com.acledabank.vicheak.api.core.dto.StudentDto;
import com.acledabank.vicheak.api.core.entity.Student;
import com.acledabank.vicheak.api.core.mapper.StudentMapper;
import com.acledabank.vicheak.api.core.pagination.LoadPageable;
import com.acledabank.vicheak.api.core.pagination.PageDto;
import com.acledabank.vicheak.api.core.repository.StudentRepository;
import com.acledabank.vicheak.api.core.service.StudentService;
import com.acledabank.vicheak.api.core.spec.StudentFilter;
import com.acledabank.vicheak.api.core.spec.StudentSpec;
import com.acledabank.vicheak.api.core.util.SortUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    private final static String defaultNotFoundMessage = "Student with id, %d has not been found in the system!";

    @Transactional
    @Override
    public StudentDto createNewStudent(StudentDto studentDto) {
        Student newStudent = studentRepository.save(studentMapper.fromStudentDtoToStudent(studentDto));
        return studentMapper.fromStudentToStudentDto(newStudent);
    }

    @Override
    public List<StudentDto> loadAllStudents() {
        return studentMapper.fromStudentToStudentDto(studentRepository.findAll());
    }

    @Override
    public StudentDto loadStudentById(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, defaultNotFoundMessage
                                        .formatted(studentId))
                );
        return studentMapper.fromStudentToStudentDto(student);
    }

    @Transactional
    @Override
    public StudentDto updateStudentById(Long studentId, StudentDto studentDto) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, defaultNotFoundMessage
                                        .formatted(studentId))
                );

        //validate student age
        if (Objects.nonNull(studentDto.studentAge()) && (studentDto.studentAge() < 0 || studentDto.studentAge() > 100))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Student's age must be between 1 and 100");

        studentMapper.fromStudentDtoToStudent(student, studentDto);
        student = studentRepository.save(student);
        return studentMapper.fromStudentToStudentDto(student);
    }

    @Transactional
    @Override
    public void deleteStudentById(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, defaultNotFoundMessage
                                        .formatted(studentId))
                );
        studentRepository.delete(student);
    }

    @Override
    public PageDto loadPaginatedStudents(Map<String, String> requestMap) {
        Pageable pageable = LoadPageable.loadPageable(requestMap);

        Page<Student> pages = studentRepository.findAll(pageable);

        List<StudentDto> studentDtoList = studentMapper.fromStudentToStudentDto(pages.getContent());

        return new PageDto(studentDtoList, pages);
    }

    @Override
    public List<StudentDto> searchStudents(Map<String, Object> requestMap) {
        //extract the data from request map
        StudentFilter.StudentFilterBuilder studentFilterBuilder = StudentFilter.builder();

        if (requestMap.containsKey("studentName"))
            studentFilterBuilder.studentName(requestMap.get("studentName").toString());

        if (requestMap.containsKey("studentAge"))
            studentFilterBuilder.studentAge(Integer.parseInt(requestMap.get("studentAge").toString()));

        //set default direction
        String direction = "asc";
        if (requestMap.containsKey(SortUtil.DIRECTION.getLabel()))
            direction = requestMap.get(SortUtil.DIRECTION.getLabel()).toString();

        //set default property
        String field = "";
        if (requestMap.containsKey(SortUtil.FIELD.getLabel()))
            field = requestMap.get(SortUtil.FIELD.getLabel()).toString();

        List<Student> students = studentRepository.findAll(
                StudentSpec.builder()
                        .studentFilter(studentFilterBuilder.build())
                        .build(),
                Sort.by(
                        direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
                        field.isEmpty() ? "studentId" : field
                )
        );

        return studentMapper.fromStudentToStudentDto(students);
    }

}
