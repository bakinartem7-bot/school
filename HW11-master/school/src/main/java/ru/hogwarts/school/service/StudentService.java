package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student updateStudent(Long id, Student studentDetails) {
        Student student = getStudent(id);
        student.setName(studentDetails.getName());
        student.setAge(studentDetails.getAge());
        student.setFaculty(studentDetails.getFaculty());
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public List<Student> getStudentsByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public List<Student> getStudentsByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    public long getStudentsCount() {
        return studentRepository.countAllStudents();
    }

    public double getAverageStudentAge() {
        Double averageAge = studentRepository.getAverageAge();
        return averageAge != null ? averageAge : 0.0;
    }

    public List<Student> getLastFiveStudents() {
        return studentRepository.findLastFiveStudents();
    }
}
