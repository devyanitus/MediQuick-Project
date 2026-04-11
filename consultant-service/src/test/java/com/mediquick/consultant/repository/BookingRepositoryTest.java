package com.mediquick.consultant.repository;

import com.mediquick.consultant.entity.Booking;
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
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ConsultantRepository consultantRepository;

    private Consultant doctor;
    private Booking booking1;
    private Booking booking2;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        consultantRepository.deleteAll();

        doctor = new Consultant("Dr John", "General Medicine", "GP", "john@gmail.com");
        consultantRepository.save(doctor);

        booking1 = new Booking();
        booking1.setUserEmail("user@gmail.com");
        booking1.setDoctorId(doctor.getId());
        booking1.setDoctorName("Dr John");
        booking1.setSpecialization("General Medicine");
        booking1.setCategory("GP");
        booking1.setBookingDate(LocalDate.of(2026, 4, 10));
        booking1.setTimeSlot("09:00 AM");
        booking1.setBookingReference("BK12345678");
        booking1.setStatus("CONFIRMED");

        booking2 = new Booking();
        booking2.setUserEmail("user@gmail.com");
        booking2.setDoctorId(doctor.getId());
        booking2.setDoctorName("Dr John");
        booking2.setSpecialization("General Medicine");
        booking2.setCategory("GP");
        booking2.setBookingDate(LocalDate.of(2026, 4, 15));
        booking2.setTimeSlot("12:00 PM");
        booking2.setBookingReference("BK87654321");
        booking2.setStatus("CANCELLED");

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
    }

    // ===== findByUserEmail =====

    @Test
    void findByUserEmail_ReturnsUserBookings() {
        List<Booking> result = bookingRepository.findByUserEmail("user@gmail.com");
        assertEquals(2, result.size());
    }

    @Test
    void findByUserEmail_WrongEmail_ReturnsEmpty() {
        List<Booking> result = bookingRepository.findByUserEmail("nobody@gmail.com");
        assertTrue(result.isEmpty());
    }

    @Test
    void findByUserEmail_ReturnsCorrectStatuses() {
        List<Booking> result = bookingRepository.findByUserEmail("user@gmail.com");
        List<String> statuses = result.stream().map(Booking::getStatus).toList();

        assertTrue(statuses.contains("CONFIRMED"));
        assertTrue(statuses.contains("CANCELLED"));
    }

    @Test
    void findByUserEmail_ReturnsCorrectBookingReferences() {
        List<Booking> result = bookingRepository.findByUserEmail("user@gmail.com");
        List<String> refs = result.stream().map(Booking::getBookingReference).toList();

        assertTrue(refs.contains("BK12345678"));
        assertTrue(refs.contains("BK87654321"));
    }

    // ===== save =====

    @Test
    void save_PersistsBooking() {
        Booking newBooking = new Booking();
        newBooking.setUserEmail("new@gmail.com");
        newBooking.setDoctorId(doctor.getId());
        newBooking.setDoctorName("Dr John");
        newBooking.setBookingDate(LocalDate.of(2026, 5, 1));
        newBooking.setTimeSlot("03:00 PM");
        newBooking.setBookingReference("BKNEW1234");
        newBooking.setStatus("CONFIRMED");

        Booking saved = bookingRepository.save(newBooking);

        assertNotNull(saved.getId());
        assertEquals("new@gmail.com", saved.getUserEmail());
    }

    // ===== findById =====

    @Test
    void findById_ExistingBooking_ReturnsBooking() {
        Optional<Booking> result = bookingRepository.findById(booking1.getId());

        assertTrue(result.isPresent());
        assertEquals("BK12345678", result.get().getBookingReference());
    }

    @Test
    void findById_NonExisting_ReturnsEmpty() {
        Optional<Booking> result = bookingRepository.findById(999L);
        assertFalse(result.isPresent());
    }

    // ===== cancel (status update) =====

    @Test
    void save_UpdateStatus_ToCancelled() {
        booking1.setStatus("CANCELLED");
        bookingRepository.save(booking1);

        Optional<Booking> updated = bookingRepository.findById(booking1.getId());
        assertEquals("CANCELLED", updated.get().getStatus());
    }

    // ===== delete =====

    @Test
    void delete_RemovesBooking() {
        Long id = booking1.getId();
        bookingRepository.deleteById(id);

        assertFalse(bookingRepository.findById(id).isPresent());
    }
}