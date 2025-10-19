package com.se2030.BoatSafariManagement.controller;

import com.se2030.BoatSafariManagement.dto.BookingRequestDTO;
import com.se2030.BoatSafariManagement.dto.BookingResponseDTO;
import com.se2030.BoatSafariManagement.model.Booking;
import com.se2030.BoatSafariManagement.model.Customer;
import com.se2030.BoatSafariManagement.model.Payment;
import com.se2030.BoatSafariManagement.model.Trip;
import com.se2030.BoatSafariManagement.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @PostMapping("/api/book-trip")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> bookTrip(@RequestBody BookingRequestDTO bookingRequest) {
        Map<String, Object> response = new HashMap<>();
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
            System.out.println("Booking Debug - Created Booking: " + booking);

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

            response.put("success", true);
            response.put("booking", bookingResponse);
            response.put("message", "Booking confirmed successfully!");

            System.out.println("Booking successful: " + bookingResponse.getBookingId());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("Error in bookTrip: " + e.getMessage());
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
}