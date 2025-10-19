package com.example.boatsafarisalarysystem.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SalaryAudit")
public class SalaryAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer auditId;

    @ManyToOne
    @JoinColumn(name = "SalaryID", nullable = false)
    private SalaryPayment salaryPayment;

    @Column(nullable = false)
    private String actionType;

    @Column(nullable = false)
    private Date actionDate;

    @ManyToOne
    @JoinColumn(name = "performed_by", nullable = false)
    private Admin performedBy;

    private String changes;

    // Getters and Setters
    public Integer getAuditId()
    { return auditId; }
    public void setAuditId(Integer auditId)
    { this.auditId = auditId; }
    public SalaryPayment getSalaryPayment()
    { return salaryPayment; }
    public void setSalaryPayment(SalaryPayment salaryPayment)
    { this.salaryPayment = salaryPayment; }
    public String getActionType()
    { return actionType; }
    public void setActionType(String actionType)
    { this.actionType = actionType; }
    public Date getActionDate()
    { return actionDate; }
    public void setActionDate(Date actionDate)
    { this.actionDate = actionDate; }
    public Admin getPerformedBy()
    { return performedBy; }
    public void setPerformedBy(Admin performedBy)
    { this.performedBy = performedBy; }
    public String getChanges()
    { return changes; }
    public void setChanges(String changes)
    { this.changes = changes; }
}