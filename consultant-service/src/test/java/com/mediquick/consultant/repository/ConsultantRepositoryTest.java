package com.mediquick.consultant.repository;

import com.mediquick.consultant.entity.Consultant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ConsultantRepositoryTest {

    @Autowired
    private ConsultantRepository consultantRepository;

    private Consultant doctor1;
    private Consultant doctor2;
    private Consultant doctor3;

    @BeforeEach
    void setUp() {
        consultantRepository.deleteAll();

        doctor1 = new Consultant("Dr John Smith", "General Medicine", "GP", "john@gmail.com");
        doctor2 = new Consultant("Dr Alice", "Family Medicine", "GP", "alice@gmail.com");
        doctor3 = new Consultant("Dr Anna Lee", "Mental Health", "Psychiatrist", "anna@gmail.com");

        consultantRepository.save(doctor1);
        consultantRepository.save(doctor2);
        consultantRepository.save(doctor3);
    }

    // ===== findByCategory =====

    @Test
    void findByCategory_ReturnsMatchingDoctors() {
        List<Consultant> result = consultantRepository.findByCategory("GP");

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(c -> c.getCategory().equals("GP")));
    }

    @Test
    void findByCategory_NoMatch_ReturnsEmpty() {
        List<Consultant> result = consultantRepository.findByCategory("Nurse");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByCategory_Psychiatrist_ReturnsOne() {
        List<Consultant> result = consultantRepository.findByCategory("Psychiatrist");

        assertEquals(1, result.size());
        assertEquals("Dr Anna Lee", result.get(0).getName());
    }

    // ===== save =====

    @Test
    void save_PersistsConsultant() {
        Consultant newDoctor = new Consultant(
                "Dr New", "Sports Injury", "Sports", "new@gmail.com");
        Consultant saved = consultantRepository.save(newDoctor);

        assertNotNull(saved.getId());
        assertEquals("Dr New", saved.getName());
    }

    @Test
    void save_DuplicateEmail_ThrowsException() {
        Consultant duplicate = new Consultant(
                "Dr Duplicate", "General Medicine", "GP", "john@gmail.com");

        assertThrows(Exception.class, () -> {
            consultantRepository.saveAndFlush(duplicate);
        });
    }

    // ===== findAll =====

    @Test
    void findAll_ReturnsAllConsultants() {
        List<Consultant> result = consultantRepository.findAll();
        assertEquals(3, result.size());
    }

    // ===== findById =====

    @Test
    void findById_ExistingId_ReturnsConsultant() {
        Consultant saved = consultantRepository.save(
                new Consultant("Dr Find", "Cardiology", "GP", "find@gmail.com"));

        Optional<Consultant> result = consultantRepository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("Dr Find", result.get().getName());
    }

    @Test
    void findById_NonExistingId_ReturnsEmpty() {
        Optional<Consultant> result = consultantRepository.findById(999L);
        assertFalse(result.isPresent());
    }

    // ===== delete =====

    @Test
    void delete_RemovesConsultant() {
        Consultant saved = consultantRepository.save(
                new Consultant("Dr Delete", "Neurology", "GP", "delete@gmail.com"));
        Long id = saved.getId();

        consultantRepository.deleteById(id);

        assertFalse(consultantRepository.findById(id).isPresent());
    }
}