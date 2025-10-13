package com.example.se2030.BoatSafariManagement.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class BookingRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String contact;
    private String tripName;
    private LocalDate date;
    private LocalTime time;
    private Integer guests;
    private List<String> destinations;
    private String specialRequests;
    private String cardNo;
    private String expiry;
    private String cvv;
    private String paymentMethod;
}