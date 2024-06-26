package com.example.academicmanagementreporter.services;

import com.example.academicmanagementreporter.entities.*;
import com.example.academicmanagementreporter.repositories.StudentRepository;
import com.example.academicmanagementreporter.repositories.TeacherRepository;
import com.example.academicmanagementreporter.utils.CustomColumnPositionStrategy;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportingService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("_yyyy-MM-dd_HH-mm-ss");
    private final static String REPORTS_FOLDER_PREFIX = "reports";
    private final static String FILE_EXTENSION = ".csv";

    private final static String GET_STUDENTS_BY_COURSE_DESTINATION = "/studentsByCourse/Report-Students-By-Course-";
    private final static String GET_STUDENTS_BY_GROUP_DESTINATION = "/studentsByGroup/Report-Students-By-Group-";
    private final static String GET_GROUPS_COURSE_TEACHERS = "/groupCourseMembers/Report-Teachers-By-Group-And-Course-";
    private final static String GET_GROUPS_COURSE_STUDENTS = "/groupCourseMembers/Report-Students-By-Group-And-Course-";
    private final static String GET_STUDENTS_OLDER_THAN_AND_IN_COURSE = "/studentsByAgeAndCourse/Report-Students-By-Age-And-Course-";

    public long countStudents() {
        return studentRepository.count();
    }

    public long countTeachers() {
        return teacherRepository.count();
    }

    public Integer countCourses() {
        return Course.values().length;
    }

    public List<Student> getStudentsByCourse(Course course, boolean generateCSVReport) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        var studentsByCource = studentRepository.findAll().stream()
                .filter(student -> student.getCourses().contains(course))
                .toList();
        if (generateCSVReport && !studentsByCource.isEmpty()){
            var additionalParams = "_Params_Course-" + course;
            var csvEntities = studentsByCource.stream().map(StudentCSVEntity::mapFrom).toList();
            generateStudentCSVReport(csvEntities, GET_STUDENTS_BY_COURSE_DESTINATION, additionalParams);
        }

        return studentsByCource;
    }

    public List<Student> getStudentsByGroup(String group, boolean generateCSVReport) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        var studentsByGroup = studentRepository.findAll().stream()
                .filter(student -> student.getGroups().contains(group))
                .toList();
        if (generateCSVReport && !studentsByGroup.isEmpty()){
            var additionalParams = "_Params_Group-" + group;
            var csvEntities = studentsByGroup.stream().map(StudentCSVEntity::mapFrom).toList();
            generateStudentCSVReport(csvEntities, GET_STUDENTS_BY_GROUP_DESTINATION, additionalParams);
        }

        return studentsByGroup;
    }

    public Map<String, List<?>> getGroupCourseMembers(String group, Course course, boolean generateCSVReport) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        List<Student> students = studentRepository.findAll().stream()
                .filter(student -> student.getGroups().contains(group) && student.getCourses().contains(course))
                .toList();

        List<Teacher> teachers = teacherRepository.findAll().stream()
                .filter(teacher -> teacher.getGroups().contains(group) && teacher.getCourses().contains(course))
                .toList();
        if (generateCSVReport) {
            var additionalParams = "_Params_Group-" + group + "_Course-" + course;
            if (!students.isEmpty()){
                var csvEntities = students.stream().map(StudentCSVEntity::mapFrom).toList();
                generateStudentCSVReport(csvEntities, GET_GROUPS_COURSE_STUDENTS, additionalParams);
            }
            if (!teachers.isEmpty()){
                var csvEntities = teachers.stream().map(TeacherCSVEntity::mapFrom).toList();
                generateTeacherCSVReport(csvEntities, GET_GROUPS_COURSE_TEACHERS, additionalParams);
            }
        }


        return Map.of("students", students, "teachers", teachers);
    }

    public List<Student> getStudentsOlderThanAndInCourse(int age, Course course, boolean generateCSVReport) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        var studentsOlderThanAndInCourse = studentRepository.findAll().stream()
                .filter(student -> student.getAge() > age && student.getCourses().contains(course))
                .collect(Collectors.toList());
        if (generateCSVReport && !studentsOlderThanAndInCourse.isEmpty()) {
            var additionalParams = "_Params_Age-" + age + "_Course-" + course;
            var csvEntities = studentsOlderThanAndInCourse.stream().map(StudentCSVEntity::mapFrom).toList();
            generateStudentCSVReport(csvEntities, GET_STUDENTS_OLDER_THAN_AND_IN_COURSE, additionalParams);
        }

        return studentsOlderThanAndInCourse;
    }


    private void generateStudentCSVReport(List<?> list, String destination, String additionalParams) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        var destinationAndFileName = REPORTS_FOLDER_PREFIX + destination + additionalParams + LocalDateTime.now().format(formatter) + FILE_EXTENSION;
        var mappingStrategy = new CustomColumnPositionStrategy<StudentCSVEntity>();
        mappingStrategy.setType(StudentCSVEntity.class);
        Writer writer = new FileWriter(destinationAndFileName);
        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder<StudentCSVEntity>(writer)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withMappingStrategy(mappingStrategy)
                .build();
        beanToCsv.write(list);
        writer.close();
    }

    private void generateTeacherCSVReport(List<?> list, String destination, String additionalParams) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        var destinationAndFileName = REPORTS_FOLDER_PREFIX + destination + additionalParams + LocalDateTime.now().format(formatter) + FILE_EXTENSION;
        var mappingStrategy = new CustomColumnPositionStrategy<TeacherCSVEntity>();
        mappingStrategy.setType(TeacherCSVEntity.class);
        Writer writer = new FileWriter(destinationAndFileName);
        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder<TeacherCSVEntity>(writer)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withMappingStrategy(mappingStrategy)
                .build();
        beanToCsv.write(list);
        writer.close();
    }
}