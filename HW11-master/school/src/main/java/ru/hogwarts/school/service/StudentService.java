package ru.hogwarts.school.service;

import org.springframework.web.bind.annotation.GetMapping;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
            logger.error("Студент с ID = {} не найден", id);
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
            logger.error("Невозможно удалить студента — студент с ID = {} не существует", id);
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

    public List<String> getNamesStartingWithA() {
        logger.info("Был вызван метод для получения имён студентов, начинающихся с 'A'");
        logger.debug("Фильтруются имена студентов, начинающиеся с 'A', переводятся в верхний регистр и сортируются");


        List<String> names = studentRepository.findAll()
                .stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(name -> name.startsWith("A"))
                .sorted()
                .collect(Collectors.toList());

        logger.debug("Найдено {} имён, начинающихся с 'A'", names.size());
        return names;
    }

    public double getAverageAgeOfAllStudents() {
        logger.info("Был вызван метод для получения среднего возраста всех студентов");
        logger.debug("Рассчитывается средний возраст всех студентов через Stream API");

        double averageAge = studentRepository.findAll()
                .stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);

        logger.debug("Средний возраст всех студентов рассчитан: {}", averageAge);
        return averageAge;
    }

    public String getLongestFacultyName() {
        logger.info("Был вызван метод для получения самого длинного названия факультета");
        logger.debug("Ищем самое длинное название факультета среди всех студентов");

        String longestName = studentRepository.findAll()
                .stream()
                .map(Student::getFaculty)
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElse("");

        logger.debug("Самое длинное название факультета: '{}'", longestName);
        return longestName;
    }

    public long getParallelSum() {
        logger.info("Был вызван метод для вычисления суммы через параллельный поток");
        logger.debug("Вычисляется сумма чисел от 1 до 1 000 000 через параллельный поток");

        long sum = IntStream.rangeClosed(1, 1_000_000)
                .parallel()
                .sum();

        logger.debug("Сумма, вычисленная через параллельный поток: {}", sum);
        return sum;
    }

    public long getOptimizedSum() {
        logger.info("Был вызван метод для вычисления оптимизированной суммы");
        logger.debug("Вычисляется сумма чисел от 1 до 1 000 000 математической формулой");

        long n = 1_000_000L;
        long sum = n * (n + 1) / 2;

        logger.debug("Вычислинная сумма: {}", sum);
        return sum;
    }

    public void printStudentsParallel() {
        logger.info("Был вызван метод для вывода студентов в параллельном режиме");

        List<String> studentNames = getAllStudents().stream()
                .map(Student::getName)
                .collect(Collectors.toList());

        if (studentNames.size() < 6) {
            logger.warn("Для выполнения операции требуется минимум 6 студентов. Текущее количество: {}", studentNames.size());
            throw new RuntimeException("Для выполнения операции требуется минимум 6 студентов");
        }

        logger.debug("Получено {} студентов для вывода в параллельном режиме", studentNames.size());

        System.out.println("Основной поток: " + studentNames.get(0));
        System.out.println("Основной поток: " + studentNames.get(1));

        Thread thread1 = new Thread(() -> {
            logger.debug("Поток 1 начал выполнение");
            System.out.println("Поток 1: " + studentNames.get(2));
            System.out.println("Поток 1: " + studentNames.get(3));
            logger.debug("Поток 1 завершил выполнение");
        });

        Thread thread2 = new Thread(() -> {
            logger.debug("Поток 2 начал выполнение");
            System.out.println("Поток 2: " + studentNames.get(4));
            System.out.println("Поток 2: " + studentNames.get(5));
            logger.debug("Поток 2 завершил выполнение");
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
            logger.debug("Все потоки завершили выполнение в параллельном режиме");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Потоки были прерваны в параллельном режиме: {}", e.getMessage());
            System.err.println("Потоки были прерваны: " + e.getMessage());
        }
    }

    public void printStudentsSynchronized() {
        logger.info("Был вызван метод для вывода студентов в синхронизированном режиме");

        List<String> studentNames = getAllStudents().stream()
                .map(Student::getName)
                .collect(Collectors.toList());

        if (studentNames.size() < 6) {
            logger.warn("Для выполнения операции требуется минимум 6 студентов. Текущее количество: {}", studentNames.size());
            throw new RuntimeException("Для выполнения операции требуется минимум 6 студентов");
        }

        logger.debug("Получено {} студентов для вывода в синхронизированном режиме", studentNames.size());

        printName("Основной поток: " + studentNames.get(0));
        printName("Основной поток: " + studentNames.get(1));

        Thread thread1 = new Thread(() -> {
            logger.debug("Поток 1 начал выполнение в синхронизированном режиме");
            printName("Поток 1: " + studentNames.get(2));
            printName("Поток 1: " + studentNames.get(3));
            logger.debug("Поток 1 завершил выполнение в синхронизированном режиме");
        });

        Thread thread2 = new Thread(() -> {
            logger.debug("Поток 2 начал выполнение в синхронизированном режиме");
            printName("Поток 2: " + studentNames.get(4));
            printName("Поток 2: " + studentNames.get(5));
            logger.debug("Поток 2 завершил выполнение в синхронизированном режиме");
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
            logger.debug("Все потоки завершили выполнение в синхронизированном режиме");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Потоки были прерваны в синхронизированном режиме: {}", e.getMessage());
            System.err.println("Потоки были прерваны: " + e.getMessage());
        }
    }

    private synchronized void printName(String name) {
        logger.trace("Поток {} выводит: {}", Thread.currentThread().getName(), name);
        System.out.println(name);
    }
}
