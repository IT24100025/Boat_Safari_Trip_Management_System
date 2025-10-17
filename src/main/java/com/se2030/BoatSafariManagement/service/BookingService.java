package com.se2030.BoatSafariManagement.service;

import com.se2030.BoatSafariManagement.model.Boat;
import com.se2030.BoatSafariManagement.model.Booking;
import com.se2030.BoatSafariManagement.model.BookingDetails;
import com.se2030.BoatSafariManagement.model.Staff;
import com.se2030.BoatSafariManagement.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    @Autowired
    private BookingRepository repo;

    public Booking getBooking(int id) {
        return repo.getBooking(id);
    }

    public List<Boat> getAvailableBoats() {
        return repo.getAvailableBoats();
    }

    public void assignBoat(int bookingId, int boatId) {
        repo.assignBoatToBooking(bookingId, boatId);
    }

    public List<Staff> getGuides(String guide) {
        return repo.getStaffByRole("Guide");
    }

    public List<Staff> getDrivers(String driver) {
        return repo.getStaffByRole("Driver");
    }


    public void assignStaff(int bookingId, int guideId, int driverId) {
        repo.assignGuideAndDriver(bookingId, guideId, driverId);
    }

    public List<BookingDetails> getAllBookings() {
        return repo.getAllBookings();
    }

    public void updateAvailability(int guideId, int driverId, int boatID){
        repo.updateAvailability(guideId, driverId, boatID);
    }
}

