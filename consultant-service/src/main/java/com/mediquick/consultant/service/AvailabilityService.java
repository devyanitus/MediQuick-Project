package com.mediquick.consultant.service;

import com.mediquick.consultant.entity.Availability;
import com.mediquick.consultant.repository.AvailabilityRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;

    public AvailabilityService(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    public List<Availability> getAvailabilityByDoctor(Long doctorId) {
        return availabilityRepository.findByDoctorId(doctorId);
    }

    public Availability bookSlot(Long slotId) {
        Availability slot = availabilityRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));
        slot.setBooked(true);
        return availabilityRepository.save(slot);
    }
}