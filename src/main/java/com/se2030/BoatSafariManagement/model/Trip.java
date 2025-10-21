package com.se2030.BoatSafariManagement.model;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class Trip {
    private Integer tripId;
    private String tripName;
    private int duration;
    private LocalDateTime departureTime;
    private String destinations;
    private Integer availability;
    private String description;
    private BigDecimal basePrice;
    private String status;
    private boolean hasConflict;
    private String conflictDetails;
    private boolean capacityWarning;
    private String capacityStatus;


    public Trip() {
        // Initialize with default values
        this.status = "Scheduled";
        this.hasConflict = false;
        this.capacityWarning = false;
        this.capacityStatus = "AVAILABLE";
    }

    public Trip(Integer tripId, String tripName, int duration, LocalDateTime departureTime, String destinations, Integer availability, String description, BigDecimal basePrice, String status, boolean hasConflict, String conflictDetails, boolean capacityWarning, String capacityStatus) {
        this.tripId = tripId;
        this.tripName = tripName;
        this.duration = duration;
        this.departureTime = departureTime;
        this.destinations = destinations;
        this.availability = availability;
        this.description = description;
        this.basePrice = basePrice;
        this.status = status;
        this.hasConflict = hasConflict;
        this.conflictDetails = conflictDetails;
        this.capacityWarning = capacityWarning;
        this.capacityStatus = capacityStatus;
    }

    // Getters and Setters
    public Integer getTripId() { return tripId; }
    public void setTripId(Integer tripId) { this.tripId = tripId; }
    public String getTripName() { return tripName; }
    public void setTripName(String tripName) { this.tripName = tripName; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }
    public String getDestinations() { return destinations; }
    public void setDestinations(String destinations) { this.destinations = destinations; }
    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getAvailability() { return availability; }
    public void setAvailability(Integer availability) {
        this.availability = availability;
        // Update capacity warning and status when availability changes
        updateCapacityStatus();
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // Getters and Setters for conflict detection fields
    public boolean isHasConflict() { return hasConflict; }
    public void setHasConflict(boolean hasConflict) { this.hasConflict = hasConflict; }

    public String getConflictDetails() { return conflictDetails; }
    public void setConflictDetails(String conflictDetails) { this.conflictDetails = conflictDetails; }

    public boolean isCapacityWarning() { return capacityWarning; }
    public void setCapacityWarning(boolean capacityWarning) { this.capacityWarning = capacityWarning; }

    public String getCapacityStatus() { return capacityStatus; }
    public void setCapacityStatus(String capacityStatus) {
        this.capacityStatus = capacityStatus;
        // Update capacity warning based on status
        this.capacityWarning = "WARNING".equals(capacityStatus) || "FULL".equals(capacityStatus);
    }

    // Helper method to update capacity status based on availability
    private void updateCapacityStatus() {
        if (this.availability == null) {
            this.capacityStatus = "AVAILABLE";
            this.capacityWarning = false;
            return;
        }

        if (this.availability <= 0) {
            this.capacityStatus = "FULL";
            this.capacityWarning = true;
        } else if (this.availability <= 5) {
            this.capacityStatus = "WARNING";
            this.capacityWarning = true;
        } else {
            this.capacityStatus = "AVAILABLE";
            this.capacityWarning = false;
        }
    }

    // Override toString for better debugging
    @Override
    public String toString() {
        return "Trip{" +
                "tripId=" + tripId +
                ", tripName='" + tripName + '\'' +
                ", duration=" + duration +
                ", departureTime=" + departureTime +
                ", destinations='" + destinations + '\'' +
                ", availability=" + availability +
                ", hasConflict=" + hasConflict +
                ", conflictDetails='" + conflictDetails + '\'' +
                ", capacityWarning=" + capacityWarning +
                ", capacityStatus='" + capacityStatus + '\'' +
                '}';
    }
}