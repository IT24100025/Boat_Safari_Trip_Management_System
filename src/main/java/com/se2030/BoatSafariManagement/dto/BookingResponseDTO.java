package com.se2030.BoatSafariManagement.dto;

import java.math.BigDecimal;

public class BookingResponseDTO {
    private String bookingId;
    private String customerName;
    private String tripName;
    private String date;
    private String time;
    private Integer guests;
    private String destinations;
    private BigDecimal totalAmount;
    private String paymentTime;
    private String status;

    // Getters and Setters
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getTripName() { return tripName; }
    public void setTripName(String tripName) { this.tripName = tripName; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public Integer getGuests() { return guests; }
    public void setGuests(Integer guests) { this.guests = guests; }

    public String getDestinations() { return destinations; }
    public void setDestinations(String destinations) { this.destinations = destinations; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getPaymentTime() { return paymentTime; }
    public void setPaymentTime(String paymentTime) { this.paymentTime = paymentTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}