package com.mediquick.consultant.service;

import com.mediquick.consultant.entity.Consultant;
import com.mediquick.consultant.repository.ConsultantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultantServiceTest {

    @Mock
    private ConsultantRepository consultantRepository;

    @InjectMocks
    private ConsultantService consultantService;

    @Test
    void shouldReturnAllConsultants() {
        List<Consultant> consultants = Arrays.asList(
                new Consultant("Alice", "Cardiology", "Heart"),
                new Consultant("Bob", "Neurology", "Brain")
        );

        when(consultantRepository.findAll()).thenReturn(consultants);

        List<Consultant> result = consultantService.getAllConsultants();

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
        verify(consultantRepository).findAll();
    }

    @Test
    void shouldReturnConsultantsByCategory() {
        List<Consultant> consultants = Collections.singletonList(
                new Consultant("Alice", "Cardiology", "Heart")
        );

        when(consultantRepository.findByCategory("Heart")).thenReturn(consultants);

        List<Consultant> result = consultantService.getConsultantsByCategory("Heart");

        assertEquals(1, result.size());
        assertEquals("Heart", result.get(0).getCategory());
        verify(consultantRepository).findByCategory("Heart");
    }

    @Test
    void shouldAddConsultant() {
        Consultant consultant = new Consultant("Alice", "Cardiology", "Heart");

        when(consultantRepository.save(consultant)).thenReturn(consultant);

        Consultant result = consultantService.addConsultant(consultant);

        assertEquals("Alice", result.getName());
        assertEquals("Cardiology", result.getSpecialization());
        verify(consultantRepository).save(consultant);
    }
}