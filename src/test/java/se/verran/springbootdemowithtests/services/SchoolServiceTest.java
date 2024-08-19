package se.verran.springbootdemowithtests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;
import se.verran.springbootdemowithtests.entities.Student;
import se.verran.springbootdemowithtests.repositories.StudentRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class SchoolServiceTest {



    private SchoolService schoolService;

    private StudentService studentService;


    @BeforeEach
    void setUp() {

        studentService = mock(StudentService.class);
        schoolService = new SchoolService(studentService);

    }

    @Test
    void numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups() {

        // Test case: numberOfGroups < 2
        when(studentService.getAllStudents()).thenReturn(new ArrayList<>());
        String result = schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(1);
        assertEquals("There should be at least two groups", result);

        // Test case: numberOfStudents < numberOfGroups
        List<Student> oneStudentList = new ArrayList<>();
        oneStudentList.add(new Student());
        when(studentService.getAllStudents()).thenReturn(oneStudentList);
        result = schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(2);
        assertEquals("Not able to divide 1 students into 2 groups", result);

        // Test case: numberOfStudents / numberOfGroups < 2
        List<Student> twoStudentsList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            twoStudentsList.add(new Student());
        }
        when(studentService.getAllStudents()).thenReturn(twoStudentsList);
        result = schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(2);
        assertEquals("Not able to manage 2 groups with 2 students", result);

        // Test case: remainder == 0
        List<Student> tenStudentsList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tenStudentsList.add(new Student());
        }
        when(studentService.getAllStudents()).thenReturn(tenStudentsList);
        result = schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(2);
        assertEquals("2 groups could be formed with 5 students per group", result);

        // Test case: remainder != 0
        List<Student> elevenStudentsList = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            elevenStudentsList.add(new Student());
        }
        when(studentService.getAllStudents()).thenReturn(elevenStudentsList);
        result = schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(2);
        assertEquals("2 groups could be formed with 5 students per group, but that would leave 1 student hanging", result);

        // Test case: reminder == 1 (for singular condition)
        List<Student> nineStudentsList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            nineStudentsList.add(new Student());
        }
        when(studentService.getAllStudents()).thenReturn(nineStudentsList);
        result = schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(4);
        assertEquals("4 groups could be formed with 2 students per group, but that would leave 1 student hanging", result);

        // Test case: remainder > 1 (for plural condition)
        List<Student> fourteenStudentsList = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            fourteenStudentsList.add(new Student());
        }
        when(studentService.getAllStudents()).thenReturn(fourteenStudentsList);
        result = schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(5);
        assertEquals("5 groups could be formed with 2 students per group, but that would leave 4 students hanging", result);
    }



    @Test
    void numberOfGroupsWhenDividedIntoGroupsOf() {
        // Test case: studentsPerGroup < 2
        when(studentService.getAllStudents()).thenReturn(new ArrayList<>());
        String result = schoolService.numberOfGroupsWhenDividedIntoGroupsOf(1);
        assertEquals("Size of group should be at least 2", result);

        // Test case: numberOfStudents < studentsPerGroup
        List<Student> oneStudentList = new ArrayList<>();
        oneStudentList.add(new Student());
        when(studentService.getAllStudents()).thenReturn(oneStudentList);
        result = schoolService.numberOfGroupsWhenDividedIntoGroupsOf(3);
        assertEquals("Not able to manage groups of 3 with only 1 students", result);

        // Test case: numberOfStudents / studentsPerGroup < 2
        List<Student> threeStudentsList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            threeStudentsList.add(new Student());
        }
        when(studentService.getAllStudents()).thenReturn(threeStudentsList);
        result = schoolService.numberOfGroupsWhenDividedIntoGroupsOf(2);
        assertEquals("Not able to manage groups of 2 with only 3 students", result);

        // Test case: remainder == 0
        List<Student> tenStudentsList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tenStudentsList.add(new Student());
        }
        when(studentService.getAllStudents()).thenReturn(tenStudentsList);
        result = schoolService.numberOfGroupsWhenDividedIntoGroupsOf(2);
        assertEquals("2 students per group is possible, there will be 5 groups", result);

        // Test case: remainder == 1
        List<Student> elevenStudentsList = new ArrayList<>();
        for (int i = 0; i<11; i++) {
            elevenStudentsList.add(new Student());
        }
        when(studentService.getAllStudents()).thenReturn(elevenStudentsList);
        result = schoolService.numberOfGroupsWhenDividedIntoGroupsOf(2);
        assertEquals("2 students per group is possible, there will be 5 groups, there will be 1 student hanging", result);

        // Test case: remainder > 1
        List<Student> fourteenStudentsList = new ArrayList<>();
        for (int i = 0; i<14; i++) {
            fourteenStudentsList.add(new Student());
        }
        when(studentService.getAllStudents()).thenReturn(fourteenStudentsList);
        result = schoolService.numberOfGroupsWhenDividedIntoGroupsOf(3);
        assertEquals("3 students per group is possible, there will be 4 groups, there will be 2 students hanging", result);
    }


    @Test
    void calculateAverageGrade() {

        // Test case: studentList.isEmpty()
        when(studentService.getAllStudents()).thenReturn(new ArrayList<>());
        assertThrows(RuntimeException.class, () -> schoolService.calculateAverageGrade());

        // Test case: studentList.size() == 1
        List<Student> oneStudentList = new ArrayList<>();
        Student student = new Student();
        student.setJavaProgrammingGrade(3.0);
        oneStudentList.add(student);
        when(studentService.getAllStudents()).thenReturn(oneStudentList);
        String result = schoolService.calculateAverageGrade();
        assertEquals("Average grade is 3.0", result);

        // Test case: studentList.size() > 1
        List<Student> threeStudentsList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Student student1 = new Student();
            student1.setJavaProgrammingGrade(3.0);
            threeStudentsList.add(student1);
        }
        when(studentService.getAllStudents()).thenReturn(threeStudentsList);
        result = schoolService.calculateAverageGrade();
        assertEquals("Average grade is 3.0", result);


    }

    @Test
    void getTopScoringStudents() {

        // Test case: When studentList is empty, expect a ResponseStatusException.
        when(studentService.getAllStudents()).thenReturn(new ArrayList<>());
        assertThrows(ResponseStatusException.class, () -> schoolService.getTopScoringStudents());

        // Test case: studentList.size() == 1
        List<Student> oneStudentList = new ArrayList<>();
        Student student = new Student();
        student.setJavaProgrammingGrade(1.0);
        oneStudentList.add(student);
        when(studentService.getAllStudents()).thenReturn(oneStudentList);
        List<Student> result = schoolService.getTopScoringStudents();
        assertEquals(1, result.size());

        // Test case: studentList.size() > 1
        List<Student> threeStudentsList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Student student1 = new Student();
            student1.setJavaProgrammingGrade(3.0);
            threeStudentsList.add(student1);
        }
        when(studentService.getAllStudents()).thenReturn(threeStudentsList);
        result = schoolService.getTopScoringStudents();
        assertEquals(1, result.size());
    }
}