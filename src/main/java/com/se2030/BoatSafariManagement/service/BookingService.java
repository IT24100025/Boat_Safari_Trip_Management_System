package com.se2030.BoatSafariManagement.service;

import com.se2030.BoatSafariManagement.model.Booking;
import com.se2030.BoatSafariManagement.model.Customer;
import com.se2030.BoatSafariManagement.model.Payment;
import com.se2030.BoatSafariManagement.model.Trip;
import com.se2030.BoatSafariManagement.repository.BookingJDBCRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingJDBCRepository bookingRepository;

    public Trip findAvailableTrip(String tripName, LocalDate date, LocalTime time, List<String> destinations) {
        try {
            LocalDateTime requestedDateTime = LocalDateTime.of(date, time);
            
            // Get all future trips
            List<Trip> trips = bookingRepository.findAllTripsAfter(LocalDateTime.now());
            
            for (Trip trip : trips) {
                if (!trip.getTripName().equals(tripName)) continue;
                if (!trip.getDepartureTime().toLocalDate().equals(date) || 
                    !trip.getDepartureTime().toLocalTime().equals(time)) {
                    continue;
                }

                if (trip.getAvailability() > 0) {
                    if (trip.getDestinations() != null && !destinations.isEmpty()) {
                        List<String> tripDestinations = Arrays.asList(trip.getDestinations().split(",\\s*"));
                        boolean allDestinationsMatch = destinations.stream()
                                .allMatch(dest -> tripDestinations.stream()
                                        .anyMatch(tripDest -> tripDest.toLowerCase().contains(dest.toLowerCase())));

                        if (allDestinationsMatch) {
                            System.out.println("Found matching trip: " + trip.getTripName() + " on " + trip.getDepartureTime());
                            return trip;
                        }
                    } else {
                        System.out.println("Found matching trip (no dest check): " + trip.getTripName() + " on " + trip.getDepartureTime());
                        return trip;
                    }
                }
            }
            System.out.println("No matching trip found for " + tripName + " on " + requestedDateTime);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Customer findOrCreateCustomer(String email, String firstName, String lastName, String contact, Object userContact) {
        Optional<Customer> existingCustomer = bookingRepository.findCustomerByEmail(email);
        if (existingCustomer.isPresent()) {
            return existingCustomer.get();
        }

        // Create new customer
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPassword("defaultPassword");
        customer.setRole("Customer");
        
        Customer savedCustomer = bookingRepository.saveCustomer(customer);

        // Create and save UserContact record
        createUserContact(savedCustomer.getUserId(), contact);

        return savedCustomer;
    }

    private void createUserContact(Integer userId, String contact) {
        try {
            String cleanedContact = contact.replaceAll("[^0-9+]", "");
            if (!cleanedContact.isEmpty()) {
                bookingRepository.saveUserContact(userId, cleanedContact);
                System.out.println("Created UserContact: " + cleanedContact + " for user: " + userId);
            }
        } catch (Exception e) {
            System.out.println("Error creating UserContact: " + e.getMessage());
        }
    }

    public Booking createBooking(Customer customer, Trip trip, Integer guests, String specialRequests, String destinations) {
        BigDecimal totalPrice = trip.getBasePrice().multiply(BigDecimal.valueOf(guests));

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setTrip(trip);
        booking.setNumOfGuests(guests);
        booking.setTotalPrice(totalPrice);
        booking.setSpecialRequests(specialRequests);
        booking.setStatus("Confirmed");
        booking.setBookingDate(LocalDateTime.now());
        booking.setBookingTime(LocalTime.now());

        // Update trip availability
        int newAvailability = trip.getAvailability() - guests;
        bookingRepository.updateTripAvailability(trip.getTripId(), newAvailability);

        return bookingRepository.saveBooking(booking);
    }

    public Payment processPayment(Booking booking, String paymentMethod, String cardLastFour) {
        Payment payment = new Payment();
        payment.setBookingId(booking.getBookingId());
        payment.setAmount(booking.getTotalPrice());
        payment.setPaymentMethod(paymentMethod);
        payment.setTransactionId("TXN" + System.currentTimeMillis());
        payment.setStatus("Successful");
        payment.setPaymentDate(LocalDateTime.now());

        return bookingRepository.savePayment(payment);
    }

    public List<Trip> getAllTrips() {
        return bookingRepository.findAllTripsAfter(LocalDateTime.now());
    }

    public Integer getTotalBookedSeats(Integer tripId, LocalDate date) {
        return bookingRepository.getTotalBookedSeatsForTrip(tripId, date);
    }

    public String initializeSampleData() {
        return "Initialization disabled – using manual December trips";
    }
}