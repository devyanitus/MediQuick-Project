package com.mediquick.consultant.service;

import com.mediquick.consultant.entity.Consultant;
import com.mediquick.consultant.repository.ConsultantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultantService {

    private final ConsultantRepository consultantRepository;

    public ConsultantService(ConsultantRepository consultantRepository) {
        this.consultantRepository = consultantRepository;
    }

    public List<Consultant> getAllConsultants() {
        return consultantRepository.findAll();
    }

    public List<Consultant> getConsultantsByCategory(String category) {
        return consultantRepository.findByCategory(category);
    }

    public Consultant addConsultant(Consultant consultant) {
        return consultantRepository.save(consultant);
    }
}