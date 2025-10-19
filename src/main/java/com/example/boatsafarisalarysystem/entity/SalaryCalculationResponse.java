package com.example.boatsafarisalarysystem.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SalaryCalculationResponse {
    private Integer salaryId;
    private Integer staffId;
    private String staffName;
    private String role;
    private LocalDateTime paymentDate;
    private String period;
    private Integer tripCount;
    private BigDecimal baseSalary;
    private BigDecimal tripIncentives;
    private BigDecimal weatherAllowances;
    private BigDecimal totalAmount;
    private String status;
    private Integer authorizedBy;

    // Constructors
    public SalaryCalculationResponse() {}

    public SalaryCalculationResponse(Integer salaryId, Integer staffId, String staffName, String role, 
                                   LocalDateTime paymentDate, String period, Integer tripCount,
                                   BigDecimal baseSalary, BigDecimal tripIncentives, BigDecimal weatherAllowances, 
                                   BigDecimal totalAmount, String status, Integer authorizedBy) {
        this.salaryId = salaryId;
        this.staffId = staffId;
        this.staffName = staffName;
        this.role = role;
        this.paymentDate = paymentDate;
        this.period = period;
        this.tripCount = tripCount;
        this.baseSalary = baseSalary;
        this.tripIncentives = tripIncentives;
        this.weatherAllowances = weatherAllowances;
        this.totalAmount = totalAmount;
        this.status = status;
        this.authorizedBy = authorizedBy;
    }

    // Getters and Setters
    public Integer getSalaryId() { return salaryId; }
    public void setSalaryId(Integer salaryId) { this.salaryId = salaryId; }

    public Integer getStaffId() { return staffId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }

    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    public Integer getTripCount() { return tripCount; }
    public void setTripCount(Integer tripCount) { this.tripCount = tripCount; }

    public BigDecimal getBaseSalary() { return baseSalary; }
    public void setBaseSalary(BigDecimal baseSalary) { this.baseSalary = baseSalary; }

    public BigDecimal getTripIncentives() { return tripIncentives; }
    public void setTripIncentives(BigDecimal tripIncentives) { this.tripIncentives = tripIncentives; }

    public BigDecimal getWeatherAllowances() { return weatherAllowances; }
    public void setWeatherAllowances(BigDecimal weatherAllowances) { this.weatherAllowances = weatherAllowances; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getAuthorizedBy() { return authorizedBy; }
    public void setAuthorizedBy(Integer authorizedBy) { this.authorizedBy = authorizedBy; }
}
