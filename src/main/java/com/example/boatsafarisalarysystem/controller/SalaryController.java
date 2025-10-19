package com.example.boatsafarisalarysystem.controller;

import com.example.boatsafarisalarysystem.entity.SalaryAudit;
import com.example.boatsafarisalarysystem.entity.SalaryPayment;
import com.example.boatsafarisalarysystem.entity.SalaryCalculationResponse;
import com.example.boatsafarisalarysystem.service.SalaryService;
import com.example.boatsafarisalarysystem.repository.SalaryAuditRepository;
import com.example.boatsafarisalarysystem.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SalaryController {

    @Autowired
    private SalaryService salaryService;

    @Autowired
    private SalaryAuditRepository salaryAuditRepository;

    @Autowired
    private StaffRepository staffRepository;

    // Match frontend: /api/salaries/calculate/{staffId}?adminId=...&periodMonth=...&periodYear=...
    @PostMapping("/salaries/calculate/{staffId}")
    public ResponseEntity<?> calculateSalary(
            @PathVariable Integer staffId,
            @RequestParam Integer adminId,
            @RequestParam Integer periodMonth,
            @RequestParam Integer periodYear) {
        try {
            SalaryPayment payment = salaryService.calculateAndSaveSalary(staffId, adminId, periodMonth, periodYear);
            var staff = staffRepository.findById(staffId).orElse(null);
            if (staff != null) {
                SalaryCalculationResponse response = salaryService.createComprehensiveResponse(payment, staff, periodMonth, periodYear);
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: An unexpected server error occurred.");
        }
    }

    // Match frontend: /api/salaries/preview/{staffId}?adminId=...&periodMonth=...&periodYear=...
    @GetMapping("/salaries/preview/{staffId}")
    public ResponseEntity<?> previewSalary(
            @PathVariable Integer staffId,
            @RequestParam Integer adminId,
            @RequestParam Integer periodMonth,
            @RequestParam Integer periodYear) {
        try {
            SalaryPayment payment = salaryService.previewSalary(staffId, adminId, periodMonth, periodYear);
            var staff = staffRepository.findById(staffId).orElse(null);
            if (staff != null) {
                SalaryCalculationResponse response = salaryService.createComprehensiveResponse(payment, staff, periodMonth, periodYear);
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: An unexpected server error occurred.");
        }
    }

    // Audit endpoints expected by HTML
    @GetMapping("/salary-audits")
    public ResponseEntity<List<SalaryAudit>> listAudits() {
        try {
            System.out.println("Attempting to fetch all audit logs...");
            List<SalaryAudit> audits = salaryAuditRepository.findAll();
            System.out.println("Found " + audits.size() + " audit logs");
            return ResponseEntity.ok(audits);
        } catch (Exception e) {
            System.out.println("Error fetching audit logs: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/salary-audits/{salaryId}")
    public ResponseEntity<List<SalaryAudit>> listAuditsBySalary(@PathVariable Integer salaryId) {
        return ResponseEntity.ok(salaryAuditRepository.findBySalaryPayment_SalaryId(salaryId));
    }

    // New endpoint to fetch audit logs by Salary ID and Admin ID
    @GetMapping("/salary-audits/{salaryId}/admin/{adminId}")
    public ResponseEntity<List<SalaryAudit>> listAuditsBySalaryAndAdmin(
            @PathVariable Integer salaryId, 
            @PathVariable Integer adminId) {
        try {
            List<SalaryAudit> audits = salaryAuditRepository.findBySalaryPayment_SalaryId(salaryId);
            
            // Filter by admin ID if needed (performed by specific admin)
            List<SalaryAudit> filteredAudits = audits.stream()
                .filter(audit -> {
                    try {
                        return audit.getPerformedBy() != null && 
                               audit.getPerformedBy().getAdminID().equals(adminId);
                    } catch (Exception e) {
                        // If there's an issue with the relationship, include the audit log anyway
                        return true;
                    }
                })
                .collect(java.util.stream.Collectors.toList());
            
            return ResponseEntity.ok(filteredAudits);
        } catch (Exception e) {
            System.out.println("Error in listAuditsBySalaryAndAdmin: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/salary-audits/{auditId}")
    public ResponseEntity<?> deleteAudit(@PathVariable Integer auditId, @RequestParam Integer adminId) {
        if (!salaryAuditRepository.existsById(auditId)) {
            return ResponseEntity.notFound().build();
        }
        salaryAuditRepository.deleteById(auditId);
        return ResponseEntity.ok().build();
    }

    // Database connection test endpoint
    @GetMapping("/test-db")
    public ResponseEntity<Map<String, Object>> testDatabaseConnection() {
        Map<String, Object> response = new HashMap<>();
        try {
            // Try to count staff records
            long staffCount = staffRepository.count();
            response.put("status", "success");
            response.put("message", "Database connection successful");
            response.put("staffCount", staffCount);
            response.put("timestamp", java.time.LocalDateTime.now());
            response.put("version", "UPDATED - Using new salary calculation logic");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Database connection failed: " + e.getMessage());
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.status(500).body(response);
        }
    }

    // New endpoint to show actual database data
    @GetMapping("/staff-data/{staffId}")
    public ResponseEntity<?> getStaffData(@PathVariable Integer staffId) {
        try {
            var staff = staffRepository.findById(staffId);
            if (staff.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("staffId", staff.get().getStaffId());
                response.put("laneNumber", staff.get().getLaneNumber());
                response.put("city", staff.get().getCity());
                response.put("availability", staff.get().getAvailability());
                response.put("salary", staff.get().getSalary());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}