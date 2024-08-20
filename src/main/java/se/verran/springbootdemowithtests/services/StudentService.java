package se.verran.springbootdemowithtests.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import se.verran.springbootdemowithtests.entities.Student;
import se.verran.springbootdemowithtests.repositories.StudentRepository;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student){
        boolean emailAlreadyExists = studentRepository.existsStudentByEmail(student.getEmail());
        if(emailAlreadyExists){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email " + student.getEmail() + " already exists");
        }
        return studentRepository.save(student);
    }
    public List<Student> getAllStudents()   {
        return studentRepository.findAll();
    }

    public void deleteStudent(int id){
        if(!studentRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find and delete student by id " + id);
        }
        studentRepository.deleteById(id);
    }
    public Student updateStudent(Student student){
        if(!studentRepository.existsById(student.getId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find and update student by id " + student.getId());
        }
        return studentRepository.save(student);
    }

    public Student getStudentById(int id) {
        return studentRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find student by id " + id));
    }

    public Student setGradeForStudentById(int studentId, String gradeAsString) {
        double grade;
        try {
            grade = Double.parseDouble(gradeAsString);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Valid grades are 0.0 - 5.0");
        }
        if(grade < 0 || grade > 5)
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Valid grades are 0.0 - 5.0");
        Student student = studentRepository.findById(studentId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find and update grades for student by id " + studentId)
        );
        student.setJavaProgrammingGrade(grade);
        return studentRepository.save(student);
    }
}
