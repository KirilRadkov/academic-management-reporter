package com.example.academicmanagementreporter.controllers.dtos;

import com.example.academicmanagementreporter.entities.Course;
import com.example.academicmanagementreporter.entities.Student;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;


@Data
public class StudentDto {

    @NotBlank
    private String name;
    @NotNull
    private Integer age;
    @UniqueElements
    private List<Course> courses;
    @UniqueElements
    private List<String> groups;

    public static Student mapToEntity(StudentDto studentDto){
        return Student.builder()
                .name(studentDto.getName())
                .age(studentDto.getAge())
                .courses(studentDto.getCourses())
                .groups(studentDto.getGroups())
                .build();
    }

}
