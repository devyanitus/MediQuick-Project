package com.mediquick.consultant.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConsultantTest {

    @Test
    void shouldCreateConsultantUsingConstructor() {
        Consultant consultant = new Consultant("Alice", "Cardiology", "Heart");

        assertEquals("Alice", consultant.getName());
        assertEquals("Cardiology", consultant.getSpecialization());
        assertEquals("Heart", consultant.getCategory());
        assertNull(consultant.getId());
    }

    @Test
    void shouldSetAndGetFields() {
        Consultant consultant = new Consultant();

        consultant.setName("Bob");
        consultant.setSpecialization("Neurology");
        consultant.setCategory("Brain");

        assertEquals("Bob", consultant.getName());
        assertEquals("Neurology", consultant.getSpecialization());
        assertEquals("Brain", consultant.getCategory());
    }
}