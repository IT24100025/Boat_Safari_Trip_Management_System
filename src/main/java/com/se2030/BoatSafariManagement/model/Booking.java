package com.se2030.BoatSafariManagement.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Booking {
    private Integer bookingId;
    private Customer customer;
    private Trip trip;
    private LocalDateTime bookingDate;
    private LocalTime bookingTime;
    private Integer numOfGuests;
    private BigDecimal totalPrice;
    private String status;
    private String specialRequests;
    private Integer assignedStaffId;
    private Integer assignedBoatId;

    // ADDED FROM STAFF_ASSIGNMENT BRANCH
    private Integer guideId;         // staff.Role = 'Guide'
    private Integer driverId;        // staff.Role = 'Driver'
    private Integer boatId;          // BoatId (note: lowercase 'b' for consistency)

    // Getters and Setters - ORIGINAL FROM MERGED BRANCH
    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Trip getTrip() { return trip; }
    public void setTrip(Trip trip) { this.trip = trip; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public LocalTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalTime bookingTime) { this.bookingTime = bookingTime; }

    public Integer getNumOfGuests() { return numOfGuests; }
    public void setNumOfGuests(Integer numOfGuests) { this.numOfGuests = numOfGuests; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }

    public Integer getAssignedStaffId() { return assignedStaffId; }
    public void setAssignedStaffId(Integer assignedStaffId) { this.assignedStaffId = assignedStaffId; }

    public Integer getAssignedBoatId() { return assignedBoatId; }
    public void setAssignedBoatId(Integer assignedBoatId) { this.assignedBoatId = assignedBoatId; }

    // ADDED GETTERS AND SETTERS FROM STAFF_ASSIGNMENT BRANCH
    public Integer getGuideId() { return guideId; }
    public void setGuideId(Integer guideId) { this.guideId = guideId; }

    public Integer getDriverId() { return driverId; }
    public void setDriverId(Integer driverId) { this.driverId = driverId; }

    public Integer getBoatId() { return boatId; }
    public void setBoatId(Integer boatId) { this.boatId = boatId; }

    // Helper methods for compatibility
    /**
     * Gets customer ID from Customer object (for Staff_Assignment compatibility)
     */
    public Integer getCustomerId() {
        return customer != null ? customer.getUserId() : null;
    }

    /**
     * Gets trip ID from Trip object (for Staff_Assignment compatibility)
     */
    public Integer getTripId() {
        return trip != null ? trip.getTripId() : null;
    }
}