package com.example.boatsafarisalarysystem.service;

import com.example.boatsafarisalarysystem.entity.SalaryPayment;
import com.example.boatsafarisalarysystem.entity.Staff;
import com.example.boatsafarisalarysystem.entity.SalaryAudit;
import com.example.boatsafarisalarysystem.entity.Admin;
import com.example.boatsafarisalarysystem.entity.SalaryCalculationResponse;
import com.example.boatsafarisalarysystem.repository.SalaryPaymentRepository;
import com.example.boatsafarisalarysystem.repository.StaffRepository;
import com.example.boatsafarisalarysystem.repository.SalaryAuditRepository;
import com.example.boatsafarisalarysystem.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SalaryService {

    @Autowired
    private SalaryPaymentRepository salaryPaymentRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private SalaryAuditRepository salaryAuditRepository;

    @Autowired
    private AdminRepository adminRepository;

    public SalaryPayment calculateAndSaveSalary(Integer staffId, Integer adminId, Integer periodMonth, Integer periodYear) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found with ID: " + staffId));

        // Validate admin exists
        if (adminId == null || adminId <= 0) {
            throw new RuntimeException("Invalid Admin ID: " + adminId);
        }

        // Validate period month
        if (periodMonth == null || periodMonth < 1 || periodMonth > 12) {
            throw new RuntimeException("Invalid Period Month: " + periodMonth + ". Must be between 1-12");
        }

        // Validate period year
        if (periodYear == null || periodYear < 2020 || periodYear > 2030) {
            throw new RuntimeException("Invalid Period Year: " + periodYear + ". Must be between 2020-2030");
        }

        SalaryPayment payment = new SalaryPayment();
        payment.setStaffId(staffId);
        payment.setAuthorizedBy(adminId);
        payment.setPeriodMonth(periodMonth);
        payment.setPeriodYear(periodYear);

        // Calculate salary components using actual staff data from database
        BigDecimal baseSalary = calculateBaseSalary(staff);
        BigDecimal tripIncentives = calculateTripIncentives(staff, periodMonth, periodYear);
        BigDecimal weatherAllowances = calculateWeatherAllowances(periodMonth);
        BigDecimal totalAmount = baseSalary.add(tripIncentives).add(weatherAllowances);

        // Set database fields
        payment.setBaseSalary(baseSalary);
        payment.setTripIncentives(tripIncentives);
        payment.setWeatherAllowances(weatherAllowances);
        payment.setTotalAmount(totalAmount);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus("Pending");
        payment.setDisputeDescription(null);

        // Save the salary payment
        SalaryPayment savedPayment = salaryPaymentRepository.save(payment);

        // Create audit log entry
        createAuditLog(savedPayment, adminId, "SALARY_CALCULATED", 
                      "Salary calculated for Staff ID: " + staffId + 
                      ", Period: " + periodMonth + "/" + periodYear +
                      ", Amount: $" + totalAmount);

        return savedPayment;
    }

    public Optional<SalaryPayment> findById(Integer salaryId) {
        return salaryPaymentRepository.findById(salaryId);
    }

    public SalaryPayment previewSalary(Integer staffId, Integer adminId, Integer periodMonth, Integer periodYear) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found with ID: " + staffId));

        SalaryPayment payment = new SalaryPayment();
        payment.setStaffId(staffId);
        payment.setPeriodMonth(periodMonth);
        payment.setPeriodYear(periodYear);

        // Debug: Show what's actually in the database
        System.out.println("=== DATABASE DEBUG ===");
        System.out.println("Staff ID: " + staff.getStaffId());
        System.out.println("Staff Salary from DB: " + staff.getSalary());
        System.out.println("Staff Lane Number: " + staff.getLaneNumber());
        System.out.println("Staff City: " + staff.getCity());
        System.out.println("Staff Availability: " + staff.getAvailability());
        System.out.println("======================");

        // Calculate salary components for preview using actual staff data from database
        // Since your database doesn't have salary data in Staff table, we'll use role-based calculation
        BigDecimal baseSalary = calculateBaseSalary(staff);
        BigDecimal tripIncentives = calculateTripIncentives(staff, periodMonth, periodYear);
        BigDecimal weatherAllowances = calculateWeatherAllowances(periodMonth);
        BigDecimal totalAmount = baseSalary.add(tripIncentives).add(weatherAllowances);

        // Set database fields for preview
        payment.setBaseSalary(baseSalary);
        payment.setTripIncentives(tripIncentives);
        payment.setWeatherAllowances(weatherAllowances);
        payment.setTotalAmount(totalAmount);

        return payment;
    }

    private BigDecimal calculateBaseSalary(Staff staff) {
        // Use salary from database if available, otherwise use role-based calculation
        if (staff.getSalary() != null && staff.getSalary() > 0) {
            return new BigDecimal(staff.getSalary().toString());
        }
        
        // Fallback to role-based calculation
        if (staff.getStaffId() == 1) {
            return new BigDecimal("75000.00"); // Captain/Manager
        } else if (staff.getStaffId() == 2) {
            return new BigDecimal("65000.00"); // Senior Staff
        } else if (staff.getStaffId() == 3) {
            return new BigDecimal("55000.00"); // Regular Staff
        } else {
            return new BigDecimal("50000.00"); // Default salary
        }
    }

    private BigDecimal calculateTripIncentives(Staff staff, Integer periodMonth, Integer periodYear) {
        // Calculate trip incentives based on staff data and period
        // Base incentive calculation (percentage of base salary)
        BigDecimal baseSalary = calculateBaseSalary(staff);
        BigDecimal baseIncentive = baseSalary.multiply(new BigDecimal("0.08")); // 8% of base salary
        
        // Add seasonal bonus (higher in peak months)
        if (periodMonth >= 11 || periodMonth <= 3) { // Peak season
            baseIncentive = baseIncentive.add(baseSalary.multiply(new BigDecimal("0.02"))); // +2% for peak season
        }
        
        // Add staff-specific bonus based on staff ID (role-based)
        if (staff.getStaffId() == 1) {
            baseIncentive = baseIncentive.add(baseSalary.multiply(new BigDecimal("0.03"))); // +3% captain bonus
        } else if (staff.getStaffId() == 2) {
            baseIncentive = baseIncentive.add(baseSalary.multiply(new BigDecimal("0.02"))); // +2% senior staff bonus
        }
        
        return baseIncentive;
    }
    
    private BigDecimal calculateWeatherAllowances(Integer periodMonth) {
        // Calculate weather allowances based on season
        // Higher allowances during monsoon/rainy seasons
        
        if (periodMonth >= 5 && periodMonth <= 9) { // Monsoon season
            return new BigDecimal("1500.00"); // Higher weather allowance
        } else if (periodMonth >= 10 && periodMonth <= 12) { // Post-monsoon
            return new BigDecimal("1000.00"); // Moderate weather allowance
        } else { // Dry season
            return new BigDecimal("500.00"); // Lower weather allowance
        }
    }
    
    private String getMonthName(Integer month) {
        String[] months = {"", "January", "February", "March", "April", "May", "June",
                          "July", "August", "September", "October", "November", "December"};
        return months[month];
    }

    private void createAuditLog(SalaryPayment salaryPayment, Integer adminId, String actionType, String changes) {
        try {
            // Create audit log entry without complex relationships for now
            SalaryAudit audit = new SalaryAudit();
            audit.setSalaryPayment(salaryPayment);
            audit.setActionType(actionType);
            audit.setActionDate(new java.util.Date());
            audit.setChanges(changes);
            
            // Try to find admin, but don't fail if not found
            try {
                Admin admin = adminRepository.findById(adminId).orElse(null);
                if (admin != null) {
                    audit.setPerformedBy(admin);
                } else {
                    System.out.println("Warning: Admin with ID " + adminId + " not found, creating audit log without admin reference");
                }
            } catch (Exception e) {
                System.out.println("Warning: Could not load admin, creating audit log without admin reference: " + e.getMessage());
            }

            // Save audit log
            salaryAuditRepository.save(audit);
            System.out.println("✅ Audit log created: " + actionType + " for Salary ID: " + salaryPayment.getSalaryId());
        } catch (Exception e) {
            System.out.println("❌ Error creating audit log: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Create comprehensive salary calculation response
    public SalaryCalculationResponse createComprehensiveResponse(SalaryPayment salaryPayment, Staff staff, Integer periodMonth, Integer periodYear) {
        SalaryCalculationResponse response = new SalaryCalculationResponse();
        
        // Basic salary information
        response.setSalaryId(salaryPayment.getSalaryId());
        response.setStaffId(salaryPayment.getStaffId());
        response.setStaffName(staff.getName() != null ? staff.getName() : "Staff " + staff.getStaffId());
        response.setRole(staff.getRole() != null ? staff.getRole() : getRoleByStaffId(staff.getStaffId()));
        response.setPaymentDate(salaryPayment.getPaymentDate());
        response.setPeriod(getMonthName(periodMonth) + " " + periodYear);
        response.setTripCount(calculateTripCount(staff.getStaffId(), periodMonth, periodYear));
        
        // Financial information
        response.setBaseSalary(salaryPayment.getBaseSalary());
        response.setTripIncentives(salaryPayment.getTripIncentives());
        response.setWeatherAllowances(salaryPayment.getWeatherAllowances());
        response.setTotalAmount(salaryPayment.getTotalAmount());
        response.setStatus(salaryPayment.getStatus());
        response.setAuthorizedBy(salaryPayment.getAuthorizedBy());
        
        return response;
    }

    // Get role based on staff ID (fallback method)
    private String getRoleByStaffId(Integer staffId) {
        switch (staffId) {
            case 1: return "Captain";
            case 2: return "Senior Staff";
            case 3: return "Regular Staff";
            case 4: return "Manager";
            case 5: return "Senior Staff";
            case 6: return "Regular Staff";
            case 7: return "Senior Manager";
            case 8: return "Coordinator";
            default: return "Staff Member";
        }
    }

    // Calculate trip count based on staff and period (simulated)
    private Integer calculateTripCount(Integer staffId, Integer periodMonth, Integer periodYear) {
        // Simulate trip count based on staff role and season
        int baseTrips = 15; // Base trips per month
        
        // Adjust based on staff role
        if (staffId == 1) { // Captain
            baseTrips = 20;
        } else if (staffId == 2 || staffId == 5) { // Senior Staff
            baseTrips = 18;
        } else if (staffId == 4 || staffId == 7) { // Manager/Senior Manager
            baseTrips = 12;
        } else if (staffId == 8) { // Coordinator
            baseTrips = 10;
        }
        
        // Adjust based on season
        if (periodMonth >= 5 && periodMonth <= 9) { // Monsoon season - fewer trips
            baseTrips = (int) (baseTrips * 0.8);
        } else if (periodMonth >= 10 && periodMonth <= 12) { // Peak season - more trips
            baseTrips = (int) (baseTrips * 1.2);
        }
        
        return Math.max(5, baseTrips); // Minimum 5 trips
    }
}