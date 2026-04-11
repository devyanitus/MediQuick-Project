package com.mediquick.consultant.controller;

import com.mediquick.consultant.entity.Availability;
import com.mediquick.consultant.entity.Booking;
import com.mediquick.consultant.entity.Consultant;
import com.mediquick.consultant.service.AvailabilityService;
import com.mediquick.consultant.service.BookingService;
import com.mediquick.consultant.service.ConsultantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultants")
public class ConsultantController {

    private final ConsultantService consultantService;
    private final AvailabilityService availabilityService;
    private final BookingService bookingService;

    public ConsultantController(ConsultantService consultantService,
                                AvailabilityService availabilityService,
                                BookingService bookingService) {
        this.consultantService = consultantService;
        this.availabilityService = availabilityService;
        this.bookingService = bookingService;
    }

    // Get all consultants
    @GetMapping
    public List<Consultant> getAllConsultants() {
        return consultantService.getAllConsultants();
    }

    // Get consultants by category
    @GetMapping("/doctors/category/{category}")
    public List<Consultant> getConsultantsByCategory(@PathVariable("category") String category) {
        return consultantService.getConsultantsByCategory(category);
    }

    // Add consultant
    @PostMapping
    public Consultant addConsultant(@RequestBody Consultant consultant) {
        return consultantService.addConsultant(consultant);
    }

    // Get availability for a doctor
    @GetMapping("/doctors/{doctorId}/availability")
    public List<Availability> getAvailability(@PathVariable("doctorId") Long doctorId) {
        return availabilityService.getAvailabilityByDoctor(doctorId);
    }

    // Book a slot
    @PutMapping("/availability/{slotId}/book")
    public Availability bookSlot(@PathVariable("slotId") Long slotId) {
        return availabilityService.bookSlot(slotId);
    }

    // ✅ Create booking - WAS MISSING!
    @PostMapping("/bookings")
    public Booking createBooking(@RequestBody Booking booking) {
        return bookingService.createBooking(booking);
    }

    // ✅ Get bookings by email - fixed @RequestParam
    @GetMapping("/bookings")
    public List<Booking> getBookings(@RequestParam("email") String email) {
        return bookingService.getBookingsByEmail(email);
    }

    // Cancel booking
    @PutMapping("/bookings/{id}/cancel")
    public Booking cancelBooking(@PathVariable("id") Long id) {
        return bookingService.cancelBooking(id);
    }
}