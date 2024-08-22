package se.verran.springbootdemowithtests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import se.verran.springbootdemowithtests.entities.Student;
import se.verran.springbootdemowithtests.repositories.StudentRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class StudentServiceTest {

    private StudentService studentService;

    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {

        studentRepository = mock(StudentRepository.class);
        studentService = new StudentService(studentRepository);


    }


    @Test
    void addStudent_shouldHandleUniqueAndExistingEmails() {
        // Arrange
        Student uniqueEmailStudent = new Student("Abenezer", "Anglo", LocalDate.of(1983, 1, 29), "unique@example.com");
        Student existingEmailStudent = new Student("Abenezer", "Anglo", LocalDate.of(1983, 1, 29), "existing@example.com");

        // Mock behavior for unique email
        when(studentRepository.existsStudentByEmail(uniqueEmailStudent.getEmail())).thenReturn(false);
        when(studentRepository.save(uniqueEmailStudent)).thenReturn(uniqueEmailStudent);

        // Mock behavior for existing email
        when(studentRepository.existsStudentByEmail(existingEmailStudent.getEmail())).thenReturn(true);

        // Act & Assert for unique email
        assertDoesNotThrow(() -> {
            Student result = studentService.addStudent(uniqueEmailStudent);
            assertEquals(uniqueEmailStudent.getEmail(), result.getEmail());
        });
        verify(studentRepository).existsStudentByEmail(uniqueEmailStudent.getEmail());
        verify(studentRepository).save(uniqueEmailStudent);

        // Act & Assert for existing email
        assertThrows(ResponseStatusException.class, () -> studentService.addStudent(existingEmailStudent));
        verify(studentRepository).existsStudentByEmail(existingEmailStudent.getEmail());
        verify(studentRepository, never()).save(existingEmailStudent);
    }

    @Test
    void getAllStudents() {
        // Arrange
        when(studentRepository.findAll()).thenReturn(null);

        // Act
        studentService.getAllStudents();

        // Assert
        verify(studentRepository).findAll();
    }

    @Test
    void deleteStudent() {

        // Arrange
        int existingStudentId = 1;
        int nonExistingStudentId = 2;

        // Mock behavior for existing student
        when(studentRepository.existsById(existingStudentId)).thenReturn(true);

        // Mock behavior for non-existing student
        when(studentRepository.existsById(nonExistingStudentId)).thenReturn(false);

        // Act & Assert for existing student
        assertDoesNotThrow(() -> studentService.deleteStudent(existingStudentId));
        verify(studentRepository).deleteById(existingStudentId);

        // Act & Assert for non-existing student
        assertThrows(ResponseStatusException.class, () -> studentService.deleteStudent(nonExistingStudentId));




    }

    @Test
    void updateStudent() {
        // Arrange
        Student existingStudent = new Student("Abenezer", "Anglo", LocalDate.of(1983, 1, 29), "abenezer@example.com ");
        existingStudent.setId(1);

        Student nonExistingStudent = new Student("Abenezer", "Anglo", LocalDate.of(1983, 1, 29), "abenezer@example.com");
        nonExistingStudent.setId(2);

        // Mock behavior for existing student
        when(studentRepository.existsById(existingStudent.getId())).thenReturn(true);
        when(studentRepository.save(existingStudent)).thenReturn(existingStudent);



        // Mock behavior for non-existing student
        when(studentRepository.existsById(nonExistingStudent.getId())).thenReturn(false);

        // Act & Assert for existing student
        assertDoesNotThrow(() -> {
            Student result = studentService.updateStudent(existingStudent);
            assertEquals(existingStudent.getEmail(), result.getEmail());
        });
        verify(studentRepository).save(existingStudent);

        // Act & Assert for non-existing student
        assertThrows(ResponseStatusException.class, () -> studentService.updateStudent(nonExistingStudent));
        verify(studentRepository, never()).save(nonExistingStudent);



    }

    @Test
    void getStudentById() {

        // Arrange
        int existingStudentId = 1;
        int nonExistingStudentId = 2;

        Student existingStudent = new Student("Abenezer", "Anglo", LocalDate.of(1983, 1, 29), "abenezer@example.com");

        // Mock behavior for existing student
        when(studentRepository.findById(existingStudentId)).thenReturn(java.util.Optional.of(existingStudent));

        // Mock behavior for non-existing student
        when(studentRepository.findById(nonExistingStudentId)).thenReturn(java.util.Optional.empty());

        // Act & Assert for existing student
        assertDoesNotThrow(() -> {
            Student result = studentService.getStudentById(existingStudentId);
            assertEquals(existingStudent.getEmail(), result.getEmail());
        });
        verify(studentRepository).findById(existingStudentId);

    }

    @Test
    void setGradeForStudentById() {
        // Arrange
        int existingStudentId = 1;
        int nonExistingStudentId = 2;
        String validGradeAsString = "3.0";
        String invalidGradeAsString = "6.0";
        String invalidGradeFormat = "invalidGrade";
        String minGradeAsString = "0.0";
        String maxGradeAsString = "5.0";
        String belowZeroGradeAsString = "-1.0";
        Student student = new Student();
        student.setId(existingStudentId);
        student.setJavaProgrammingGrade(3.0);

        when(studentRepository.findById(existingStudentId)).thenReturn(Optional.of(student));
        when(studentRepository.findById(nonExistingStudentId)).thenReturn(Optional.empty());
        when(studentRepository.save(student)).thenReturn(student);

        // Act & Assert for valid grade
        assertDoesNotThrow(() -> {
            Student result = studentService.setGradeForStudentById(existingStudentId, validGradeAsString);
            assertEquals(3.0, result.getJavaProgrammingGrade());
        });
        verify(studentRepository, times(1)).findById(existingStudentId);
        verify(studentRepository, times(1)).save(student);

        // Act & Assert for non-existing student
        assertThrows(ResponseStatusException.class, () -> {
            studentService.setGradeForStudentById(nonExistingStudentId, validGradeAsString);
        });
        verify(studentRepository, times(1)).findById(nonExistingStudentId);

        // Act & Assert for invalid grade format
        assertThrows(ResponseStatusException.class, () -> {
            studentService.setGradeForStudentById(existingStudentId, invalidGradeFormat);
        });

        // Act & Assert for grade below zero
        assertThrows(ResponseStatusException.class, () -> {
            studentService.setGradeForStudentById(existingStudentId, belowZeroGradeAsString);
        });

        // Act & Assert for grade above max
        assertThrows(ResponseStatusException.class, () -> {
            studentService.setGradeForStudentById(existingStudentId, invalidGradeAsString);
        });

        // Act & Assert for min grade
        assertDoesNotThrow(() -> {
            Student result = studentService.setGradeForStudentById(existingStudentId, minGradeAsString);
            assertEquals(0.0, result.getJavaProgrammingGrade());
        });

        // Act & Assert for max grade
        assertDoesNotThrow(() -> {
            Student result = studentService.setGradeForStudentById(existingStudentId, maxGradeAsString);
            assertEquals(5.0, result.getJavaProgrammingGrade());
        });
    }



}