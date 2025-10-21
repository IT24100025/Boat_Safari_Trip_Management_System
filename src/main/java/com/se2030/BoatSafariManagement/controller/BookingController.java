package com.se2030.BoatSafariManagement.controller;

import com.se2030.BoatSafariManagement.dto.BookingRequestDTO;
import com.se2030.BoatSafariManagement.dto.BookingResponseDTO;
import com.se2030.BoatSafariManagement.model.Booking;
import com.se2030.BoatSafariManagement.model.Customer;
import com.se2030.BoatSafariManagement.model.Payment;
import com.se2030.BoatSafariManagement.model.Trip;
import com.se2030.BoatSafariManagement.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BookingController {

    private final BookingService bookingService;

    // Manual constructor
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/")
    public String index(Model model) {
        return "customer-index";
    }

    @PostMapping("/api/book-trip")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> bookTrip(@RequestBody BookingRequestDTO bookingRequest) {
        Map<String, Object> response = new HashMap<>();
        System.out.println("=== BOOKING CONTROLLER DEBUG ===");
        System.out.println("Booking Debug - Request: " + bookingRequest.toString());

        try {
            System.out.println("Processing booking for: " + bookingRequest.getEmail());

            // Find or create customer
            Customer customer = bookingService.findOrCreateCustomer(
                    bookingRequest.getEmail(),
                    bookingRequest.getFirstName(),
                    bookingRequest.getLastName(),
                    bookingRequest.getContact(),
                    null);

            // Find trip
            Trip trip = bookingService.findAvailableTrip(
                    bookingRequest.getTripName(),
                    bookingRequest.getDate(),
                    bookingRequest.getTime(),
                    bookingRequest.getDestinations()
            );
            System.out.println("Booking Debug - Found trip: " + trip);

            // DEBUG: Check trip price before creating booking
            if (trip != null) {
                System.out.println("TRIP PRICE DEBUG - Base Price: " + trip.getBasePrice());
                System.out.println("TRIP PRICE DEBUG - Trip Name: " + trip.getTripName());
                System.out.println("TRIP PRICE DEBUG - Trip ID: " + trip.getTripId());
                System.out.println("TRIP PRICE DEBUG - Availability: " + trip.getAvailability());
            } else {
                System.out.println("TRIP PRICE DEBUG - Trip is NULL!");
            }

            if (trip == null) {
                response.put("success", false);
                response.put("message", "Trip no longer available");
                return ResponseEntity.badRequest().body(response);
            }

            // Create booking
            String destinationsString = String.join(", ", bookingRequest.getDestinations());
            Booking booking = bookingService.createBooking(
                    customer,
                    trip,
                    bookingRequest.getGuests(),
                    bookingRequest.getSpecialRequests(),
                    destinationsString
            );
            System.out.println("Booking Debug - Created Booking with total price: " + booking.getTotalPrice());

            // Process payment
            String cardLastFour = bookingRequest.getCardNo().length() > 4 ?
                    bookingRequest.getCardNo().substring(bookingRequest.getCardNo().length() - 4) :
                    bookingRequest.getCardNo();

            Payment payment = bookingService.processPayment(
                    booking,
                    bookingRequest.getPaymentMethod(),
                    cardLastFour
            );
            System.out.println("Booking Debug - Processed Payment: " + payment);
            System.out.println("Payment Amount: " + payment.getAmount());

            // Prepare response
            BookingResponseDTO bookingResponse = new BookingResponseDTO();
            bookingResponse.setBookingId("RS" + booking.getBookingId());
            bookingResponse.setCustomerName(bookingRequest.getFirstName() + " " + bookingRequest.getLastName());
            bookingResponse.setTripName(bookingRequest.getTripName());
            bookingResponse.setDate(bookingRequest.getDate().toString());
            bookingResponse.setTime(bookingRequest.getTime().toString());
            bookingResponse.setGuests(bookingRequest.getGuests());
            bookingResponse.setDestinations(destinationsString);
            bookingResponse.setTotalAmount(booking.getTotalPrice());
            bookingResponse.setPaymentTime(payment.getPaymentDate().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            bookingResponse.setStatus("Confirmed");

            // DEBUG: Check final response amount
            System.out.println("FINAL RESPONSE DEBUG - Total Amount: " + bookingResponse.getTotalAmount());
            System.out.println("FINAL RESPONSE DEBUG - Booking Total Price: " + booking.getTotalPrice());

            response.put("success", true);
            response.put("booking", bookingResponse);
            response.put("message", "Booking confirmed successfully!");

            System.out.println("✓ Booking successful: " + bookingResponse.getBookingId() +
                    " | Amount: " + bookingResponse.getTotalAmount() +
                    " | Guests: " + bookingResponse.getGuests());
            System.out.println("=== END BOOKING CONTROLLER DEBUG ===");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("❌ Error in bookTrip: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Booking failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/api/trips")
    @ResponseBody
    public ResponseEntity<List<Trip>> getAllTrips() {
        List<Trip> trips = bookingService.getAllTrips();
        System.out.println("Returning " + trips.size() + " trips");
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/api/test")
    @ResponseBody
    public String test() {
        return "BookingController is working!";
    }

    // Add this debug endpoint to check trip prices
    @GetMapping("/api/debug/trip-prices")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> debugTripPrices() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Trip> trips = bookingService.getAllTrips();
            System.out.println("=== DEBUG TRIP PRICES ===");
            System.out.println("Total trips found: " + trips.size());

            for (Trip trip : trips) {
                System.out.println("Trip: " + trip.getTripName() +
                        " | Price: " + trip.getBasePrice() +
                        " | ID: " + trip.getTripId() +
                        " | Available: " + trip.getAvailability());
            }

            response.put("trips", trips);
            response.put("success", true);
            response.put("message", "Found " + trips.size() + " trips");

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}