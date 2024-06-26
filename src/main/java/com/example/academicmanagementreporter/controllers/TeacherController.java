package com.example.academicmanagementreporter.controllers;

import com.example.academicmanagementreporter.controllers.dtos.TeacherDto;
import com.example.academicmanagementreporter.entities.Teacher;
import com.example.academicmanagementreporter.services.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    @Operation(summary = "Returns all teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All teachers were successfully returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    public List<Teacher> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @Operation(summary = "Returns one teacher with the given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher for a given id was found"),
            @ApiResponse(responseCode = "404", description = "Teacher for a given id was found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/{id}")
    public Teacher getTeacherById(@PathVariable Long id) {
        return teacherService.getTeacherById(id);
    }

    @Operation(summary = "Adds a new teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The teacher was successfully added"),
            @ApiResponse(responseCode = "400", description = "DTO validation error"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    public Teacher addTeacher(@Valid @RequestBody TeacherDto teacherDto) {
        return teacherService.addTeacher(TeacherDto.mapTo(teacherDto));
    }

    @Operation(summary = "Updates a teacher with the given id or adds a new one")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The teacher was successfully updated or added"),
            @ApiResponse(responseCode = "400", description = "DTO validation error"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/{id}")
    public void updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherDto teacherDto) {
        teacherService.updateTeacher(id, TeacherDto.mapTo(teacherDto));
    }

    @Operation(summary = "Deletes a teacher with the given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The teacher was successfully deleted"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping("/{id}")
    public void deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
    }
}
