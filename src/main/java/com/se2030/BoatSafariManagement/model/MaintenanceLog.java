package com.se2030.BoatSafariManagement.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MaintenanceLog {
    private int maintenanceId;
    private int boatId;
    private int reportedByAdminId;
    private String issueDescription;
    private LocalDateTime dateReported;
    private LocalDateTime dateResolved;
    private BigDecimal cost;
    private String status;

    // Getters and Setters
    public int getMaintenanceId() { return maintenanceId; }
    public void setMaintenanceId(int maintenanceId) { this.maintenanceId = maintenanceId; }

    public int getBoatId() { return boatId; }
    public void setBoatId(int boatId) { this.boatId = boatId; }

    public int getReportedByAdminId() { return reportedByAdminId; }
    public void setReportedByAdminId(int reportedByAdminId) { this.reportedByAdminId = reportedByAdminId; }

    public String getIssueDescription() { return issueDescription; }
    public void setIssueDescription(String issueDescription) { this.issueDescription = issueDescription; }

    public LocalDateTime getDateReported() { return dateReported; }
    public void setDateReported(LocalDateTime dateReported) { this.dateReported = dateReported; }

    public LocalDateTime getDateResolved() { return dateResolved; }
    public void setDateResolved(LocalDateTime dateResolved) { this.dateResolved = dateResolved; }

    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}