package se.verran.springbootdemowithtests.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.verran.springbootdemowithtests.entities.Student;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        Student student = new Student("Abenezer", "Anglo", LocalDate.of(1983, 1, 29), "abenezer.anglo@example.com");
        studentRepository.save(student);

    }


    @Test
    void existsStudentByEmail() {

        boolean expected = studentRepository.existsStudentByEmail("abenezer.anglo@example.com");
        assertTrue(expected);


    }


}