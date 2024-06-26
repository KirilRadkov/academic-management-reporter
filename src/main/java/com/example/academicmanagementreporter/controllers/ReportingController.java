package com.example.academicmanagementreporter.controllers;


import com.example.academicmanagementreporter.entities.Course;
import com.example.academicmanagementreporter.entities.Student;
import com.example.academicmanagementreporter.services.ReportingService;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Slf4j
public class ReportingController {

    private final ReportingService reportingService;

    @Operation(summary = "Returns the students count")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student count was successfully returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/students/count")
    public long getStudentCount() {
        return reportingService.countStudents();
    }

    @Operation(summary = "Returns the teachers count")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher count was successfully returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/teachers/count")
    public long getTeacherCount() {
        return reportingService.countTeachers();
    }

    @Operation(summary = "Returns the cources count")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher count was successfully returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/courses/count")
    public Integer getCoursesCount() {
        return reportingService.countCourses();
    }

    @Operation(summary = "Returns all students which participate in specific course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course participants were successfully returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/students/course")
    public List<Student> getStudentsByCourse(@RequestParam Course course, @RequestParam boolean generateCSVReport) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
            return reportingService.getStudentsByCourse(course, generateCSVReport);
    }

    @Operation(summary = "Returns all students which participate in specific group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Group participants were successfully returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/students/group")
    public List<Student> getStudentsByGroup(@RequestParam String group, @RequestParam boolean generateCSVReport) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        return reportingService.getStudentsByGroup(group, generateCSVReport);
    }

    @Operation(summary = "Returns all teachers and students which participate in specific group and course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Participants in specific group and course were successfully returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/members/groupAndCourse")
    public Map<String, List<?>> getGroupCourseMembers(@RequestParam String group, @RequestParam Course course, @RequestParam boolean generateCSVReport) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        return reportingService.getGroupCourseMembers(group, course, generateCSVReport);
    }

    @Operation(summary = "Returns all students older than specific age and participate in specific course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Participants in specific course older than specific age were successfully returned"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/students/ageAndCourse")
    public List<Student> getStudentsOlderThanAndInCourse(@RequestParam int age, @RequestParam Course course, @RequestParam boolean generateCSVReport) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        return reportingService.getStudentsOlderThanAndInCourse(age, course, generateCSVReport);
    }

}