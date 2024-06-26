package com.example.academicmanagementreporter.integrationtest

import com.example.academicmanagementreporter.entities.Course
import com.example.academicmanagementreporter.entities.Student
import com.example.academicmanagementreporter.entities.Teacher
import com.example.academicmanagementreporter.services.ReportingService
import com.fasterxml.jackson.databind.ObjectMapper
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class ReportingControllerIT extends Specification {

    @SpringBean
    private ReportingService reportingService = Mock()
    @Autowired
    private MockMvc mockMvc

    def "test getStudentCount"() {
        given:
        reportingService.countStudents() >> 2

        when:
        def response = mockMvc.perform(get("/api/reports/students/count"))

        then:
        response.andExpect(status().is2xxSuccessful())
        response.andExpect(content().string("2"))
    }

    def "test getTeacherCount"() {
        given:
        reportingService.countTeachers() >> 3

        when:
        def response = mockMvc.perform(get("/api/reports/teachers/count"))

        then:
        response.andExpect(status().is2xxSuccessful())
        response.andExpect(content().string("3"))
    }

    def "test getCoursesCount"() {
        given:
        reportingService.countCourses() >> 2

        when:
        def response = mockMvc.perform(get("/api/reports/courses/count"))

        then:
        response.andExpect(status().is2xxSuccessful())
        response.andExpect(content().string("2"))
    }

    def "test getStudentsByCourse"() {
        given:
        def student = Student.builder()
                .id(1)
                .name("Student 1")
                .age(22)
                .courses(List.of(Course.MAIN))
                .groups(List.of("A")).build()
        reportingService.getStudentsByCourse(Course.MAIN, false) >> List.of(student)

        when:
        def response = mockMvc.perform(get("/api/reports/students/course").param("course", "MAIN").param("generateCSVReport", "false"))

        then:
        response.andExpect(status().is2xxSuccessful())
        response.andExpect(jsonPath('$[0].name').value("Student 1"))
        response.andExpect(jsonPath('$[0].age').value(22))
        response.andExpect(jsonPath('$[0].courses[0]').value("MAIN"))
        response.andExpect(jsonPath('$[0].groups[0]').value("A"))
    }

    def "test getStudentsByGroup"() {
        given:
        def student = Student.builder()
                .id(1)
                .name("Student 1")
                .age(22)
                .courses(List.of(Course.MAIN))
                .groups(List.of("A")).build()
        reportingService.getStudentsByGroup("A", false) >> List.of(student)

        when:
        def response = mockMvc.perform(get("/api/reports/students/group").param("group", "A").param("generateCSVReport", "false"))

        then:
        response.andExpect(status().is2xxSuccessful())
        response.andExpect(jsonPath('$[0].name').value("Student 1"))
        response.andExpect(jsonPath('$[0].age').value(22))
        response.andExpect(jsonPath('$[0].courses[0]').value("MAIN"))
        response.andExpect(jsonPath('$[0].groups[0]').value("A"))
    }

    def "test getGroupCourseMembers"() {
        given:
        def student = Student.builder()
                .id(1)
                .name("Student 1")
                .age(22)
                .courses(List.of(Course.MAIN))
                .groups(List.of("A")).build()
        def teacher = Teacher.builder()
                .id(1)
                .name("Teacher 1")
                .age(40)
                .courses(List.of(Course.MAIN))
                .groups(List.of("A")).build()

        reportingService.getGroupCourseMembers("A", Course.MAIN, false) >> Map.of("students", [student], "teachers", [teacher])

        when:
        def response = mockMvc.perform(get("/api/reports/members/groupAndCourse").param("group", "A").param("course", "MAIN").param("generateCSVReport", "false"))

        then:
        response.andExpect(status().is2xxSuccessful())
        response.andExpect(jsonPath('$.students[0].name').value("Student 1"))
        response.andExpect(jsonPath('$.students[0].age').value(22))
        response.andExpect(jsonPath('$.students[0].courses').value("MAIN"))
        response.andExpect(jsonPath('$.students[0].groups').value("A"))
        response.andExpect(jsonPath('$.teachers[0].name').value("Teacher 1"))
        response.andExpect(jsonPath('$.teachers[0].age').value(40))
        response.andExpect(jsonPath('$.teachers[0].courses').value("MAIN"))
        response.andExpect(jsonPath('$.teachers[0].groups').value("A"))
    }

    def "test getStudentsOlderThanAndInCourse"() {
        given:
        def student = Student.builder()
                .id(1)
                .name("Student 1")
                .age(22)
                .courses(List.of(Course.MAIN))
                .groups(List.of("A")).build()
        reportingService.getStudentsOlderThanAndInCourse(20, Course.MAIN, false) >> List.of(student)

        when:
        def response = mockMvc.perform(get("/api/reports/students/ageAndCourse").param("age", "20").param("course", "MAIN").param("generateCSVReport", "false"))

        then:
        response.andExpect(status().is2xxSuccessful())
        response.andExpect(jsonPath('$[0].name').value("Student 1"))
        response.andExpect(jsonPath('$[0].age').value(22))
        response.andExpect(jsonPath('$[0].courses[0]').value("MAIN"))
        response.andExpect(jsonPath('$[0].groups[0]').value("A"))
    }

}
