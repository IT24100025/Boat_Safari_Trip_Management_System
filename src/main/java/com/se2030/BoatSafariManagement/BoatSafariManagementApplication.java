package com.se2030.BoatSafariManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
public class BoatSafariManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoatSafariManagementApplication.class, args);
    }
}

// Simple home controller
@Controller
class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/feedback";
    }
    
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Application is running successfully!";
    }
}