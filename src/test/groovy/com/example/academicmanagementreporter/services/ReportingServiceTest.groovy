package com.example.academicmanagementreporter.services

import com.example.academicmanagementreporter.entities.Course
import com.example.academicmanagementreporter.entities.Student
import com.example.academicmanagementreporter.entities.Teacher
import com.example.academicmanagementreporter.repositories.StudentRepository
import com.example.academicmanagementreporter.repositories.TeacherRepository
import org.junit.jupiter.api.BeforeEach
import spock.lang.Specification

class ReportingServiceTest extends Specification {

    def studentRepository = Mock(StudentRepository)
    def teacherRepository = Mock(TeacherRepository)
    def reportingService = new ReportingService(studentRepository, teacherRepository)

    @BeforeEach
    void setup() {
        def student1 = Student.builder()
                .id(1)
                .name("Student 1")
                .age(22)
                .courses(List.of(Course.MAIN))
                .groups(List.of("A")).build()
        def student2 = Student.builder()
                .id(2)
                .name("Student 2")
                .age(25)
                .courses(List.of(Course.SECONDARY))
                .groups(List.of("A", "B")).build()
        def student3 = Student.builder()
                .id(3)
                .name("Student 3")
                .age(27)
                .courses(List.of(Course.MAIN))
                .groups(List.of("A", "C")).build()
        def student4 = Student.builder()
                .id(5)
                .name("Student 4")
                .age(35)
                .courses(List.of(Course.MAIN))
                .groups(List.of("C", "D")).build()
        studentRepository.findAll() >> List.of(student1, student2, student3, student4)

        def teacher1 = Teacher.builder()
                .id(1)
                .name("Teacher 1")
                .age(40)
                .courses(List.of(Course.MAIN))
                .groups(List.of("A")).build()
        def teacher2 = Teacher.builder()
                .id(3)
                .name("Teacher 2")
                .age(42)
                .courses(List.of(Course.SECONDARY))
                .groups(List.of("B")).build()
        def teacher3 = Teacher.builder()
                .id(4)
                .name("Teacher 3")
                .age(32)
                .courses(List.of(Course.MAIN))
                .groups(List.of("C")).build()
        def teacher4 = Teacher.builder()
                .id(5)
                .name("Teacher 4")
                .age(50)
                .courses(List.of(Course.MAIN))
                .groups(List.of("D")).build()
        teacherRepository.findAll() >> List.of(teacher1, teacher2, teacher3, teacher4)

    }

    def "CountStudents"() {
        when:
        reportingService.countStudents()

        then:
        1 * studentRepository.count();
    }

    def "CountTeachers"() {
        when:
        reportingService.countTeachers()

        then:
        1 * teacherRepository.count();
    }

    def "CountCourses"() {
        when:
        def result = reportingService.countCourses()

        then:
        result == 2
    }

    def "GetStudentsByCourse"() {
        when:
        def result = reportingService.getStudentsByCourse(Course.MAIN, false)

        then:
        result.size() == 3
        result.get(0).getName() == "Student 1"
        result.get(0).getAge() == 22
        result.get(0).getCourses() == List.of(Course.MAIN)
        result.get(0).getGroups() == List.of("A")
        result.get(1).getName() == "Student 3"
        result.get(1).getAge() == 27
        result.get(1).getCourses() == List.of(Course.MAIN)
        result.get(1).getGroups() == List.of("A", "C")
        result.get(2).getName() == "Student 4"
        result.get(2).getAge() == 35
        result.get(2).getCourses() == List.of(Course.MAIN)
        result.get(2).getGroups() == List.of("C", "D")
    }

    def "GetStudentsByGroup"() {
        when:
        def result = reportingService.getStudentsByGroup("C", false)

        then:
        result.get(0).getName() == "Student 3"
        result.get(0).getAge() == 27
        result.get(0).getCourses() == List.of(Course.MAIN)
        result.get(0).getGroups() == List.of("A", "C")
        result.get(1).getName() == "Student 4"
        result.get(1).getAge() == 35
        result.get(1).getCourses() == List.of(Course.MAIN)
        result.get(1).getGroups() == List.of("C", "D")

    }

    def "GetGroupCourseMembers"() {
        when:
        def result = reportingService.getGroupCourseMembers("A", Course.MAIN, false)

        then:
        result.get("students").get(0).getName() == "Student 1"
        result.get("students").get(0).getAge() == 22
        result.get("students").get(0).getCourses() == List.of(Course.MAIN)
        result.get("students").get(0).getGroups() == List.of("A")
        result.get("teachers").get(0).getName() == "Teacher 1"
        result.get("teachers").get(0).getAge() == 40
        result.get("teachers").get(0).getCourses() == List.of(Course.MAIN)
        result.get("teachers").get(0).getGroups() == List.of("A")
    }

    def "GetStudentsOlderThanAndInCourse"() {
        when:
        def result = reportingService.getStudentsOlderThanAndInCourse(26, Course.MAIN, false)

        then:
        result.get(0).getName() == "Student 3"
        result.get(0).getAge() == 27
        result.get(0).getCourses() == List.of(Course.MAIN)
        result.get(0).getGroups() == List.of("A", "C")
        result.get(1).getName() == "Student 4"
        result.get(1).getAge() == 35
        result.get(1).getCourses() == List.of(Course.MAIN)
        result.get(1).getGroups() == List.of("C", "D")
    }
}
