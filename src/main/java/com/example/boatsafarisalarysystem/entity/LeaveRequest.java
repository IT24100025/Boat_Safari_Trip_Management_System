package com.example.boatsafarisalarysystem.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "LeaveRequest")
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer requestId;

    @ManyToOne
    @JoinColumn(name = "StaffID", nullable = false)
    private Staff staff;

    @Column(nullable = false)
    private Date requestDate;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @Column(nullable = false)
    private String reason;

    @Column(columnDefinition = "VARCHAR(8) DEFAULT 'pending'")
    private String status = "pending";

    @ManyToOne
    @JoinColumn(name = "approver_id")
    private Admin approver;

    private Date approvalDate;

    // Getters and Setters
    public Integer getRequestId()
    { return requestId; }
    public void setRequestId(Integer requestId)
    { this.requestId = requestId; }
    public Staff getStaff()
    { return staff; }
    public void setStaff(Staff staff)
    { this.staff = staff; }
    public Date getRequestDate()
    { return requestDate; }
    public void setRequestDate(Date requestDate)
    { this.requestDate = requestDate; }
    public Date getStartDate()
    { return startDate; }
    public void setStartDate(Date startDate)
    { this.startDate = startDate; }
    public Date getEndDate()
    { return endDate; }
    public void setEndDate(Date endDate)
    { this.endDate = endDate; }
    public String getReason()
    { return reason; }
    public void setReason(String reason)
    { this.reason = reason; }
    public String getStatus()
    { return status; }
    public void setStatus(String status)
    { this.status = status; }
    public Admin getApprover()
    { return approver; }
    public void setApprover(Admin approver)
    { this.approver = approver; }
    public Date getApprovalDate()
    { return approvalDate; }
    public void setApprovalDate(Date approvalDate)
    { this.approvalDate = approvalDate; }
}