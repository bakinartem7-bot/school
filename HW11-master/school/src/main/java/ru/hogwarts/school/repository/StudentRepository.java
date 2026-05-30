package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByAgeBetween(int minAge, int maxAge);
    List<Student> findByAge(int age);

    @Query("SELECT COUNT(*) FROM Student")
    long countAllStudents();

    @Query("SELECT AVG(s.age) FROM Student s")
    Double getAverageAge();

    @Query("SELECT s FROM Student s ORDER BY s.id DESC LIMIT 5")
    List<Student> findLastFiveStudents();
}
