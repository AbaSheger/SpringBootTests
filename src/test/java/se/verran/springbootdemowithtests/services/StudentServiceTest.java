package se.verran.springbootdemowithtests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import se.verran.springbootdemowithtests.entities.Student;
import se.verran.springbootdemowithtests.repositories.StudentRepository;

import java.time.LocalDate;

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
        verify(studentRepository, times(1)).findAll();
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
        Student nonExistingStudent = new Student("Abenezer", "Anglo", LocalDate.of(1983, 1, 29), "abenezer@example.coim");

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


    }

    @Test
    void getStudentById() {
    }

    @Test
    void setGradeForStudentById() {
    }
}