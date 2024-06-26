package com.example.academicmanagementreporter.integrationtest

import com.example.academicmanagementreporter.entities.Course
import com.example.academicmanagementreporter.entities.Student
import com.example.academicmanagementreporter.services.StudentService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.server.ResponseStatusException
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerIT extends Specification {

    @SpringBean
    private StudentService studentService = Mock()
    @Autowired
    private MockMvc mockMvc
    @Autowired
    ObjectMapper objectMapper

    private Student student1;
    private Student student2;

    @BeforeEach
    void setup() {
        student1 = Student.builder()
                .id(1)
                .name("Student 1")
                .age(22)
                .courses(List.of(Course.MAIN))
                .groups(List.of("A")).build()
        student2 = Student.builder()
                .id(2)
                .name("Student 2")
                .age(25)
                .courses(List.of(Course.SECONDARY))
                .groups(List.of("B", "C")).build()
    }


    def "test getAllStudents"() {
        given:
        studentService.getAllStudents() >> List.of(student1, student2)
        when:
        def response = mockMvc.perform(get("/api/students"))


        then:
        response.andExpect(status().is2xxSuccessful())
        response.andExpect(jsonPath('$[0].name').value("Student 1"))
        response.andExpect(jsonPath('$[0].age').value(22))
        response.andExpect(jsonPath('$[0].courses[0]').value("MAIN"))
        response.andExpect(jsonPath('$[0].groups[0]').value("A"))
        response.andExpect(jsonPath('$[1].name').value("Student 2"))
        response.andExpect(jsonPath('$[1].age').value(25))
        response.andExpect(jsonPath('$[1].courses[0]').value("SECONDARY"))
        response.andExpect(jsonPath('$[1].groups[0]').value("B"))
        response.andExpect(jsonPath('$[1].groups[1]').value("C"))
    }

    def "test getStudentById"() {
        given:
        studentService.getStudentById(1L) >> student1

        when:
        def response = mockMvc.perform(get("/api/students/1"))

        then:
        response.andExpect(status().is2xxSuccessful())
        response.andExpect(jsonPath('$.name').value("Student 1"))
        response.andExpect(jsonPath('$.age').value(22))
        response.andExpect(jsonPath('$.courses[0]').value("MAIN"))
        response.andExpect(jsonPath('$.groups[0]').value("A"))
    }

    def "test getStudentById: student not found"() {
        given:
        studentService.getStudentById(1L) >>  { throw new ResponseStatusException( HttpStatus.NOT_FOUND, "Student with the given id not found") }

        when:
        def response = mockMvc.perform(get("/api/students/1"))

        then:
        response.andExpect(status().is4xxClientError())
    }

    def "test addStudent"() {
        given:
        studentService.addStudent(_) >> student1
        when:
        def response = mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student1)))


        then:
        response.andExpect(status().is2xxSuccessful())
        response.andExpect(jsonPath('$.name').value("Student 1"))
        response.andExpect(jsonPath('$.age').value(22))
        response.andExpect(jsonPath('$.courses[0]').value("MAIN"))
        response.andExpect(jsonPath('$.groups[0]').value("A"))
    }

    def "test updateStudent"() {
        when:
        def response = mockMvc.perform(put("/api/students/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student1)))

        then:
        response.andExpect(status().is2xxSuccessful())
    }

    def "test deleteStudent"() {
        when:
        def response = mockMvc.perform(delete("/api/students/1"))

        then:
        response.andExpect(status().is2xxSuccessful())
    }

}