package com.se2030.BoatSafariManagement.controller;

import com.se2030.BoatSafariManagement.model.User;
import com.se2030.BoatSafariManagement.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    @GetMapping("/maintenance")
    public String maintenancePage(@SessionAttribute(name = "user", required = false) User user, Model model) {
        if (user == null || !"Admin".equals(user.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("maintenances", maintenanceService.getAllMaintenanceLogs());
        return "maintenance";
    }

    @PostMapping("/maintenance/update/{id}")
    public String updateMaintenance(@PathVariable int id, @RequestParam String status) {
        maintenanceService.updateMaintenanceStatus(id, status);
        return "redirect:/maintenance";
    }
}