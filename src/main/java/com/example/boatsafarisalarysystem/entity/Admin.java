package com.example.boatsafarisalarysystem.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adminID;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String role;

    @OneToMany(mappedBy = "approver", cascade = CascadeType.ALL)
    private List<LeaveRequest> approvedLeaves = new ArrayList<>();

    // Note: SalaryPayment uses Integer authorizedByAdminId, not a direct entity relationship
    // @OneToMany(mappedBy = "authorizedBy", cascade = CascadeType.ALL)
    // private List<SalaryPayment> authorizedPayments = new ArrayList<>();

    @OneToMany(mappedBy = "performedBy", cascade = CascadeType.ALL)
    private List<SalaryAudit> audits = new ArrayList<>();

    // Getters and Setters
    public Integer getAdminID() { return adminID; }
    public void setAdminID(Integer adminID) { this.adminID = adminID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public List<LeaveRequest> getApprovedLeaves() { return approvedLeaves; }
    public void setApprovedLeaves(List<LeaveRequest> approvedLeaves) { this.approvedLeaves = approvedLeaves; }
    public List<SalaryAudit> getAudits() { return audits; }
    public void setAudits(List<SalaryAudit> audits) { this.audits = audits; }
}