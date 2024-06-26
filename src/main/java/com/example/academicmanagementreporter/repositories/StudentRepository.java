package com.example.academicmanagementreporter.repositories;

import com.example.academicmanagementreporter.entities.Student;
import com.example.academicmanagementreporter.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}