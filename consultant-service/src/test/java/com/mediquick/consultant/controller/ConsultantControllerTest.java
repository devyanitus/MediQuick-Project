package com.mediquick.consultant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediquick.consultant.entity.Consultant;
import com.mediquick.consultant.service.ConsultantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ConsultantControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ConsultantService consultantService;

    @InjectMocks
    private ConsultantController consultantController;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(consultantController).build();
    }

    @Test
    @DisplayName("Should return all consultants")
    void shouldReturnAllConsultants() throws Exception {
        Consultant c1 = new Consultant("Alice", "Cardiology", "Heart");
        Consultant c2 = new Consultant("Bob", "Neurology", "Brain");

        when(consultantService.getAllConsultants()).thenReturn(Arrays.asList(c1, c2));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[0].specialization").value("Cardiology"))
                .andExpect(jsonPath("$[0].category").value("Heart"))
                .andExpect(jsonPath("$[1].name").value("Bob"))
                .andExpect(jsonPath("$[1].specialization").value("Neurology"))
                .andExpect(jsonPath("$[1].category").value("Brain"));

        verify(consultantService).getAllConsultants();
    }

    @Test
    @DisplayName("Should return consultants by category")
    void shouldReturnConsultantsByCategory() throws Exception {
        Consultant consultant = new Consultant("Alice", "Cardiology", "Heart");

        when(consultantService.getConsultantsByCategory("Heart"))
                .thenReturn(Collections.singletonList(consultant));

        mockMvc.perform(get("/consultants/category/Heart"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[0].specialization").value("Cardiology"))
                .andExpect(jsonPath("$[0].category").value("Heart"));

        verify(consultantService).getConsultantsByCategory("Heart");
    }

    @Test
    @DisplayName("Should add consultant")
    void shouldAddConsultant() throws Exception {
        Consultant input = new Consultant("Alice", "Cardiology", "Heart");
        Consultant saved = new Consultant("Alice", "Cardiology", "Heart");

        when(consultantService.addConsultant(any(Consultant.class)))
                .thenReturn(saved);

        mockMvc.perform(post("/consultants")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.specialization").value("Cardiology"))
                .andExpect(jsonPath("$.category").value("Heart"));

        verify(consultantService).addConsultant(any(Consultant.class));
    }
}