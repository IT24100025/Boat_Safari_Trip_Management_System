package com.example.boatsafarisalarysystem.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "SalaryPayment")
public class SalaryPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SalaryID")
    private Integer salaryId;

    @Column(name = "StaffID")
    private Integer staffId;

    @Column(name = "periodMonth")
    private Integer periodMonth;

    @Column(name = "periodYear")
    private Integer periodYear;

    @Column(name = "baseSalary")
    private BigDecimal baseSalary;

    @Column(name = "tripIncentives")
    private BigDecimal tripIncentives;

    @Column(name = "weatherAllowances")
    private BigDecimal weatherAllowances;

    @Column(name = "totalAmount")
    private BigDecimal totalAmount;

    @Column(name = "paymentDate")
    private LocalDateTime paymentDate;

    @Column(name = "status")
    private String status;

    @Column(name = "disputeDescription")
    private String disputeDescription;

    @Column(name = "authorized_by")
    private Integer authorizedBy;

    // Getters and Setters
    public Integer getSalaryId() { return salaryId; }
    public void setSalaryId(Integer salaryId) { this.salaryId = salaryId; }

    public Integer getStaffId() { return staffId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }

    public Integer getPeriodMonth() { return periodMonth; }
    public void setPeriodMonth(Integer periodMonth) { this.periodMonth = periodMonth; }

    public Integer getPeriodYear() { return periodYear; }
    public void setPeriodYear(Integer periodYear) { this.periodYear = periodYear; }

    public BigDecimal getBaseSalary() { return baseSalary; }
    public void setBaseSalary(BigDecimal baseSalary) { this.baseSalary = baseSalary; }

    public BigDecimal getTripIncentives() { return tripIncentives; }
    public void setTripIncentives(BigDecimal tripIncentives) { this.tripIncentives = tripIncentives; }

    public BigDecimal getWeatherAllowances() { return weatherAllowances; }
    public void setWeatherAllowances(BigDecimal weatherAllowances) { this.weatherAllowances = weatherAllowances; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDisputeDescription() { return disputeDescription; }
    public void setDisputeDescription(String disputeDescription) { this.disputeDescription = disputeDescription; }

    public Integer getAuthorizedBy() { return authorizedBy; }
    public void setAuthorizedBy(Integer authorizedBy) { this.authorizedBy = authorizedBy; }
}