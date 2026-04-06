// service/BookingService.java
package com.mediquick.consultant.service;

import com.mediquick.consultant.entity.Booking;
import com.mediquick.consultant.entity.Consultant;
import com.mediquick.consultant.repository.BookingRepository;
import com.mediquick.consultant.repository.ConsultantRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ConsultantRepository consultantRepository;

    public BookingService(BookingRepository bookingRepository,
                          ConsultantRepository consultantRepository) {
        this.bookingRepository = bookingRepository;
        this.consultantRepository = consultantRepository;
    }

    public Booking createBooking(Booking booking) {
        // Auto-generate booking reference
        booking.setBookingReference("BK" + UUID.randomUUID()
                .toString().substring(0, 8).toUpperCase());

        // Fill in doctor details from DB
        consultantRepository.findById(booking.getDoctorId()).ifPresent(doc -> {
            booking.setDoctorName(doc.getName());
            booking.setSpecialization(doc.getSpecialization());
            booking.setCategory(doc.getCategory());
        });

        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByEmail(String email) {
        return bookingRepository.findByUserEmail(email);
    }

    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus("CANCELLED");
        return bookingRepository.save(booking);
    }
}