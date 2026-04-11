package com.mediquick.consultant.service;

import com.mediquick.consultant.entity.Availability;
import com.mediquick.consultant.entity.Consultant;
import com.mediquick.consultant.repository.AvailabilityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvailabilityServiceTest {

    @Mock
    private AvailabilityRepository availabilityRepository;

    @InjectMocks
    private AvailabilityService availabilityService;

    private Availability slot1;
    private Availability slot2;
    private Consultant doctor;

    @BeforeEach
    void setUp() {
        doctor = new Consultant("Dr John Smith", "General Medicine", "GP", "john@gmail.com");
        doctor.setId(1L);

        slot1 = new Availability();
        slot1.setDoctor(doctor);
        slot1.setAvailableDate(LocalDate.of(2026, 4, 10));
        slot1.setTimeSlot("09:00 AM");
        slot1.setBooked(false);

        slot2 = new Availability();
        slot2.setDoctor(doctor);
        slot2.setAvailableDate(LocalDate.of(2026, 4, 10));
        slot2.setTimeSlot("12:00 PM");
        slot2.setBooked(true);
    }

    // ===== getAvailabilityByDoctor =====

    @Test
    void getAvailabilityByDoctor_ReturnsSlots() {
        when(availabilityRepository.findByDoctorId(1L))
                .thenReturn(Arrays.asList(slot1, slot2));

        List<Availability> result = availabilityService.getAvailabilityByDoctor(1L);

        assertEquals(2, result.size());
        verify(availabilityRepository, times(1)).findByDoctorId(1L);
    }

    @Test
    void getAvailabilityByDoctor_NoSlots_ReturnsEmpty() {
        when(availabilityRepository.findByDoctorId(99L)).thenReturn(List.of());

        List<Availability> result = availabilityService.getAvailabilityByDoctor(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAvailabilityByDoctor_ReturnsCorrectTimeSlots() {
        when(availabilityRepository.findByDoctorId(1L))
                .thenReturn(Arrays.asList(slot1, slot2));

        List<Availability> result = availabilityService.getAvailabilityByDoctor(1L);

        assertEquals("09:00 AM", result.get(0).getTimeSlot());
        assertEquals("12:00 PM", result.get(1).getTimeSlot());
    }

    // ===== bookSlot =====

    @Test
    void bookSlot_SetsBookedTrue() {
        when(availabilityRepository.findById(1L)).thenReturn(Optional.of(slot1));
        when(availabilityRepository.save(slot1)).thenReturn(slot1);

        Availability result = availabilityService.bookSlot(1L);

        assertTrue(result.isBooked());
        verify(availabilityRepository, times(1)).save(slot1);
    }

    @Test
    void bookSlot_SlotNotFound_ThrowsException() {
        when(availabilityRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                availabilityService.bookSlot(99L)
        );
        verify(availabilityRepository, never()).save(any());
    }

    @Test
    void bookSlot_AlreadyBooked_StillSaves() {
        slot2.setBooked(true);
        when(availabilityRepository.findById(2L)).thenReturn(Optional.of(slot2));
        when(availabilityRepository.save(slot2)).thenReturn(slot2);

        Availability result = availabilityService.bookSlot(2L);

        assertTrue(result.isBooked());
        verify(availabilityRepository, times(1)).save(slot2);
    }

    @Test
    void bookSlot_ExceptionMessage_ContainsSlotNotFound() {
        when(availabilityRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                availabilityService.bookSlot(99L)
        );
        assertEquals("Slot not found", ex.getMessage());
    }
}