package com.acledabank.vicheak.api.core.repository;

import com.acledabank.vicheak.api.core.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    
    boolean existsByStudentNameIgnoreCase(String studentName);

}
