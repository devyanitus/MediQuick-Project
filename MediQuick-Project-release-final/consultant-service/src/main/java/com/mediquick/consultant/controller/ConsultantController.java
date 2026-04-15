package com.mediquick.consultant.controller;

import com.mediquick.consultant.entity.Consultant;
import com.mediquick.consultant.service.ConsultantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class ConsultantController {

    private final ConsultantService consultantService;

    public ConsultantController(ConsultantService consultantService) {
        this.consultantService = consultantService;
    }

    @GetMapping
    public List<Consultant> getAllConsultants() {
        return consultantService.getAllConsultants();
    }

    @GetMapping("/category/{category}")
    public List<Consultant> getConsultantsByCategory(@PathVariable("category") String category) {
        return consultantService.getConsultantsByCategory(category);
    }

    @PostMapping
    public Consultant addConsultant(@RequestBody Consultant consultant) {
        return consultantService.addConsultant(consultant);
    }
}