package com.example.boatsafarisalarysystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "StaffTripAssignment")
public class StaffTripAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer assignmentId;

    @ManyToOne
    @JoinColumn(name = "StaffID", nullable = false)
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "tripID", nullable = false)
    private Trip trip;

    @Column(nullable = false)
    private String roleOnTrip;

    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'present'")
    private String attendanceStatus = "present";

    // Getters and Setters
    public Integer getAssignmentId()
    { return assignmentId; }
    public void setAssignmentId(Integer assignmentId)
    { this.assignmentId = assignmentId; }
    public Staff getStaff()
    { return staff; }
    public void setStaff(Staff staff)
    { this.staff = staff; }
    public Trip getTrip()
    { return trip; }
    public void setTrip(Trip trip)
    { this.trip = trip; }
    public String getRoleOnTrip()
    { return roleOnTrip; }
    public void setRoleOnTrip(String roleOnTrip)
    { this.roleOnTrip = roleOnTrip; }
    public String getAttendanceStatus()
    { return attendanceStatus; }
    public void setAttendanceStatus(String attendanceStatus)
    { this.attendanceStatus = attendanceStatus; }
}