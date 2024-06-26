package com.example.academicmanagementreporter.controllers.dtos;

import com.example.academicmanagementreporter.entities.Course;
import com.example.academicmanagementreporter.entities.Teacher;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;


@Data
public class TeacherDto {

    @NotBlank
    private String name;
    @NotNull
    private Integer age;
    @UniqueElements
    private List<Course> courses;
    @UniqueElements
    private List<String> groups;

    public static Teacher mapTo(TeacherDto teacherDto){
        return Teacher.builder()
                .name(teacherDto.getName())
                .age(teacherDto.getAge())
                .courses(teacherDto.getCourses())
                .groups(teacherDto.getGroups())
                .build();
    }

}
