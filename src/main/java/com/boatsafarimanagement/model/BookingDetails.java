package com.boatsafarimanagement.model;

//import com.boatsafarimanagement.model.Boat;
//import com.boatsafarimanagement.model.Booking;
//import com.boatsafarimanagement.model.Staff;
//import com.boatsafarimanagement.model.User;
//import com.boatsafarimanagement.model.Trip;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingDetails {
//    private Booking booking;
//    private Staff staff;
//    private Boat boat;
//    private User user;
//    private Trip trip;

    private int bookingId;
    private int customerId;
    private String tripName;
    private LocalDateTime bookingDate;
    private int numOfGuests;
    private BigDecimal totalPrice;
    private String status;
    private String guideName;
    private String driverName;
    private String boatName;
    private String specialRequests;

    // Getters and Setters
    public int getBookingId() {
        return bookingId;
    }
    public void setBooking(int bookingId) {
        this.bookingId = bookingId;
    }
    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public String getTripName() {
        return tripName;
    }
    public void setTripName(String tripName) {
        this.tripName = tripName;
    }
    public LocalDateTime getBookingDate() {
        return bookingDate;
    }
    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }
    public int getNumOfGuests() {
        return numOfGuests;
    }
    public void setNumOfGuests(int numOfGuests) {
        this.numOfGuests = numOfGuests;
    }
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getGuideName() {
        return guideName;
    }
    public void setGuideName(String guideName) {
        this.guideName = guideName;
    }
    public String getDriverName() {
        return driverName;
    }
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
    public String getBoatName() {
        return boatName;
    }
    public void setBoatName(String boatName) {
        this.boatName = boatName;
    }
    public String getSpecialRequests() {
        return specialRequests;
    }
    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }


}
