package com.mediquick.consultant.entity;

import jakarta.persistence.*;
@Entity
@Table(name = "consultants")
public class Consultant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, unique = true)
    private String email;

    public Consultant() {}

    public Consultant(String name, String specialization, String category, String email) {
        this.name = name;
        this.specialization = specialization;
        this.category = category;
        this.email = email;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}