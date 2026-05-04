package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.hogwarts.school.model.Student;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTestRestTemplateTest {

    @Autowired
    private RestTemplate testRestTemplate;

    @Test
    void testGetStudentById_Success() {
        ResponseEntity<Student> response = testRestTemplate.getForEntity("/student/1", Student.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testGetStudentById_NotFound() {
        ResponseEntity<Student> response = testRestTemplate.getForEntity("/student/999", Student.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateStudent_Success() {
        Student newStudent = new Student(null, "New Student", 20);
        ResponseEntity<Student> response = testRestTemplate.postForEntity("/student", newStudent, Student.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
    }

    @Test
    void testUpdateStudent_Success() {
        Student updatedStudent = new Student(1L, "Updated Name", 25);
        testRestTemplate.put("/student/1", updatedStudent);
        ResponseEntity<Student> getResponse = testRestTemplate.getForEntity("/student/1", Student.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals("Updated Name", getResponse.getBody().getName());
    }

    @Test
    void testDeleteStudent_Success() {
        testRestTemplate.delete("/student/1");
        ResponseEntity<Student> response = testRestTemplate.getForEntity("/student/1", Student.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
