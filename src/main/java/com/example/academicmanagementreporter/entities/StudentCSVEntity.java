package com.example.academicmanagementreporter.entities;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
public class StudentCSVEntity {
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

    public static StudentCSVEntity mapFrom(Student student){
        return StudentCSVEntity.builder()
                .id(student.getId())
                .name(student.getName())
                .age(student.getAge())
                .courses(Arrays.toString(student.getCourses().toArray()).replace(",", ";"))
                .groups(Arrays.toString(student.getGroups().toArray()).replace(",", ";"))
                .build();
    }

}