package ru.hogwarts.school.controller;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        return studentService.updateStudent(id, studentDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("/age")
    public List<Student> getStudentsByAge(@RequestParam int age) {
        return studentService.getStudentsByAge(age);
    }

    @GetMapping("/between-age")
    public List<Student> getStudentsByAgeRange(
            @RequestParam int min,
            @RequestParam int max
    ) {
        return studentService.getStudentsByAgeBetween(min, max);
    }

    @GetMapping("/{id}/faculty")
    public Faculty getStudentFaculty(@PathVariable Long id) {
        Student student = studentService.getStudent(id);
        return student.getFaculty();
    }

    @GetMapping("/count")
    public long getStudentsCount() {
        return studentService.getStudentsCount();
    }

    @GetMapping("/average-age")
    public double getAverageStudentAge() {
        return studentService.getAverageStudentAge();
    }

    @GetMapping("/last-five")
    public List<Student> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }

    @GetMapping("/names-starting-with-a")
    public List<String> getNamesStartingWithA() {
        return studentService.getAllStudents()
                .stream()
                .map(student -> student.getName().toUpperCase())
                .filter(name -> name.startsWith("A"))
                .sorted()
                .collect(Collectors.toList());
    }

    @GetMapping("/average-age-all")
    public double getAverageAgeOfAllStudents() {
        return studentService.getAllStudents()
                .stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);
    }

    @GetMapping("/longest-faculty-name")
    public String getLongestFacultyName() {
        return studentService.getAllStudents()
                .stream()
                .map(Student::getFaculty)
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElse("");
    }

    @GetMapping("/optimized-sum")
    public long getOptimizedSum() {
        long n = 1_000_000L;
        long first = 1L;
        long last = n;
        return n * (first + last) / 2L;
    }

    @GetMapping("/parallel-sum")
    public long getParallelSum() {
        return IntStream.rangeClosed(1, 1_000_000)
                .parallel()
                .sum();
    }
}
