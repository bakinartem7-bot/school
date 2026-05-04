package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.hogwarts.school.model.Faculty;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTestRestTemplateTest {

    @Autowired
    private RestTemplate testRestTemplate;

    @Test
    void testGetFacultyById_Success() {
        ResponseEntity<Faculty> response = testRestTemplate.getForEntity("/faculty/1", Faculty.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testGetFacultyById_NotFound() {
        ResponseEntity<Faculty> response = testRestTemplate.getForEntity("/faculty/999", Faculty.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateFaculty_Success() {
        Faculty newFaculty = new Faculty(null, "New Faculty", "Red");
        ResponseEntity<Faculty> response = testRestTemplate.postForEntity("/faculty", newFaculty, Faculty.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
    }

    @Test
    void testUpdateFaculty_Success() {
        Faculty updatedFaculty = new Faculty(1L, "Updated Faculty", "Blue");
        testRestTemplate.put("/faculty/1", updatedFaculty);
        ResponseEntity<Faculty> getResponse = testRestTemplate.getForEntity("/faculty/1", Faculty.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals("Updated Faculty", getResponse.getBody().getName());
    }

    @Test
    void testDeleteFaculty_Success() {
        testRestTemplate.delete("/faculty/1");
        ResponseEntity<Faculty> response = testRestTemplate.getForEntity("/faculty/1", Faculty.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
