package ru.hogwarts.school.controller;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
            @RequestParam int max) {
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
        return studentService.getNamesStartingWithA();
    }

    @GetMapping("/average-age-all")
    public double getAverageAgeOfAllStudents() {
        return studentService.getAverageAgeOfAllStudents();
    }

    @GetMapping("/longest-faculty-name")
    public String getLongestFacultyName() {
        return studentService.getLongestFacultyName();
    }

    @GetMapping("/optimized-sum")
    public long getOptimizedSum() {
        return studentService.getOptimizedSum();
    }

    @GetMapping("/parallel-sum")
    public long getParallelSum() {
        return studentService.getParallelSum();
    }

    @GetMapping("/students/print-parallel")
    public void printStudentsParallel() {
        List<String> studentNames = studentService.getAllStudents().stream()
                .map(Student::getName)
                .collect(Collectors.toList());

        if (studentNames.size() < 6) {
            throw new RuntimeException("Для выполнения операции требуется минимум 6 студентов");
        }

        System.out.println("Основной поток: " + studentNames.get(0));
        System.out.println("Основной поток: " + studentNames.get(1));

        Thread thread1 = new Thread(() -> {
            System.out.println("Поток 1: " + studentNames.get(2));
            System.out.println("Поток 1: " + studentNames.get(3));
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("Поток 2: " + studentNames.get(4));
            System.out.println("Поток 2: " + studentNames.get(5));
        });

        thread1.start();
        thread2.start();
    }

    @GetMapping("/students/print-synchronized")
    public void printStudentsSynchronized() {
        List<String> studentNames = studentService.getAllStudents().stream()
                .map(Student::getName)
                .collect(Collectors.toList());

        if (studentNames.size() < 6) {
            throw new RuntimeException("Для выполнения операции требуется минимум 6 студентов");
        }

        printName("Основной поток: " + studentNames.get(0));
        printName("Основной поток: " + studentNames.get(1));

        Thread thread1 = new Thread(() -> {
            printName("Поток 1: " + studentNames.get(2));
            printName("Поток 1: " + studentNames.get(3));
        });

        Thread thread2 = new Thread(() -> {
            printName("Поток 2: " + studentNames.get(4));
            printName("Поток 2: " + studentNames.get(5));
        });

        thread1.start();
        thread2.start();
    }

    private synchronized void printName(String name) {
        System.out.println(name);
    }
}

