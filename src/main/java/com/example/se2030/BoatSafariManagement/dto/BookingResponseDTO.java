package com.example.se2030.BoatSafariManagement.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
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
}