package com.example.academicmanagementreporter.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int age;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "student_courses", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "course")
    private List<Course> courses;

    @ElementCollection
    @CollectionTable(name = "student_groups", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "group_name")
    private List<String> groups;

}