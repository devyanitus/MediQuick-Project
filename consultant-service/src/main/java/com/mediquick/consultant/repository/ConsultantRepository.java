package com.mediquick.consultant.repository;

import com.mediquick.consultant.entity.Consultant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultantRepository extends JpaRepository<Consultant, Long> {

    List<Consultant> findByCategory(String category);
}