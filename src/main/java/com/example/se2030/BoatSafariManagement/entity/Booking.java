package com.example.se2030.BoatSafariManagement.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
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
}