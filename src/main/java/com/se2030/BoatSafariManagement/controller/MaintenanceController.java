package com.se2030.BoatSafariManagement.controller;

import com.se2030.BoatSafariManagement.model.User;
import com.se2030.BoatSafariManagement.model.MaintenanceLog;
import com.se2030.BoatSafariManagement.service.MaintenanceService;
import com.se2030.BoatSafariManagement.service.BoatService;
import com.se2030.BoatSafariManagement.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/maintenance")
    public String maintenancePage(@SessionAttribute(name = "user", required = false) User user, Model model) {
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
        model.addAttribute("availableBoats", boatService.getAllBoats()); // Changed to getAllBoats()
        return "maintenance";
    }

    @PostMapping("/maintenance/add")
    public String addMaintenance(@SessionAttribute(name = "user", required = false) User user,
                                 @RequestParam int boatId,
                                 @RequestParam String issueDescription,
                                 @RequestParam(required = false) BigDecimal cost,
                                 @RequestParam String status) {
        if (user == null || !"Admin".equals(user.getRole())) {
            return "redirect:/login";
        }

        // Check if maintenance feature is enabled
        if (!appConfig.isMaintenanceEnabled()) {
            return "redirect:/maintenance";
        }

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
        return "redirect:/maintenance";
    }

    @PostMapping("/maintenance/update/{id}")
    public String updateMaintenance(@PathVariable int id, @RequestParam String status) {
        // Check if maintenance feature is enabled
        if (!appConfig.isMaintenanceEnabled()) {
            return "redirect:/maintenance";
        }

        maintenanceService.updateMaintenanceStatus(id, status);
        return "redirect:/maintenance";
    }
}