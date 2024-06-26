package com.example.academicmanagementreporter.entities;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Builder;
import lombok.Data;

import java.util.Arrays;

@Data
@Builder
public class TeacherCSVEntity {
    @CsvBindByPosition(position = 0)
    private Long id;
    @CsvBindByPosition(position = 1)
    private String name;
    @CsvBindByPosition(position = 2)
    private int age;
    @CsvBindByName(column = "courses", required = true)
    @CsvBindByPosition(position = 3)
    private String courses;
    @CsvBindByName(column = "groups", required = true)
    @CsvBindByPosition(position = 4)
    private String groups;

    public static TeacherCSVEntity mapFrom(Teacher teacher) {
        return TeacherCSVEntity.builder()
                .id(teacher.getId())
                .name(teacher.getName())
                .age(teacher.getAge())
                .courses(Arrays.toString(teacher.getCourses().toArray()).replace(",", ";"))
                .groups(Arrays.toString(teacher.getGroups().toArray()).replace(",", ";"))
                .build();
    }

}