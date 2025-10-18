package com.se2030.BoatSafariManagement.service;

import com.se2030.BoatSafariManagement.model.Boat;
import com.se2030.BoatSafariManagement.model.Booking;
import com.se2030.BoatSafariManagement.model.BookingDetails;
import com.se2030.BoatSafariManagement.model.Staff;
import com.se2030.BoatSafariManagement.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class BookingService {
    private final BookingRepository repo;
    private final EmailService emailService;  // Made it final

    // ADD EmailService parameter here ⬇️
    public BookingService(BookingRepository repo, EmailService emailService) {
        this.repo = repo;
        this.emailService = emailService;  // Initialize it here
    }

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

    @Transactional
    public void assignStaff(int bookingId, int guideId, int driverId) {
        // Get current booking to check if staff are already assigned
        Booking currentBooking = repo.getBooking(bookingId);

        // Assign staff
        repo.assignGuideAndDriver(bookingId, guideId, driverId);

        // Get trip details for email
        String tripDetails = repo.getTripDetails(bookingId);

        // Send email to guide if newly assigned
        if (currentBooking.getGuideId() == null || !currentBooking.getGuideId().equals(guideId)) {
            sendStaffNotification(guideId, "Guide", bookingId, tripDetails);
        }

        // Send email to driver if newly assigned
        if (currentBooking.getDriverId() == null || !currentBooking.getDriverId().equals(driverId)) {
            sendStaffNotification(driverId, "Driver", bookingId, tripDetails);
        }
    }

    private void sendStaffNotification(int staffId, String role, int bookingId, String tripDetails) {
        try {
            Staff staffInfo = repo.getStaffEmailAndName(staffId);
            String email = staffInfo.getEmail();
            String staffName = staffInfo.getName();

            if (email != null && !email.trim().isEmpty()) {
                emailService.sendAssignmentNotification(email, staffName, role, bookingId, tripDetails);
            }
        } catch (Exception e) {
            // Log error but don't break the assignment process
            System.err.println("Failed to send notification to staff " + staffId + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateBookingStatus(int bookingId) {
        repo.updateBookingStatus(bookingId);
    }

    public List<BookingDetails> getAllBookings() {
        return repo.getAllBookings();
    }

    public void updateAvailability(int guideId, int driverId, int boatID){
        repo.updateAvailability(guideId, driverId, boatID);
    }

    public int getTodayBookingCount(){
        return repo.getTodayBookingCount();
    }

    public int getUnassignedBookingCount(){
        return repo.getUnassignedBookingCount();
    }
}