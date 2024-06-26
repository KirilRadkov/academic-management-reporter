package com.example.academicmanagementreporter.services

import com.example.academicmanagementreporter.entities.Course
import com.example.academicmanagementreporter.entities.Teacher
import com.example.academicmanagementreporter.repositories.TeacherRepository
import org.springframework.web.server.ResponseStatusException
import spock.lang.Specification

class TeacherServiceTest extends Specification {

    def teacherRepository = Mock(TeacherRepository)
    def teacherService = new TeacherService(teacherRepository)

    def "GetAllTeachers"() {
        when:
        teacherService.getAllTeachers()

        then:
        1 * teacherRepository.findAll()
    }

    def "GetTeacherById: teacher found"() {
        given:
        def teacher = Optional.of(Teacher.builder()
                .id(5)
                .name("Teacher Name")
                .age(50)
                .courses(List.of(Course.MAIN))
                .groups(List.of("A")).build())
        teacherRepository.findById(5L) >> teacher

        when:
        def result =teacherService.getTeacherById(5L)

        then:
        result == teacher.get()
        noExceptionThrown()
    }

    def "GetTeacherById: teacher not found"() {
        given:
        def teacher = Optional.empty()
        teacherRepository.findById(5L) >> teacher

        when:
        teacherService.getTeacherById(5L)

        then:
        thrown(ResponseStatusException)
    }

    def "AddTeacher"() {
        def teacher = Teacher.builder()
                .id(5)
                .name("Teacher Name")
                .age(40)
                .courses(List.of(Course.MAIN))
                .groups(List.of("A")).build()

        when:
        teacherService.addTeacher(teacher)

        then:
        1 * teacherRepository.save(teacher)
    }

    def "UpdateTeacher: teacher found"() {
        def teacher = Teacher.builder()
                .id(5)
                .name("Teacher Name")
                .age(52)
                .courses(List.of(Course.MAIN))
                .groups(List.of("A")).build()
        teacherRepository.findById(5L) >> Optional.of(teacher)

        when:
        teacherService.updateTeacher(5L, teacher)

        then:
        1 * teacherRepository.save(teacher)
    }

    def "UpdateTeacher: teacher not found - add new"() {
        def teacher = Teacher.builder()
                .id(5)
                .name("Teacher Name")
                .age(42)
                .courses(List.of(Course.MAIN))
                .groups(List.of("A")).build()
        teacherRepository.findById(5L) >> Optional.empty()

        when:
        teacherService.updateTeacher(5L, teacher)

        then:
        1 * teacherRepository.save(teacher)
    }

    def "DeleteTeacher"() {
        when:
        teacherService.deleteTeacher(1L)

        then:
        1 * teacherRepository.deleteById(1L)
    }
}
