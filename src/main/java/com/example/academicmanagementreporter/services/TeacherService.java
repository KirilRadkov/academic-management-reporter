package com.example.academicmanagementreporter.services;

import com.example.academicmanagementreporter.entities.Teacher;
import com.example.academicmanagementreporter.repositories.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Teacher with the given id not found"
        ));
    }

    public Teacher addTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public void updateTeacher(Long id, Teacher teacher) {
        Optional<Teacher> existingTeacher = teacherRepository.findById(id);
        existingTeacher.ifPresentOrElse(t -> {
                t.setName(teacher.getName());
                t.setAge(teacher.getAge());
                t.setGroups(teacher.getGroups());
                t.setCourses(teacher.getCourses());
                teacherRepository.save(t);
                },
                () -> teacherRepository.save(teacher));
    }

    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }
}