package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Test
    void testGetStudentById_Success() throws Exception {
        Student mockStudent = new Student(1L, "Harry Potter", 17);
        when(studentService.getStudent(1L)).thenReturn(mockStudent);

        mockMvc.perform(get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Harry Potter"))
                .andExpect(jsonPath("$.age").value(17));
    }

    @Test
    void testGetStudentById_NotFound() throws Exception {
        when(studentService.getStudent(999L)).thenReturn(null);

        mockMvc.perform(get("/student/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateStudent_Success() throws Exception {
        Student newStudent = new Student(null, "New Student", 20);
        Student savedStudent = new Student(100L, "New Student", 20);
        when(studentService.createStudent(newStudent)).thenReturn(savedStudent);

        mockMvc.perform(post("/student")
                        .contentType("application/json")
                        .content("{\"name\":\"New Student\",\"age\":20}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.name").value("New Student"));
    }

    @Test
    void testUpdateStudent_Success() throws Exception {
        Student updatedStudent = new Student(1L, "Updated Name", 25);
        when(studentService.updateStudent(eq(1L), any(Student.class))).thenReturn(updatedStudent);

        mockMvc.perform(put("/student/1")
                        .contentType("application/json")
                        .content("{\"name\":\"Updated Name\",\"age\":25}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    void testDeleteStudent_Success() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/student/1"))
                .andExpect(status().isNoContent());
    }
}
