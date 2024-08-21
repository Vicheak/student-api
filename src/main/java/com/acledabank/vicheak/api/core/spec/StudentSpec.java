package com.acledabank.vicheak.api.core.spec;

import com.acledabank.vicheak.api.core.entity.Student;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@RequiredArgsConstructor
public class StudentSpec implements Specification<Student> {

    private transient final StudentFilter studentFilter;

    @Override
    public Predicate toPredicate(Root<Student> studentRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if(Objects.nonNull(studentFilter.studentName())){
            Predicate predicateStudentName = cb.like(cb.lower(studentRoot.get("studentName")),
                    '%' + studentFilter.studentName().toLowerCase() + '%');
            predicates.add(predicateStudentName);
        }

        if(Objects.nonNull(studentFilter.studentAge())){
            Predicate predicateStudentAge = cb.equal(studentRoot.get("studentAge"), studentFilter.studentAge());
            predicates.add(predicateStudentAge);
        }

        return cb.and(predicates.toArray(Predicate[]::new));
    }

}
