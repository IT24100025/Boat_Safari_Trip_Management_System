package com.se2030.BoatSafariManagement.controller;

import com.se2030.BoatSafariManagement.model.Staff;
import com.se2030.BoatSafariManagement.model.User;
import com.se2030.BoatSafariManagement.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class StaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping("/staff")
    public String staffPage(@SessionAttribute(name = "user", required = false) User user, Model model) {
        if (user == null || !"Admin".equals(user.getRole())) {
            return "redirect:/login";
        }
        List<Staff> staffList = staffService.getAllStaff();
        model.addAttribute("staffList", staffList);
        return "staff";
    }

    @PostMapping("/staff/add")
    public String addStaff(@RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String email,
                           @RequestParam BigDecimal salary,
                           @RequestParam String laneNumber,
                           @RequestParam String city,
                           @RequestParam(defaultValue = "true") Boolean availability,
                           @RequestParam String role) {
        Staff staff = new Staff();
        staff.setFirstName(firstName);
        staff.setLastName(lastName);
        staff.setEmail(email);
        staff.setSalary(salary);
        staff.setLaneNumber(laneNumber);
        staff.setCity(city);
        staff.setAvailability(availability);
        staff.setRole(role);

        staffService.saveStaff(staff, role);
        return "redirect:/staff";
    }

    @PostMapping("/staff/update/{id}")
    public String updateStaff(@PathVariable int id,
                              @RequestParam String firstName,
                              @RequestParam String lastName,
                              @RequestParam String email,
                              @RequestParam BigDecimal salary,
                              @RequestParam String laneNumber,
                              @RequestParam String city,
                              @RequestParam(required = false) Boolean availability) {
        Staff staff = staffService.getStaffById(id);
        if (staff != null) {
            staff.setFirstName(firstName);
            staff.setLastName(lastName);
            staff.setEmail(email);
            staff.setSalary(salary);
            staff.setLaneNumber(laneNumber);
            staff.setCity(city);
            staff.setAvailability(availability != null ? availability : false);
            staffService.updateStaff(staff);
        }
        return "redirect:/staff";
    }

    @PostMapping("/staff/delete/{id}")
    public String deleteStaff(@PathVariable int id) {
        staffService.deleteStaff(id);
        return "redirect:/staff";
    }
}