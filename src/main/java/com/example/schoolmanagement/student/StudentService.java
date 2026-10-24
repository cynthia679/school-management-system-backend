package com.example.schoolmanagement.student;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<Student> findAll(String search, int page, int size,
                                 String sortBy, String direction) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);

        String requestedSort = sortBy == null ? "lastName" : sortBy;
        String safeSort = switch (requestedSort) {
            case "firstName", "lastName", "admissionNumber",
                 "className", "createdAt" -> requestedSort;
            default -> "lastName";
        };

        Sort sort = "desc".equalsIgnoreCase(direction)
                ? Sort.by(safeSort).descending()
                : Sort.by(safeSort).ascending();

        Pageable pageable = PageRequest.of(safePage, safeSize, sort);

        if (search == null || search.isBlank()) {
            return repository.findAll(pageable);
        }

        String term = search.trim();
        return repository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrAdmissionNumberContainingIgnoreCase(
                        term, term, term, pageable);
    }

    @Transactional(readOnly = true)
    public Student findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    public Student create(StudentRequest request) {
        if (repository.existsByAdmissionNumberIgnoreCase(request.admissionNumber())) {
            throw new DuplicateStudentException("Admission number already exists.");
        }
        if (repository.existsByEmailIgnoreCase(request.email())) {
            throw new DuplicateStudentException("Email already exists.");
        }

        Student student = new Student();
        apply(student, request);
        return repository.save(student);
    }

    public Student update(Long id, StudentRequest request) {
        Student student = findById(id);

        if (repository.existsByAdmissionNumberIgnoreCaseAndIdNot(
                request.admissionNumber(), id)) {
            throw new DuplicateStudentException("Admission number already exists.");
        }
        if (repository.existsByEmailIgnoreCaseAndIdNot(request.email(), id)) {
            throw new DuplicateStudentException("Email already exists.");
        }

        apply(student, request);
        return repository.save(student);
    }

    public void delete(Long id) {
        Student student = findById(id);
        repository.delete(student);
    }

    private void apply(Student student, StudentRequest request) {
        student.setAdmissionNumber(request.admissionNumber().trim());
        student.setFirstName(request.firstName().trim());
        student.setLastName(request.lastName().trim());
        student.setEmail(request.email().trim().toLowerCase());
        student.setClassName(request.className().trim());
    }
}