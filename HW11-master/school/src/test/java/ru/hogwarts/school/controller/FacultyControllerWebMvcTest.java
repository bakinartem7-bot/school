package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @Test
    void testGetFacultyById_Success() throws Exception {
        Faculty mockFaculty = new Faculty(1L, "Gryffindor", "Red");
        Mockito.when(facultyService.getFaculty(1L)).thenReturn(mockFaculty);

        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Red"));
    }

    @Test
    void testGetFacultyById_NotFound() throws Exception {
        Mockito.when(facultyService.getFaculty(999L)).thenReturn(null);

        mockMvc.perform(get("/faculty/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateFaculty_Success() throws Exception {
        Faculty newFaculty = new Faculty(null, "New Faculty", "Green");
        Faculty savedFaculty = new Faculty(100L, "New Faculty", "Green");
        Mockito.when(facultyService.createFaculty(newFaculty)).thenReturn(savedFaculty);

        mockMvc.perform(post("/faculty")
                        .contentType("application/json")
                        .content("{\"name\":\"New Faculty\",\"color\":\"Green\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.name").value("New Faculty"));
    }

    @Test
    void testUpdateFaculty_Success() throws Exception {
        Faculty updatedFaculty = new Faculty(1L, "Updated Faculty", "Blue");
        Mockito.when(facultyService.updateFaculty(ArgumentMatchers.eq(1L), ArgumentMatchers.any(Faculty.class))).thenReturn(updatedFaculty);

        mockMvc.perform(put("/faculty/1")
                        .contentType("application/json")
                        .content("{\"name\":\"Updated Faculty\",\"color\":\"Blue\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Faculty"))
                .andExpect(jsonPath("$.color").value("Blue"));
    }

    @Test
    void testDeleteFaculty_Success() throws Exception {
        Mockito.doNothing().when(facultyService).deleteFaculty(1L);

        mockMvc.perform(delete("/faculty/1"))
                .andExpect(status().isNoContent());
    }
}
