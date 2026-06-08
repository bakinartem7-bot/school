package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private StudentRepository studentRepository;

    public Student createStudent(Student student) {
        logger.info("Был вызван метод для создания студента");
        logger.debug("Создаётся студент с именем: {}, возрастом: {}", student.getName(), student.getAge());

        if (student.getName() == null || student.getName().trim().isEmpty()) {
            logger.warn("Попытка создать студента с пустым или null именем");
            throw new IllegalArgumentException("Имя студента не может быть пустым или null");
        }

        Student savedStudent = studentRepository.save(student);
        logger.debug("Студент успешно создан с ID: {}", savedStudent.getId());
        return savedStudent;
    }

    public Student getStudent(Long id) {
        logger.info("Был вызван метод для получения студента по ID");
        logger.debug("Ищется студент с ID: {}", id);

        Optional<Student> studentOptional = studentRepository.findById(id);
        if (!studentOptional.isPresent()) {
            logger.error("Студент с ID = " + id + " не найден");
            throw new RuntimeException("Студент не найден с ID: " + id);
        }

        Student student = studentOptional.get();
        logger.debug("Найден студент: ID={}, имя={}", student.getId(), student.getName());
        return student;
    }

    public List<Student> getAllStudents() {
        logger.info("Был вызван метод для получения всех студентов");
        logger.debug("Получаются все студенты из репозитория");

        List<Student> students = studentRepository.findAll();
        logger.debug("Получено {} студентов", students.size());
        return students;
    }

    public Student updateStudent(Long id, Student studentDetails) {
        logger.info("Был вызван метод для обновления студента");
        logger.debug("Обновляется студент с ID: {}, новые данные: имя={}, возраст={}",
                id, studentDetails.getName(), studentDetails.getAge());

        Student student = getStudent(id);

        if (studentDetails.getName() != null && !studentDetails.getName().trim().isEmpty()) {
            student.setName(studentDetails.getName());
        } else {
            logger.warn("Попытка обновить студента с пустым именем для ID: {}", id);
        }

        student.setAge(studentDetails.getAge());
        student.setFaculty(studentDetails.getFaculty());

        Student updatedStudent = studentRepository.save(student);
        logger.debug("Студент успешно обновлён: ID={}", updatedStudent.getId());
        return updatedStudent;
    }

    public void deleteStudent(Long id) {
        logger.info("Был вызван метод для удаления студента");
        logger.debug("Удаляется студент с ID: {}", id);

        if (!studentRepository.existsById(id)) {
            logger.error("Невозможно удалить студента — студент с ID = " + id + " не существует");
            throw new RuntimeException("Студент не найден для удаления с ID: " + id);
        }

        studentRepository.deleteById(id);
        logger.debug("Студент успешно удалён, ID: {}", id);
    }

    public List<Student> getStudentsByAge(int age) {
        logger.info("Был вызван метод для получения студентов по возрасту");
        logger.debug("Ищутся студенты с возрастом: {}", age);

        List<Student> students = studentRepository.findByAge(age);
        logger.debug("Найдено {} студентов с возрастом {}", students.size(), age);
        return students;
    }

    public List<Student> getStudentsByAgeBetween(int min, int max) {
        logger.info("Был вызван метод для получения студентов по диапазону возрастов");
        logger.debug("Ищутся студенты с возрастом между {} и {}", min, max);

        List<Student> students = studentRepository.findByAgeBetween(min, max);
        logger.debug("Найдено {} студентов с возрастом между {} и {}", students.size(), min, max);
        return students;
    }

    public long getStudentsCount() {
        logger.info("Был вызван метод для получения количества студентов");
        logger.debug("Подсчитывается общее количество студентов в репозитории");

        long count = studentRepository.countAllStudents();
        logger.debug("Общее количество студентов: {}", count);
        return count;
    }

    public double getAverageStudentAge() {
        logger.info("Был вызван метод для получения среднего возраста студентов");
        logger.debug("Рассчитывается средний возраст всех студентов");

        Double averageAge = studentRepository.getAverageAge();
        double result = averageAge != null ? averageAge : 0.0;
        logger.debug("Средний возраст студентов рассчитан: {}", result);
        return result;
    }

    public List<Student> getLastFiveStudents() {
        logger.info("Был вызван метод для получения последних пяти студентов");
        logger.debug("Получаются последние 5 студентов из репозитория");

        List<Student> students = studentRepository.findLastFiveStudents();
        logger.debug("Получено {} студентов (последние 5)", students.size());
        return students;
    }
}
