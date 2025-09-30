package com.boatsafarimanagement.model;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class Booking {
    private int bookingId;
    private int customerId;
    private int tripId;
    private LocalDateTime bookingDate;
    private String status;
    private int noOfGuests;
    private BigDecimal tripPrice;
    private Integer guideId;         // staff.Role = 'Guide'
    private Integer driverId;        // staff.Role = 'Driver'
    private Integer assignedBoatId;
    private String SpecialRequests;// BoatId

    // Getters & setters
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getTripId() { return tripId; }
    public void setTripId(int tripId) { this.tripId = tripId; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getNoOfGuests() { return noOfGuests; }
    public void setNoOfGuests(int noOfGuests) { this.noOfGuests = noOfGuests; }

    public BigDecimal getTripPrice() { return tripPrice; }
    public void setTripPrice(BigDecimal tripPrice) { this.tripPrice = tripPrice; }

    public Integer getGuideId() { return guideId; }
    public void setGuideId(Integer guideId) { this.guideId = guideId; }

    public Integer getDriverId() { return driverId; }
    public void setDriverId(Integer driverId) { this.driverId = driverId; }

    public Integer getAssignedBoatId() { return assignedBoatId; }
    public void setAssignedBoatId(Integer assignedBoatId) { this.assignedBoatId = assignedBoatId; }

    public String getSpecialRequests() { return SpecialRequests; }
    public void setSpecialRequests(String specialRequests) { SpecialRequests = specialRequests;}

}

