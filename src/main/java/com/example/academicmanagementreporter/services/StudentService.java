package com.example.academicmanagementreporter.services;

import com.example.academicmanagementreporter.entities.Student;
import com.example.academicmanagementreporter.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Student with the given id not found"
        ));
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public void updateStudent(Long id, Student student) {
        Optional<Student> existingStudent = studentRepository.findById(id);
        existingStudent.ifPresentOrElse(t -> {
                    t.setName(student.getName());
                    t.setAge(student.getAge());
                    t.setGroups(student.getGroups());
                    t.setCourses(student.getCourses());
                    studentRepository.save(t);
                },
                () -> studentRepository.save(student));
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
