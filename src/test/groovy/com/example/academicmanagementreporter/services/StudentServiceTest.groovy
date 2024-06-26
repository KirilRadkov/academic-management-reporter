package com.example.academicmanagementreporter.services

import com.example.academicmanagementreporter.entities.Course
import com.example.academicmanagementreporter.entities.Student
import com.example.academicmanagementreporter.repositories.StudentRepository
import org.springframework.web.server.ResponseStatusException
import spock.lang.Specification

class StudentServiceTest extends Specification {

    def studentRepository = Mock(StudentRepository)
    def studentService = new StudentService(studentRepository)

    def "GetAllStudents"() {
        when:
        studentService.getAllStudents()

        then:
        1 * studentRepository.findAll()
    }

    def "GetStudentById: student found"() {
        given:
        def student = Optional.of(Student.builder()
                .id(5)
                .name("Student Name")
                .age(22)
                .courses(List.of(Course.MAIN))
                .groups(List.of("A")).build())
        studentRepository.findById(5L) >> student

        when:
        def result = studentService.getStudentById(5L)

        then:
        result == student.get()
        noExceptionThrown()
    }

    def "GetStudentById: student not found"() {
        given:
        def student = Optional.empty()
        studentRepository.findById(5L) >> student

        when:
        studentService.getStudentById(5L)

        then:
        thrown(ResponseStatusException)
    }

    def "AddStudent"() {
        def student = Student.builder()
                .id(5)
                .name("Student Name")
                .age(22)
                .courses(List.of(Course.MAIN))
                .groups(List.of("A")).build()

        when:
        studentService.addStudent(student)

        then:
        1 * studentRepository.save(student)
    }

    def "UpdateStudent: student found"() {
        def student = Student.builder()
                .id(5)
                .name("Student Name")
                .age(22)
                .courses(List.of(Course.MAIN))
                .groups(List.of("A")).build()
        studentRepository.findById(5L) >> Optional.of(student)

        when:
        studentService.updateStudent(5L, student)

        then:
        1 * studentRepository.save(student)
    }

    def "UpdateStudent: student not found - add new"() {
        def student = Student.builder()
                .id(5)
                .name("Student Name")
                .age(22)
                .courses(List.of(Course.MAIN))
                .groups(List.of("A")).build()
        studentRepository.findById(5L) >> Optional.empty()

        when:
        studentService.updateStudent(5L, student)

        then:
        1 * studentRepository.save(student)
    }

    def "DeleteStudent"() {
        when:
        studentService.deleteStudent(1L)

        then:
        1 * studentRepository.deleteById(1L)
    }
}
