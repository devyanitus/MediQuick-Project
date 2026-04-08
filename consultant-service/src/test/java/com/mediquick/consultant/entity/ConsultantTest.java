package com.mediquick.consultant.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConsultantTest {

    @Test
    void shouldCreateConsultantUsingConstructor() {

        Consultant consultant = new Consultant(
                "Alice",
                "Cardiology",
                "Heart",
                "alice@gmail.com"   // ✅ REQUIRED
        );

        assertEquals("Alice", consultant.getName());
        assertEquals("Cardiology", consultant.getSpecialization());
        assertEquals("Heart", consultant.getCategory());
        assertEquals("alice@gmail.com", consultant.getEmail());
        assertNull(consultant.getId()); // not set yet
    }

    @Test
    void shouldSetAndGetFields() {

        Consultant consultant = new Consultant();

        consultant.setName("Bob");
        consultant.setSpecialization("Neurology");
        consultant.setCategory("Brain");
        consultant.setEmail("bob@gmail.com");

        assertEquals("Bob", consultant.getName());
        assertEquals("Neurology", consultant.getSpecialization());
        assertEquals("Brain", consultant.getCategory());
        assertEquals("bob@gmail.com", consultant.getEmail());
    }
}