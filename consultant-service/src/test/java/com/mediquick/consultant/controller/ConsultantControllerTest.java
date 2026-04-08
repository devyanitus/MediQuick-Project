package com.mediquick.consultant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediquick.consultant.entity.*;
import com.mediquick.consultant.security.JwtService;
import com.mediquick.consultant.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConsultantController.class)
@AutoConfigureMockMvc(addFilters = false)
class ConsultantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private ConsultantService consultantService;

//    @MockBean
//    private JwtService jwtService;

    @MockBean
    private AvailabilityService availabilityService;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ GET ALL CONSULTANTS
    @Test
    void getAllConsultants() throws Exception {

        Consultant c = new Consultant();
        c.setId(1L);
        c.setName("Dr John");
        c.setSpecialization("Cardiology");
        c.setCategory("Heart");
        c.setEmail("john@gmail.com");

        when(consultantService.getAllConsultants())
                .thenReturn(List.of(c));

        mockMvc.perform(get("/consultants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Dr John"))
                .andExpect(jsonPath("$[0].category").value("Heart"));
    }

    // ✅ GET BY CATEGORY
    @Test
    void getByCategory() throws Exception {

        Consultant c = new Consultant();
        c.setId(1L);
        c.setName("Dr John");
        c.setSpecialization("Cardiology");
        c.setCategory("Heart");
        c.setEmail("john@gmail.com");

        when(consultantService.getConsultantsByCategory("Heart"))
                .thenReturn(List.of(c));

        mockMvc.perform(get("/consultants/doctors/category/Heart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Heart"));
    }

    // ✅ ADD CONSULTANT
    @Test
    void addConsultant() throws Exception {

        Consultant c = new Consultant();
        c.setName("Dr John");
        c.setSpecialization("Cardiology");
        c.setCategory("Heart");
        c.setEmail("john@gmail.com");

        when(consultantService.addConsultant(any()))
                .thenReturn(c);

        mockMvc.perform(post("/consultants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(c)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dr John"))
                .andExpect(jsonPath("$.category").value("Heart"));
    }

    // ✅ GET AVAILABILITY
    @Test
    void getAvailability() throws Exception {

        Availability a = new Availability();
        a.setTimeSlot("10:00 AM");
        a.setAvailableDate(LocalDate.now());
        a.setBooked(false);

        when(availabilityService.getAvailabilityByDoctor(1L))
                .thenReturn(List.of(a));

        mockMvc.perform(get("/consultants/doctors/1/availability"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].timeSlot").value("10:00 AM"));
    }

    // ✅ BOOK SLOT
    @Test
    void bookSlot() throws Exception {

        Availability a = new Availability();
        a.setBooked(true);

        when(availabilityService.bookSlot(1L))
                .thenReturn(a);

        mockMvc.perform(put("/consultants/availability/1/book"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booked").value(true));
    }

    // ✅ CREATE BOOKING
    @Test
    void createBooking() throws Exception {

        Booking b = new Booking();
        b.setUserEmail("user@gmail.com");
        b.setDoctorId(1L);
        b.setBookingDate(LocalDate.now());
        b.setTimeSlot("10:00 AM");

        when(bookingService.createBooking(any()))
                .thenReturn(b);

        mockMvc.perform(post("/consultants/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(b)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userEmail").value("user@gmail.com"));
    }

    // ✅ GET BOOKINGS
    @Test
    void getBookings() throws Exception {

        Booking b = new Booking();
        b.setUserEmail("user@gmail.com");
        b.setDoctorId(1L);
        b.setBookingDate(LocalDate.now());
        b.setTimeSlot("10:00 AM");

        when(bookingService.getBookingsByEmail("user@gmail.com"))
                .thenReturn(List.of(b));

        mockMvc.perform(get("/consultants/bookings?email=user@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userEmail").value("user@gmail.com"));
    }

    // ✅ CANCEL BOOKING
    @Test
    void cancelBooking() throws Exception {

        Booking b = new Booking();
        b.setStatus("CANCELLED");

        when(bookingService.cancelBooking(1L))
                .thenReturn(b);

        mockMvc.perform(put("/consultants/bookings/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }
}