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

    // Getters and Setters
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
}