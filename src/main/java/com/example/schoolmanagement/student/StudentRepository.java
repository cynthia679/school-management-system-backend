package com.example.schoolmanagement.student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByAdmissionNumberIgnoreCase(String admissionNumber);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByAdmissionNumberIgnoreCaseAndIdNot(String admissionNumber, Long id);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
    Page<Student> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrAdmissionNumberContainingIgnoreCase(
            String firstName, String lastName, String admissionNumber, Pageable pageable);
}
