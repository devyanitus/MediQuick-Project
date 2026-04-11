package com.mediquick.consultant.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    @Test
    void shouldSetAndGetAllFields() {

        Booking booking = new Booking();

        booking.setUserEmail("user@gmail.com");
        booking.setDoctorId(1L);
        booking.setDoctorName("Dr John");
        booking.setSpecialization("Cardiology");
        booking.setCategory("Heart");
        booking.setBookingDate(LocalDate.of(2026, 4, 10));
        booking.setTimeSlot("10:00 AM");
        booking.setBookingReference("BK123456");
        booking.setStatus("CONFIRMED");

        assertEquals("user@gmail.com", booking.getUserEmail());
        assertEquals(1L, booking.getDoctorId());
        assertEquals("Dr John", booking.getDoctorName());
        assertEquals("Cardiology", booking.getSpecialization());
        assertEquals("Heart", booking.getCategory());
        assertEquals(LocalDate.of(2026, 4, 10), booking.getBookingDate());
        assertEquals("10:00 AM", booking.getTimeSlot());
        assertEquals("BK123456", booking.getBookingReference());
        assertEquals("CONFIRMED", booking.getStatus());
    }

    @Test
    void shouldHaveDefaultStatusConfirmed() {

        Booking booking = new Booking();

        assertEquals("CONFIRMED", booking.getStatus());
    }

    @Test
    void shouldUpdateStatus() {

        Booking booking = new Booking();

        booking.setStatus("CANCELLED");

        assertEquals("CANCELLED", booking.getStatus());
    }
}