package com.se2030.BoatSafariManagement.controller;

import com.se2030.BoatSafariManagement.model.User;
import com.se2030.BoatSafariManagement.model.Trip; // Make sure to import Trip model
import com.se2030.BoatSafariManagement.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TripController {

    @Autowired
    private TripService tripService;

    @GetMapping("/trips")
    public String tripsPage(@SessionAttribute(name = "user", required = false) User user, Model model) {
        if (user == null || !"Admin".equals(user.getRole())) {
            return "redirect:/login";
        }

        // Get all trips and add to model
        java.util.List<Trip> trips = tripService.getAllTrips();
        model.addAttribute("trips", trips);

        // Calculate total availability
        int totalAvailability = trips.stream()
                .mapToInt(Trip::getAvailability)
                .sum();
        model.addAttribute("totalAvailability", totalAvailability);

        return "trips";
    }

    @PostMapping("/trips/cancel/{id}")
    public String cancelTrip(@PathVariable int id, @RequestParam String reason) {
        tripService.cancelTrip(id, reason);
        return "redirect:/trips";
    }
}