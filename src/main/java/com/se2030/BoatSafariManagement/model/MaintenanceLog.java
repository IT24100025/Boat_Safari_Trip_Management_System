package com.se2030.BoatSafariManagement.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MaintenanceLog {
    private Integer maintenanceId;
    private Integer boatId;
    private Integer reportedByAdminId;
    private String issueDescription;
    private LocalDateTime dateReported;
    private LocalDateTime dateResolved;
    private BigDecimal cost; // Change from Double to BigDecimal
    private String status;

    // Constructors
    public MaintenanceLog() {}

    public MaintenanceLog(Integer maintenanceId, Integer boatId, Integer reportedByAdminId,
                          String issueDescription, LocalDateTime dateReported,
                          LocalDateTime dateResolved, BigDecimal cost, String status) {
        this.maintenanceId = maintenanceId;
        this.boatId = boatId;
        this.reportedByAdminId = reportedByAdminId;
        this.issueDescription = issueDescription;
        this.dateReported = dateReported;
        this.dateResolved = dateResolved;
        this.cost = cost;
        this.status = status;
    }

    // Getters and Setters
    public Integer getMaintenanceId() { return maintenanceId; }
    public void setMaintenanceId(Integer maintenanceId) { this.maintenanceId = maintenanceId; }

    public Integer getBoatId() { return boatId; }
    public void setBoatId(Integer boatId) { this.boatId = boatId; }

    public Integer getReportedByAdminId() { return reportedByAdminId; }
    public void setReportedByAdminId(Integer reportedByAdminId) { this.reportedByAdminId = reportedByAdminId; }

    public String getIssueDescription() { return issueDescription; }
    public void setIssueDescription(String issueDescription) { this.issueDescription = issueDescription; }

    public LocalDateTime getDateReported() { return dateReported; }
    public void setDateReported(LocalDateTime dateReported) { this.dateReported = dateReported; }

    public LocalDateTime getDateResolved() { return dateResolved; }
    public void setDateResolved(LocalDateTime dateResolved) { this.dateResolved = dateResolved; }

    public BigDecimal getCost() { return cost; } // Change return type to BigDecimal
    public void setCost(BigDecimal cost) { this.cost = cost; } // Change parameter type to BigDecimal

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}