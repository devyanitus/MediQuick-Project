package com.mediquick.consultant.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "consultants")
public class Consultant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String specialization;
    private String category;

    public Consultant() {}

    public Consultant(String name, String specialization, String category) {
        this.name = name;
        this.specialization = specialization;
        this.category = category;
    }

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}