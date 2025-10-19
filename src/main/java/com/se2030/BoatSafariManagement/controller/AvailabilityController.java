package com.se2030.BoatSafariManagement.controller;

import com.se2030.BoatSafariManagement.model.Trip;
import com.se2030.BoatSafariManagement.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AvailabilityController {

    private final BookingService bookingService;

    static class AvailabilityRequest {
        public String tripName;
        public String date;
        public String time;
        public int guests;
        public List<String> destinations;
    }

    @PostMapping("/check-availability")
    public Map<String, Object> checkAvailability(@RequestBody AvailabilityRequest req) {
        System.out.println("Backend Debug - Received: tripName='" + req.tripName + "', date='" + req.date + "', time='" + req.time + "', guests=" + req.guests);

        LocalDate date;
        LocalTime time;
        try {
            date = LocalDate.parse(req.date);
            time = LocalTime.parse(req.time.replace(" AM", "").replace(" PM", ""));
        } catch (Exception e) {
            return Map.of(
                    "available", false,
                    "message", "Invalid date/time format.",
                    "requestedGuests", req.guests
            );
        }

        // Use the service method to find trip
        Trip trip = bookingService.findAvailableTrip(req.tripName, date, time, req.destinations);

        if (trip == null) {
            System.out.println("No matching trip found for " + req.tripName + " on " + date + " at " + time);
            return Map.of(
                    "available", false,
                    "message", "No trip found for the selected name, date, and time.",
                    "requestedGuests", req.guests
            );
        }

        System.out.println("Found matching trip: " + trip.getTripName() + " on " + trip.getDepartureTime());

        // Check destinations
        boolean destMatch = (req.destinations == null || req.destinations.isEmpty()) ||
                req.destinations.stream().allMatch(dest -> trip.getDestinations().toLowerCase().contains(dest.toLowerCase()));
        if (!destMatch) {
            return Map.of(
                    "available", false,
                    "message", "Selected destinations are not available for this trip.",
                    "requestedGuests", req.guests
            );
        }

        // Calculate booked seats using JDBC method
        Integer bookedSeats = bookingService.getTotalBookedSeats(trip.getTripId(), date);
        int availableSeats = trip.getAvailability() - bookedSeats;

        boolean available = availableSeats >= req.guests;
        String message = available ? "Spots available! Proceed to book." : String.format("Only %d seats left (need %d).", availableSeats, req.guests);

        return Map.of(
                "available", available,
                "message", message,
                "trip", Map.of(
                        "tripId", trip.getTripId(),
                        "name", trip.getTripName(),
                        "availability", trip.getAvailability(),
                        "basePrice", trip.getBasePrice(),
                        "description", trip.getDescription()
                ),
                "requestedGuests", req.guests,
                "availableSeats", availableSeats
        );
    }
}