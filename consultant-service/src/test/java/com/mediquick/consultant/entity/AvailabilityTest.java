package com.mediquick.consultant.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AvailabilityTest {

    @Test
    void shouldSetAndGetFields() {

        Availability availability = new Availability();

        Consultant doctor = new Consultant(
                "Dr John",
                "Cardiology",
                "Heart",
                "john@gmail.com"
        );

        availability.setDoctor(doctor);
        availability.setAvailableDate(LocalDate.of(2026, 4, 10));
        availability.setTimeSlot("10:00 AM");
        availability.setBooked(true);

        assertEquals("Dr John", availability.getDoctor().getName());
        assertEquals(LocalDate.of(2026, 4, 10), availability.getAvailableDate());
        assertEquals("10:00 AM", availability.getTimeSlot());
        assertTrue(availability.isBooked());
    }

    @Test
    void shouldHaveDefaultBookedFalse() {

        Availability availability = new Availability();

        assertFalse(availability.isBooked()); // default value
    }
}