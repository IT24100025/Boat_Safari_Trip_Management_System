package com.se2030.BoatSafariManagement.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Payment {
    private Integer paymentId;
    private Integer bookingId; // Store ID instead of object for simplicity
    private LocalDateTime paymentDate;
    private BigDecimal amount;
    private String status;
    private String paymentMethod;
    private String transactionId;
}