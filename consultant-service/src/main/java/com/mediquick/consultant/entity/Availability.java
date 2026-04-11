package com.mediquick.consultant.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "availability")
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Consultant doctor;

    @Column(nullable = false)
    private LocalDate availableDate;

    @Column(nullable = false)
    private String timeSlot;

    @Column(nullable = false)
    private boolean isBooked = false;

    public Availability() {}

    public Long getId() { return id; }
    public Consultant getDoctor() { return doctor; }
    public void setDoctor(Consultant doctor) { this.doctor = doctor; }
    public LocalDate getAvailableDate() { return availableDate; }
    public void setAvailableDate(LocalDate availableDate) { this.availableDate = availableDate; }
    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
    public boolean isBooked() { return isBooked; }
    public void setBooked(boolean booked) { isBooked = booked; }
}