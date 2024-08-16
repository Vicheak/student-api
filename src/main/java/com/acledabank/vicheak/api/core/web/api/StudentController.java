package com.acledabank.vicheak.api.core.web.api;

import com.acledabank.vicheak.api.core.base.BaseApi;
import com.acledabank.vicheak.api.core.dto.StudentDto;
import com.acledabank.vicheak.api.core.pagination.PageDto;
import com.acledabank.vicheak.api.core.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BaseApi<?> createNewCourse(@RequestBody @Valid StudentDto studentDto) {

        studentDto = studentService.createNewStudent(studentDto);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.CREATED.value())
                .message("A new student has been inserted successfully!")
                .timestamp(LocalDateTime.now())
                .payload(studentDto)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public BaseApi<?> loadAllStudents() {

        List<StudentDto> studentDtoList = studentService.loadAllStudents();

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("All students loaded successfully!")
                .timestamp(LocalDateTime.now())
                .payload(studentDtoList)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public BaseApi<?> loadStudentByUuid(@PathVariable("id") Long studentId) {

        StudentDto studentDto = studentService.loadStudentById(studentId);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("Student with id, %d loaded successfully!".formatted(studentId))
                .timestamp(LocalDateTime.now())
                .payload(studentDto)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public BaseApi<?> updateStudentById(@PathVariable("id") Long studentId,
                                         @RequestBody StudentDto studentDto) {

        studentDto = studentService.updateStudentById(studentId, studentDto);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("A student has been updated successfully!")
                .timestamp(LocalDateTime.now())
                .payload(studentDto)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public BaseApi<?> deleteStudentId(@PathVariable("id") Long studentId) {

        studentService.deleteStudentById(studentId);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.NO_CONTENT.value())
                .message("A student has been deleted successfully!")
                .timestamp(LocalDateTime.now())
                .payload(Map.of("message", "Payload has no content!"))
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/paginate")
    public BaseApi<?> loadPaginatedStudents(@RequestParam(required = false) Map<String, String> requestMap) {

        PageDto pageDto = studentService.loadPaginatedStudents(requestMap);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("Students loaded successfully!")
                .timestamp(LocalDateTime.now())
                .payload(pageDto)
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public BaseApi<?> searchStudents(@RequestParam(required = false) Map<String, Object> requestMap) {

        List<StudentDto> studentDtoList = studentService.searchStudents(requestMap);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("Students searched successfully!")
                .timestamp(LocalDateTime.now())
                .payload(studentDtoList)
                .build();
    }

}