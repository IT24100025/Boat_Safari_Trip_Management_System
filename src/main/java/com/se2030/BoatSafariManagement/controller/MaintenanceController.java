package com.se2030.BoatSafariManagement.controller;

import com.se2030.BoatSafariManagement.model.User;
import com.se2030.BoatSafariManagement.model.MaintenanceLog;
import com.se2030.BoatSafariManagement.service.MaintenanceService;
import com.se2030.BoatSafariManagement.service.BoatService;
import com.se2030.BoatSafariManagement.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Controller
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    @Autowired
    private BoatService boatService;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/maintenance")
    public String maintenancePage(@SessionAttribute(name = "user", required = false) User user,
                                  Model model) {
        if (user == null || !"Admin".equals(user.getRole())) {
            return "redirect:/login";
        }

        // Check if maintenance feature is enabled
        if (!appConfig.isMaintenanceEnabled()) {
            model.addAttribute("error", "Maintenance feature is currently disabled.");
            return "maintenance";
        }

        model.addAttribute("maintenances", maintenanceService.getAllMaintenanceLogs());
        model.addAttribute("maintenanceLog", new MaintenanceLog());
        model.addAttribute("currentAdminId", user.getUserId());
        model.addAttribute("availableBoats", boatService.getAllBoats());
        return "maintenance";
    }

    @PostMapping("/maintenance/add")
    public String addMaintenance(@SessionAttribute(name = "user", required = false) User user,
                                 @RequestParam int boatId,
                                 @RequestParam String issueDescription,
                                 @RequestParam(required = false) BigDecimal cost,
                                 @RequestParam String status,
                                 RedirectAttributes redirectAttributes) {
        if (user == null || !"Admin".equals(user.getRole())) {
            return "redirect:/login";
        }

        // Check if maintenance feature is enabled
        if (!appConfig.isMaintenanceEnabled()) {
            redirectAttributes.addFlashAttribute("error", "Maintenance feature is currently disabled.");
            return "redirect:/maintenance";
        }

        try {
            MaintenanceLog maintenanceLog = new MaintenanceLog();
            maintenanceLog.setBoatId(boatId);
            maintenanceLog.setReportedByAdminId(user.getUserId());
            maintenanceLog.setIssueDescription(issueDescription);
            maintenanceLog.setDateReported(LocalDateTime.now());
            maintenanceLog.setCost(cost != null ? cost : BigDecimal.ZERO);
            maintenanceLog.setStatus(status);

            if ("Resolved".equals(status)) {
                maintenanceLog.setDateResolved(LocalDateTime.now());
            }

            maintenanceService.addMaintenanceLog(maintenanceLog);
            redirectAttributes.addFlashAttribute("success", "Maintenance log added successfully!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add maintenance log: " + e.getMessage());
        }

        return "redirect:/maintenance";
    }

    @PostMapping("/maintenance/update/{id}")
    public String updateMaintenance(@PathVariable int id,
                                    @RequestParam String status,
                                    RedirectAttributes redirectAttributes) {
        // Check if maintenance feature is enabled
        if (!appConfig.isMaintenanceEnabled()) {
            redirectAttributes.addFlashAttribute("error", "Maintenance feature is currently disabled.");
            return "redirect:/maintenance";
        }

        try {
            if ("Resolved".equals(status)) {
                jdbcTemplate.update(
                        "UPDATE MaintenanceLog SET Status = ?, DateResolved = GETDATE() WHERE MaintenanceId = ?",
                        status, id
                );
                redirectAttributes.addFlashAttribute("success",
                        "Maintenance marked as Resolved! Resolution date set.");
            } else {
                jdbcTemplate.update(
                        "UPDATE MaintenanceLog SET Status = ? WHERE MaintenanceId = ?",
                        status, id
                );
                redirectAttributes.addFlashAttribute("success",
                        "Maintenance status updated to: " + status);
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Failed to update maintenance status: " + e.getMessage());
        }

        return "redirect:/maintenance";
    }
}