package com.mediquick.consultant.service;

import com.mediquick.consultant.entity.Booking;
import com.mediquick.consultant.entity.Consultant;
import com.mediquick.consultant.repository.BookingRepository;
import com.mediquick.consultant.repository.ConsultantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ConsultantRepository consultantRepository;

    @InjectMocks
    private BookingService bookingService;

    private Booking booking;
    private Consultant doctor;

    @BeforeEach
    void setUp() {
        doctor = new Consultant("Dr John Smith", "General Medicine", "GP", "john@gmail.com");
        doctor.setId(1L);

        booking = new Booking();
        booking.setUserEmail("user@gmail.com");
        booking.setDoctorId(1L);
        booking.setBookingDate(LocalDate.of(2026, 4, 10));
        booking.setTimeSlot("09:00 AM");
        booking.setStatus("CONFIRMED");
    }

    // ===== createBooking =====

    @Test
    void createBooking_SavesAndReturnsBooking() {
        when(consultantRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.createBooking(booking);

        assertNotNull(result);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void createBooking_GeneratesBookingReference() {
        when(consultantRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        Booking result = bookingService.createBooking(booking);

        assertNotNull(result.getBookingReference());
        assertTrue(result.getBookingReference().startsWith("BK"));
    }

    @Test
    void createBooking_BookingReferenceIsUnique() {
        when(consultantRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        Booking booking2 = new Booking();
        booking2.setUserEmail("user2@gmail.com");
        booking2.setDoctorId(1L);
        booking2.setBookingDate(LocalDate.of(2026, 4, 11));
        booking2.setTimeSlot("12:00 PM");

        Booking result1 = bookingService.createBooking(booking);
        Booking result2 = bookingService.createBooking(booking2);

        assertNotEquals(result1.getBookingReference(), result2.getBookingReference());
    }

    @Test
    void createBooking_FillsDoctorDetails() {
        when(consultantRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        Booking result = bookingService.createBooking(booking);

        assertEquals("Dr John Smith", result.getDoctorName());
        assertEquals("General Medicine", result.getSpecialization());
        assertEquals("GP", result.getCategory());
    }

    @Test
    void createBooking_DoctorNotFound_StillSaves() {
        when(consultantRepository.findById(1L)).thenReturn(Optional.empty());
        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        Booking result = bookingService.createBooking(booking);

        // Doctor not found so name stays null but booking still saves
        assertNull(result.getDoctorName());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    // ===== getBookingsByEmail =====

    @Test
    void getBookingsByEmail_ReturnsUserBookings() {
        when(bookingRepository.findByUserEmail("user@gmail.com"))
                .thenReturn(Arrays.asList(booking));

        List<Booking> result = bookingService.getBookingsByEmail("user@gmail.com");

        assertEquals(1, result.size());
        assertEquals("user@gmail.com", result.get(0).getUserEmail());
    }

    @Test
    void getBookingsByEmail_NoBookings_ReturnsEmpty() {
        when(bookingRepository.findByUserEmail("nobody@gmail.com"))
                .thenReturn(List.of());

        List<Booking> result = bookingService.getBookingsByEmail("nobody@gmail.com");

        assertTrue(result.isEmpty());
    }

    @Test
    void getBookingsByEmail_CallsRepositoryWithCorrectEmail() {
        when(bookingRepository.findByUserEmail("user@gmail.com"))
                .thenReturn(List.of());

        bookingService.getBookingsByEmail("user@gmail.com");

        verify(bookingRepository, times(1)).findByUserEmail("user@gmail.com");
    }

    // ===== cancelBooking =====

    @Test
    void cancelBooking_SetsStatusCancelled() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking result = bookingService.cancelBooking(1L);

        assertEquals("CANCELLED", result.getStatus());
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void cancelBooking_NotFound_ThrowsException() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                bookingService.cancelBooking(99L)
        );
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void cancelBooking_ExceptionMessage_ContainsBookingNotFound() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                bookingService.cancelBooking(99L)
        );
        assertEquals("Booking not found", ex.getMessage());
    }
}