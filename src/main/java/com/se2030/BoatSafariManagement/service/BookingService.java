package com.se2030.BoatSafariManagement.service;

import com.se2030.BoatSafariManagement.model.Boat;
import com.se2030.BoatSafariManagement.model.Booking;
import com.se2030.BoatSafariManagement.model.BookingDetails;
import com.se2030.BoatSafariManagement.model.Customer;
import com.se2030.BoatSafariManagement.model.Payment;
import com.se2030.BoatSafariManagement.model.Staff;
import com.se2030.BoatSafariManagement.model.Trip;
import com.se2030.BoatSafariManagement.model.BookingDetails;
import com.se2030.BoatSafariManagement.repository.BookingJDBCRepository;
import com.se2030.BoatSafariManagement.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingJDBCRepository bookingRepository;
    private final BookingRepository staffAssignmentRepository;
    private final EmailService emailService;
    private final JdbcTemplate jdbc;

    @Autowired
    public BookingService(BookingJDBCRepository bookingRepository,
                          BookingRepository staffAssignmentRepository,
                          EmailService emailService,
                          JdbcTemplate jdbcTemplate) {
        this.bookingRepository = bookingRepository;
        this.staffAssignmentRepository = staffAssignmentRepository;
        this.emailService = emailService;
        this.jdbc = jdbcTemplate;
    }

    // ========== DEBUG METHOD ==========
    public void debugBookingAssignment(int bookingId) {
        System.out.println("=== DEBUG BOOKING ASSIGNMENT ===");
        try {
            Booking booking = getBooking(bookingId);
            System.out.println("Booking: " + (booking != null ? "FOUND" : "NULL"));

            if (booking != null) {
                System.out.println("Booking ID: " + booking.getBookingId());
                System.out.println("Status: " + booking.getStatus());
                System.out.println("Current Guide: " + booking.getGuideId());
                System.out.println("Current Driver: " + booking.getDriverId());
                System.out.println("Current Boat: " + booking.getBoatId());
                System.out.println("Customer: " + (booking.getCustomer() != null ?
                        booking.getCustomer().getFirstName() + " " + booking.getCustomer().getLastName() : "NULL"));
                System.out.println("Trip: " + (booking.getTrip() != null ?
                        booking.getTrip().getTripName() : "NULL"));
            } else {
                System.out.println("❌ ERROR: Booking with ID " + bookingId + " not found!");
            }

            List<Staff> guides = getGuides("Guide");
            List<Staff> drivers = getDrivers("Driver");
            List<Boat> boats = getAvailableBoats();

            System.out.println("Available Guides: " + guides.size());
            if (guides.isEmpty()) {
                System.out.println("  ❌ No guides available!");
            } else {
                guides.forEach(g -> System.out.println("  - " + g.getFirstName() + " " + g.getLastName() + " (ID: " + g.getStaffId() + ")"));
            }

            System.out.println("Available Drivers: " + drivers.size());
            if (drivers.isEmpty()) {
                System.out.println("  ❌ No drivers available!");
            } else {
                drivers.forEach(d -> System.out.println("  - " + d.getFirstName() + " " + d.getLastName() + " (ID: " + d.getStaffId() + ")"));
            }

            System.out.println("Available Boats: " + boats.size());
            if (boats.isEmpty()) {
                System.out.println("  ❌ No boats available!");
            } else {
                boats.forEach(b -> System.out.println("  - " + b.getBoatName() + " (ID: " + b.getBoatId() + ")"));
            }

        } catch (Exception e) {
            System.out.println("❌ Debug error: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("=== END DEBUG ===");
    }

    // ========== METHODS FROM STAFF_ASSIGNMENT BRANCH ==========

    public Booking getBooking(int id) {
        return staffAssignmentRepository.getBooking(id);
    }

    public List<Boat> getAvailableBoats() {
        return staffAssignmentRepository.getAvailableBoats();
    }

    public void assignBoat(int bookingId, int boatId) {
        staffAssignmentRepository.assignBoatToBooking(bookingId, boatId);
    }

    public List<Staff> getGuides(String guide) {
        return staffAssignmentRepository.getStaffByRole("Guide");
    }

    public List<Staff> getDrivers(String driver) {
        return staffAssignmentRepository.getStaffByRole("Driver");
    }

    @Transactional
    public void assignStaff(int bookingId, int guideId, int driverId, int boatId) {
        try {
            System.out.println("=== STARTING STAFF ASSIGNMENT ===");
            System.out.println("Booking ID: " + bookingId + ", Guide ID: " + guideId + ", Driver ID: " + driverId + ", Boat ID: " + boatId);

            // Get current booking to check if staff are already assigned
            Booking currentBooking = staffAssignmentRepository.getBooking(bookingId);
            if (currentBooking == null) {
                throw new RuntimeException("Booking not found with ID: " + bookingId);
            }

            // Update guide, driver, and boat in a single query
            String sql = "UPDATE Booking SET GuideId = ?, DriverId = ?, BoatId = ?, Status = 'Confirmed' WHERE BookingId = ?";
            int rowsUpdated = jdbc.update(sql, guideId, driverId, boatId, bookingId);
            System.out.println("Rows updated in database: " + rowsUpdated);

            if (rowsUpdated == 0) {
                throw new RuntimeException("No rows updated - booking may not exist");
            }

            // Update availability
            updateAvailability(guideId, driverId, boatId);

            // Get trip details for email
            String tripDetails = getTripDetails(bookingId);

            // Send email to guide if newly assigned
            if (currentBooking.getGuideId() == null || !currentBooking.getGuideId().equals(guideId)) {
                sendStaffNotification(guideId, "Guide", bookingId, tripDetails);
            }

            // Send email to driver if newly assigned
            if (currentBooking.getDriverId() == null || !currentBooking.getDriverId().equals(driverId)) {
                sendStaffNotification(driverId, "Driver", bookingId, tripDetails);
            }

            System.out.println("✅ Staff assignment completed successfully!");

        } catch (Exception e) {
            System.out.println("❌ Error in assignStaff: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to assign staff: " + e.getMessage(), e);
        }
    }

    private String getTripDetails(int bookingId) {
        return staffAssignmentRepository.getTripDetails(bookingId);
    }

    private void sendStaffNotification(int staffId, String role, int bookingId, String tripDetails) {
        try {
            Staff staffInfo = staffAssignmentRepository.getStaffEmailAndName(staffId);
            String email = staffInfo.getEmail();
            String staffName = staffInfo.getFullName();

            if (email != null && !email.trim().isEmpty()) {
                emailService.sendAssignmentNotification(email, staffName, role, bookingId, tripDetails);
                System.out.println("📧 Notification sent to " + role + ": " + email);
            } else {
                System.out.println("⚠️ No email found for " + role + " ID: " + staffId);
            }
        } catch (Exception e) {
            // Log error but don't break the assignment process
            System.err.println("Failed to send notification to staff " + staffId + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateBookingStatus(int bookingId) {
        staffAssignmentRepository.updateBookingStatus(bookingId);
    }

    public List<BookingDetails> getAllBookings() {
        return staffAssignmentRepository.getAllBookings();
    }

    public void updateAvailability(int guideId, int driverId, int boatID){
        staffAssignmentRepository.updateAvailability(guideId, driverId, boatID);
    }

    public int getTodayBookingCount(){
        return staffAssignmentRepository.getTodayBookingCount();
    }

    public int getUnassignedBookingCount(){
        return staffAssignmentRepository.getUnassignedBookingCount();
    }

    // ========== METHODS FROM MERGED BRANCH (ORIGINAL) ==========

    public Trip findAvailableTrip(String tripName, LocalDate date, LocalTime time, List<String> destinations) {
        System.out.println("=== DEBUG: FINDING AVAILABLE TRIP ===");
        System.out.println("Searching for trip with:");
        System.out.println("  Name: '" + tripName + "'");
        System.out.println("  Date: " + date);
        System.out.println("  Time: " + time);
        System.out.println("  Destinations: " + destinations);

        try {
            LocalDateTime requestedDateTime = LocalDateTime.of(date, time);

            // Get all future trips with detailed logging INCLUDING PRICES
            List<Trip> trips = bookingRepository.findAllTripsAfter(LocalDateTime.now());
            System.out.println("All future trips in database (" + trips.size() + "):");
            for (Trip trip : trips) {
                System.out.println("  - ID: " + trip.getTripId() +
                        ", Name: '" + trip.getTripName() + "'" +
                        ", Departure: " + trip.getDepartureTime() +
                        ", Available: " + trip.getAvailability() +
                        ", Base Price: " + trip.getBasePrice() +  // CRITICAL: Added price logging
                        ", Destinations: " + trip.getDestinations());
            }

            for (Trip trip : trips) {
                System.out.println("Checking trip: " + trip.getTripName() +
                        " | Price: " + trip.getBasePrice() +  // Added price here too
                        " | Departure: " + trip.getDepartureTime());

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
                            System.out.println("✓ Found matching trip: " + trip.getTripName() +
                                    " on " + trip.getDepartureTime() +
                                    " with price: " + trip.getBasePrice());
                            return trip;
                        }
                    } else {
                        System.out.println("✓ Found matching trip (no dest check): " + trip.getTripName() +
                                " on " + trip.getDepartureTime() +
                                " with price: " + trip.getBasePrice());
                        return trip;
                    }
                }
            }
            System.out.println("✗ No matching trip found for " + tripName + " on " + requestedDateTime);
            return null;
        } catch (Exception e) {
            System.out.println("ERROR in findAvailableTrip: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Customer findOrCreateCustomer(String email, String firstName, String lastName, String contact, Object userContact) {
        System.out.println("=== DEBUG: FINDING OR CREATING CUSTOMER ===");
        System.out.println("Email: " + email + ", Name: " + firstName + " " + lastName);

        Optional<Customer> existingCustomer = bookingRepository.findCustomerByEmail(email);
        if (existingCustomer.isPresent()) {
            System.out.println("✓ Found existing customer: " + existingCustomer.get().getUserId());
            return existingCustomer.get();
        }

        // Create new customer
        System.out.println("Creating new customer...");
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPassword("defaultPassword");
        customer.setRole("Customer");

        Customer savedCustomer = bookingRepository.saveCustomer(customer);
        System.out.println("✓ Created new customer with ID: " + savedCustomer.getUserId());

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
        System.out.println("=== DEBUG: CREATING BOOKING ===");
        System.out.println("Customer: " + customer.getUserId() +
                ", Trip: " + trip.getTripId() +
                ", Trip Name: " + trip.getTripName() +
                ", Guests: " + guests);

        // DEBUG TRIP PRICE BEFORE CALCULATION
        System.out.println("Trip base price: " + trip.getBasePrice());
        System.out.println("Number of guests: " + guests);

        // Handle null or zero base price
        BigDecimal basePrice = trip.getBasePrice();
        if (basePrice == null) {
            System.out.println("❌ ERROR: Base price is NULL! Using default price of 1000");
            basePrice = new BigDecimal("1000.00");
        } else if (basePrice.compareTo(BigDecimal.ZERO) == 0) {
            System.out.println("⚠️ WARNING: Base price is ZERO! Using default price of 1000");
            basePrice = new BigDecimal("1000.00");
        }

        BigDecimal totalPrice = basePrice.multiply(BigDecimal.valueOf(guests));
        System.out.println("Calculated total price: " + totalPrice);

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
        System.out.println("Updating trip availability from " + trip.getAvailability() + " to " + newAvailability);
        bookingRepository.updateTripAvailability(trip.getTripId(), newAvailability);

        Booking savedBooking = bookingRepository.saveBooking(booking);
        System.out.println("✓ Booking created with ID: " + savedBooking.getBookingId() +
                ", Total Price: " + savedBooking.getTotalPrice());
        return savedBooking;
    }

    public Payment processPayment(Booking booking, String paymentMethod, String cardLastFour) {
        System.out.println("=== DEBUG: PROCESSING PAYMENT ===");
        System.out.println("Booking ID: " + booking.getBookingId() +
                ", Amount: " + booking.getTotalPrice() +
                ", Payment Method: " + paymentMethod);

        Payment payment = new Payment();
        payment.setBookingId(booking.getBookingId());
        payment.setAmount(booking.getTotalPrice());
        payment.setPaymentMethod(paymentMethod);
        payment.setTransactionId("TXN" + System.currentTimeMillis());
        payment.setStatus("Successful");
        payment.setPaymentDate(LocalDateTime.now());

        Payment savedPayment = bookingRepository.savePayment(payment);
        System.out.println("✓ Payment processed: " + savedPayment.getTransactionId() +
                " for amount: " + savedPayment.getAmount());
        return savedPayment;
    }

    public List<Trip> getAllTrips() {
        try {
            List<Trip> trips = bookingRepository.findAllTripsAfter(LocalDateTime.now());
            System.out.println("=== DEBUG: BookingService.getAllTrips() ===");
            System.out.println("Current time: " + LocalDateTime.now());
            System.out.println("Number of trips found: " + trips.size());

            if (trips.isEmpty()) {
                System.out.println("NO TRIPS FOUND! Possible reasons:");
                System.out.println("1. No trips in database");
                System.out.println("2. All trips are in the past");
                System.out.println("3. All trips have availability = 0");
                System.out.println("4. Database connection issue");
            }

            for (Trip trip : trips) {
                System.out.println("Trip: " + trip.getTripName() +
                        " | ID: " + trip.getTripId() +
                        " | Departure: " + trip.getDepartureTime() +
                        " | Available: " + trip.getAvailability() +
                        " | Base Price: " + trip.getBasePrice());  // Added price here
            }
            System.out.println("=== END DEBUG ===");

            return trips;
        } catch (Exception e) {
            System.out.println("ERROR in getAllTrips: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Integer getTotalBookedSeats(Integer tripId, LocalDate date) {
        return bookingRepository.getTotalBookedSeatsForTrip(tripId, date);
    }

    public String initializeSampleData() {
        return "Initialization disabled – using manual December trips";
    }
}