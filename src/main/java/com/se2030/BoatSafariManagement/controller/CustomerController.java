package com.se2030.BoatSafariManagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerController {

    @GetMapping("/customer-index.html")
    public String customerIndex() {
        return "customer-index"; // This refers to src/main/resources/templates/customer-index.html
    }

    // Optional: Add a default route
    /* @GetMapping("/")
    public String home() {
        return "customer-index";
    }
    */

}