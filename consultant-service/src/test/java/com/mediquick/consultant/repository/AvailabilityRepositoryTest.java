package com.mediquick.consultant.repository;

import com.mediquick.consultant.entity.Availability;
import com.mediquick.consultant.entity.Consultant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AvailabilityRepositoryTest {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private ConsultantRepository consultantRepository;

    private Consultant doctor1;
    private Consultant doctor2;

    @BeforeEach
    void setUp() {
        availabilityRepository.deleteAll();
        consultantRepository.deleteAll();

        doctor1 = new Consultant("Dr John", "General Medicine", "GP", "john@gmail.com");
        doctor2 = new Consultant("Dr Alice", "Family Medicine", "GP", "alice@gmail.com");
        consultantRepository.save(doctor1);
        consultantRepository.save(doctor2);

        // Doctor 1 slots
        Availability slot1 = new Availability();
        slot1.setDoctor(doctor1);
        slot1.setAvailableDate(LocalDate.of(2026, 4, 10));
        slot1.setTimeSlot("09:00 AM");
        slot1.setBooked(false);

        Availability slot2 = new Availability();
        slot2.setDoctor(doctor1);
        slot2.setAvailableDate(LocalDate.of(2026, 4, 10));
        slot2.setTimeSlot("12:00 PM");
        slot2.setBooked(true);

        // Doctor 2 slot
        Availability slot3 = new Availability();
        slot3.setDoctor(doctor2);
        slot3.setAvailableDate(LocalDate.of(2026, 4, 11));
        slot3.setTimeSlot("03:00 PM");
        slot3.setBooked(false);

        availabilityRepository.save(slot1);
        availabilityRepository.save(slot2);
        availabilityRepository.save(slot3);
    }

    // ===== findByDoctorId =====

    @Test
    void findByDoctorId_ReturnsCorrectSlots() {
        List<Availability> result = availabilityRepository.findByDoctorId(doctor1.getId());
        assertEquals(2, result.size());
    }

    @Test
    void findByDoctorId_Doctor2_ReturnsOneSlot() {
        List<Availability> result = availabilityRepository.findByDoctorId(doctor2.getId());
        assertEquals(1, result.size());
    }

    @Test
    void findByDoctorId_NonExistingDoctor_ReturnsEmpty() {
        List<Availability> result = availabilityRepository.findByDoctorId(999L);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByDoctorId_ReturnsCorrectTimeSlots() {
        List<Availability> result = availabilityRepository.findByDoctorId(doctor1.getId());
        List<String> timeSlots = result.stream().map(Availability::getTimeSlot).toList();

        assertTrue(timeSlots.contains("09:00 AM"));
        assertTrue(timeSlots.contains("12:00 PM"));
    }

    // ===== save =====

    @Test
    void save_PersistsAvailability() {
        Availability slot = new Availability();
        slot.setDoctor(doctor1);
        slot.setAvailableDate(LocalDate.of(2026, 5, 1));
        slot.setTimeSlot("09:00 AM");
        slot.setBooked(false);

        Availability saved = availabilityRepository.save(slot);

        assertNotNull(saved.getId());
        assertFalse(saved.isBooked());
    }

    // ===== findById =====

    @Test
    void findById_ExistingSlot_ReturnsSlot() {
        Availability slot = new Availability();
        slot.setDoctor(doctor1);
        slot.setAvailableDate(LocalDate.of(2026, 5, 2));
        slot.setTimeSlot("03:00 PM");
        slot.setBooked(false);
        Availability saved = availabilityRepository.save(slot);

        Optional<Availability> result = availabilityRepository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("03:00 PM", result.get().getTimeSlot());
    }

    @Test
    void findById_NonExisting_ReturnsEmpty() {
        Optional<Availability> result = availabilityRepository.findById(999L);
        assertFalse(result.isPresent());
    }

    // ===== booking status =====

    @Test
    void save_UpdateBookedStatus() {
        Availability slot = new Availability();
        slot.setDoctor(doctor1);
        slot.setAvailableDate(LocalDate.of(2026, 5, 3));
        slot.setTimeSlot("09:00 AM");
        slot.setBooked(false);
        Availability saved = availabilityRepository.save(slot);

        saved.setBooked(true);
        availabilityRepository.save(saved);

        Optional<Availability> updated = availabilityRepository.findById(saved.getId());
        assertTrue(updated.get().isBooked());
    }
}