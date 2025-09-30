package com.se2030.BoatSafariManagement.model;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class Trip {
    private int tripId;
    private String tripName;
    private int duration;
    private LocalDateTime departureTime;
    private String destinations;
    private int availability;
    private String description;
    private BigDecimal basePrice;

    // Getters and Setters
    public int getTripId() { return tripId; }
    public void setTripId(int tripId) { this.tripId = tripId; }
    public String getTripName() { return tripName; }
    public void setTripName(String tripName) { this.tripName = tripName; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }
    public String getDestinations() { return destinations; }
    public void setDestinations(String destinations) { this.destinations = destinations; }
    public int getAvailability() { return availability; }
    public void setAvailability(int availability) { this.availability = availability; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }
}