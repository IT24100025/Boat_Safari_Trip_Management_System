package com.example.boatsafarisalarysystem.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Trip")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tripID;

    @Column(nullable = false)
    private Date scheduleDate;

    @Column(nullable = false)
    private Date departureTime; // Use Date for TIME type

    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'scheduled'")
    private String status = "scheduled";

    private String cancellationReason;

    private Integer boatID; // FK to Boat (assume exists)

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<StaffTripAssignment> assignments = new ArrayList<>();

    // Getters and Setters
    public Integer getTripID()
    { return tripID; }
    public void setTripID(Integer tripID)
    { this.tripID = tripID; }
    public Date getScheduleDate()
    { return scheduleDate; }
    public void setScheduleDate(Date scheduleDate)
    { this.scheduleDate = scheduleDate; }
    public Date getDepartureTime()
    { return departureTime; }
    public void setDepartureTime(Date departureTime)
    { this.departureTime = departureTime; }
    public String getStatus()
    { return status; }
    public void setStatus(String status)
    { this.status = status; }
    public String getCancellationReason()
    { return cancellationReason; }
    public void setCancellationReason(String cancellationReason)
    { this.cancellationReason = cancellationReason; }
    public Integer getBoatID()
    { return boatID; }
    public void setBoatID(Integer boatID)
    { this.boatID = boatID; }
    public List<StaffTripAssignment> getAssignments()
    { return assignments; }
    public void setAssignments(List<StaffTripAssignment> assignments)
    { this.assignments = assignments; }
}