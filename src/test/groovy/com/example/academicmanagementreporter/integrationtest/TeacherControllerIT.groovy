package com.example.academicmanagementreporter.integrationtest

import com.example.academicmanagementreporter.entities.Course
import com.example.academicmanagementreporter.entities.Teacher
import com.example.academicmanagementreporter.services.TeacherService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.server.ResponseStatusException
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class TeacherControllerIT extends Specification {

    @SpringBean
    private TeacherService teacherService = Mock()
    @Autowired
    private MockMvc mockMvc
    @Autowired
    ObjectMapper objectMapper

    private Teacher teacher1
    private Teacher teacher2

    @BeforeEach
    void setup() {
        teacher1 = Teacher.builder()
                .id(1)
                .name("Teacher 1")
                .age(40)
                .courses(List.of(Course.MAIN))
                .groups(List.of("A")).build()
        teacher2 = Teacher.builder()
                .id(2)
                .name("Teacher 2")
                .age(45)
                .courses(List.of(Course.SECONDARY))
                .groups(List.of("B", "C")).build()
    }


    def "test getAllTeachers"() {
        given:
        teacherService.getAllTeachers() >> List.of(teacher1, teacher2)
        when:
        def response = mockMvc.perform(get("/api/teachers"))


        then:
        response.andExpect(status().is2xxSuccessful())
        response.andExpect(jsonPath('$[0].name').value("Teacher 1"))
        response.andExpect(jsonPath('$[0].age').value(40))
        response.andExpect(jsonPath('$[0].courses[0]').value("MAIN"))
        response.andExpect(jsonPath('$[0].groups[0]').value("A"))
        response.andExpect(jsonPath('$[1].name').value("Teacher 2"))
        response.andExpect(jsonPath('$[1].age').value(45))
        response.andExpect(jsonPath('$[1].courses[0]').value("SECONDARY"))
        response.andExpect(jsonPath('$[1].groups[0]').value("B"))
        response.andExpect(jsonPath('$[1].groups[1]').value("C"))
    }

    def "test getTeacherById"() {
        given:
        teacherService.getTeacherById(1L) >> teacher1

        when:
        def response = mockMvc.perform(get("/api/teachers/1"))

        then:
        response.andExpect(status().is2xxSuccessful())
        response.andExpect(jsonPath('$.name').value("Teacher 1"))
        response.andExpect(jsonPath('$.age').value(40))
        response.andExpect(jsonPath('$.courses[0]').value("MAIN"))
        response.andExpect(jsonPath('$.groups[0]').value("A"))
    }

    def "test getTeacherById: teacher not found"() {
        given:
        teacherService.getTeacherById(1L) >> { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher with the given id not found") }

        when:
        def response = mockMvc.perform(get("/api/teachers/1"))

        then:
        response.andExpect(status().is4xxClientError())
    }

    def "test addTeacher"() {
        given:
        teacherService.addTeacher(_) >> teacher1
        when:
        def response = mockMvc.perform(post("/api/teachers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teacher1)))


        then:
        response.andExpect(status().is2xxSuccessful())
        response.andExpect(jsonPath('$.name').value("Teacher 1"))
        response.andExpect(jsonPath('$.age').value(40))
        response.andExpect(jsonPath('$.courses[0]').value("MAIN"))
        response.andExpect(jsonPath('$.groups[0]').value("A"))
    }

    def "test updateTeacher"() {
        when:
        def response = mockMvc.perform(put("/api/teachers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teacher1)))

        then:
        response.andExpect(status().is2xxSuccessful())
    }

    def "test deleteTeacher"() {
        when:
        def response = mockMvc.perform(delete("/api/teachers/1"))

        then:
        response.andExpect(status().is2xxSuccessful())
    }

}