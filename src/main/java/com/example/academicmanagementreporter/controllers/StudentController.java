package com.example.academicmanagementreporter.controllers;

import com.example.academicmanagementreporter.controllers.dtos.StudentDto;
import com.example.academicmanagementreporter.entities.Student;
import com.example.academicmanagementreporter.services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "Returns all students")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All students were successfully returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @Operation(summary = "Returns one student with the given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student for a given id was found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @Operation(summary = "Adds a new student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The student was successfully added"),
            @ApiResponse(responseCode = "400", description = "DTO validation error"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    public Student addStudent(@Valid @RequestBody StudentDto studentDto) {
        return studentService.addStudent(StudentDto.mapToEntity(studentDto));
    }

    @Operation(summary = "Updates a student with the given id or adds a new one")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The student was successfully updated or added"),
            @ApiResponse(responseCode = "400", description = "DTO validation error"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/{id}")
    public void updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDto studentDto) {
        studentService.updateStudent(id, StudentDto.mapToEntity(studentDto));
    }

    @Operation(summary = "Deletes a student with the given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The student was successfully deleted"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}
