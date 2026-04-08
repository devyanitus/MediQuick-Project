package com.mediquick.consultant.service;

import com.mediquick.consultant.entity.Consultant;
import com.mediquick.consultant.repository.ConsultantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultantServiceTest {

    @Mock
    private ConsultantRepository consultantRepository;

    @InjectMocks
    private ConsultantService consultantService;

    private Consultant doctor1;
    private Consultant doctor2;

    @BeforeEach
    void setUp() {
        doctor1 = new Consultant("Dr John Smith", "General Medicine", "GP", "john@gmail.com");
        doctor1.setId(1L);

        doctor2 = new Consultant("Dr Alice", "Family Medicine", "GP", "alice@gmail.com");
        doctor2.setId(2L);
    }

    // ===== getAllConsultants =====

    @Test
    void getAllConsultants_ReturnsAllDoctors() {
        when(consultantRepository.findAll()).thenReturn(Arrays.asList(doctor1, doctor2));

        List<Consultant> result = consultantService.getAllConsultants();

        assertEquals(2, result.size());
        verify(consultantRepository, times(1)).findAll();
    }

    @Test
    void getAllConsultants_EmptyList_ReturnsEmpty() {
        when(consultantRepository.findAll()).thenReturn(List.of());

        List<Consultant> result = consultantService.getAllConsultants();

        assertTrue(result.isEmpty());
    }

    // ===== getConsultantsByCategory =====

    @Test
    void getConsultantsByCategory_ReturnsMatchingDoctors() {
        when(consultantRepository.findByCategory("GP"))
                .thenReturn(Arrays.asList(doctor1, doctor2));

        List<Consultant> result = consultantService.getConsultantsByCategory("GP");

        assertEquals(2, result.size());
        assertEquals("GP", result.get(0).getCategory());
        assertEquals("GP", result.get(1).getCategory());
    }

    @Test
    void getConsultantsByCategory_NoMatch_ReturnsEmpty() {
        when(consultantRepository.findByCategory("Unknown"))
                .thenReturn(List.of());

        List<Consultant> result = consultantService.getConsultantsByCategory("Unknown");

        assertTrue(result.isEmpty());
    }

    @Test
    void getConsultantsByCategory_CorrectCategoryPassed() {
        when(consultantRepository.findByCategory("Nurse")).thenReturn(List.of());

        consultantService.getConsultantsByCategory("Nurse");

        verify(consultantRepository, times(1)).findByCategory("Nurse");
    }

    // ===== addConsultant =====

    @Test
    void addConsultant_SavesAndReturnsConsultant() {
        when(consultantRepository.save(doctor1)).thenReturn(doctor1);

        Consultant result = consultantService.addConsultant(doctor1);

        assertNotNull(result);
        assertEquals("Dr John Smith", result.getName());
        assertEquals("GP", result.getCategory());
        verify(consultantRepository, times(1)).save(doctor1);
    }

    @Test
    void addConsultant_ReturnsCorrectEmail() {
        when(consultantRepository.save(doctor1)).thenReturn(doctor1);

        Consultant result = consultantService.addConsultant(doctor1);

        assertEquals("john@gmail.com", result.getEmail());
    }
}