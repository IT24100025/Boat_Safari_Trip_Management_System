package com.se2030.BoatSafariManagement.model;

public class Boat {
    private Integer boatId;
    private Integer boatOwnerId;
    private String boatName;
    private String boatType;
    private Integer capacity;
    private Boolean boatAvailability;
    private String status;

    // Constructors, Getters and Setters
    public Boat() {}

    public Boat(Integer boatId, Integer boatOwnerId, String boatName, String boatType,
                Integer capacity, Boolean boatAvailability, String status) {
        this.boatId = boatId;
        this.boatOwnerId = boatOwnerId;
        this.boatName = boatName;
        this.boatType = boatType;
        this.capacity = capacity;
        this.boatAvailability = boatAvailability;
        this.status = status;
    }

    // Getters and Setters
    public Integer getBoatId() { return boatId; }
    public void setBoatId(Integer boatId) { this.boatId = boatId; }

    public Integer getBoatOwnerId() { return boatOwnerId; }
    public void setBoatOwnerId(Integer boatOwnerId) { this.boatOwnerId = boatOwnerId; }

    public String getBoatName() { return boatName; }
    public void setBoatName(String boatName) { this.boatName = boatName; }

    public String getBoatType() { return boatType; }
    public void setBoatType(String boatType) { this.boatType = boatType; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Boolean getBoatAvailability() { return boatAvailability; }
    public void setBoatAvailability(Boolean boatAvailability) { this.boatAvailability = boatAvailability; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}