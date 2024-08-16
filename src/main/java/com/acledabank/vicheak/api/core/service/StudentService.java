package com.acledabank.vicheak.api.core.service;

import com.acledabank.vicheak.api.core.dto.StudentDto;
import com.acledabank.vicheak.api.core.pagination.PageDto;

import java.util.List;
import java.util.Map;

public interface StudentService {

    /**
     * This method is used to create new student resource into the system
     * @param studentDto is the request from client
     * @return StudentDto
     */
    StudentDto createNewStudent(StudentDto studentDto);

    /**
     * This method is used to load all student resources from the system
     * @return List<StudentDto>
     */
    List<StudentDto> loadAllStudents();

    /**
     * This method is used to load student resource by specific id
     * @param studentId is the path parameter from client
     * @return StudentDto
     */
    StudentDto loadStudentById(Long studentId);

    /**
     * This method is used to update student resource partially by specific id
     * @param studentId is the path parameter from client
     * @param studentDto is the request from client
     * @return StudentDto
     */
    StudentDto updateStudentById(Long studentId, StudentDto studentDto);

    /**
     * This method is used to delete student resource by specific id
     * @param studentId is the path parameter from client
     */
    void deleteStudentById(Long studentId);

    /**
     * This method is used to load paginated student resource
     * @param requestMap is the request parameter from client
     * @return PageDto
     */
    PageDto loadPaginatedStudents(Map<String, String> requestMap);

    /**
     * This method is used to search student followed by criteria
     * @param requestMap is the request parameter from client
     * @return List<StudentDto>
     */
    List<StudentDto> searchStudents(Map<String, Object> requestMap);

}
