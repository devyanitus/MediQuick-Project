package com.mediquick.consultant.repository;

import com.mediquick.consultant.entity.Consultant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ConsultantRepositoryTest {

    @Autowired
    private ConsultantRepository consultantRepository;

    @Test
    @DisplayName("Should find consultants by category")
    void shouldFindConsultantsByCategory() {
        Consultant c1 = new Consultant("Alice", "Cardiology", "Heart");
        Consultant c2 = new Consultant("Bob", "Heart Surgery", "Heart");
        Consultant c3 = new Consultant("Charlie", "Neurology", "Brain");

        consultantRepository.save(c1);
        consultantRepository.save(c2);
        consultantRepository.save(c3);

        List<Consultant> result = consultantRepository.findByCategory("Heart");

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(c -> "Heart".equals(c.getCategory())));
    }

    @Test
    @DisplayName("Should return empty list when category not found")
    void shouldReturnEmptyListWhenCategoryNotFound() {
        List<Consultant> result = consultantRepository.findByCategory("Skin");

        assertTrue(result.isEmpty());
    }
}