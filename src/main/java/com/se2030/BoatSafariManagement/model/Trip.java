package com.se2030.BoatSafariManagement.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Trip {
    private Integer tripId;
    private String tripName;
    private Integer duration;
    private LocalDateTime departureTime;
    private String destinations;
    private Integer availability;
    private String description;
    private BigDecimal basePrice;
}