package com.se2030.BoatSafariManagement.controller;

import com.se2030.BoatSafariManagement.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class AdminController {

    @GetMapping("/dashboard")
    public String dashboard(@SessionAttribute(name = "user", required = false) User user, Model model) {
        if (user == null || !"Admin".equals(user.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Admin Dashboard");
        return "dashboard";
    }
}