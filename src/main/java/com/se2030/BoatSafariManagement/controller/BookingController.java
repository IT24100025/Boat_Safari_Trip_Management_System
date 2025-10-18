package com.se2030.BoatSafariManagement.controller;

import com.se2030.BoatSafariManagement.model.BookingDetails;
import com.se2030.BoatSafariManagement.service.StaffService;
import org.springframework.ui.Model;
import com.se2030.BoatSafariManagement.model.Boat;
import com.se2030.BoatSafariManagement.model.Booking;
import com.se2030.BoatSafariManagement.model.Staff;
import com.se2030.BoatSafariManagement.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     assign a guide, driver, and boat to a booking.
     */
    @GetMapping("/{id}/assignStaff")
    public String showAssignForm(@PathVariable("id") int bookingId, Model model) {
        Booking booking = bookingService.getBooking(bookingId);
        List<Staff> guides  = bookingService.getGuides("Guide");
        List<Staff> drivers = bookingService.getDrivers("Driver");
        List<Boat> boats     = bookingService.getAvailableBoats();

        model.addAttribute("booking", booking);
        model.addAttribute("guides",   guides);
        model.addAttribute("drivers",  drivers);
        model.addAttribute("boats",    boats);

        return "assignStaff";
    }

    /**
     * Handle the form submission to assign staff and boat.
     */
    @PostMapping("/{id}/assignStaff")
    public String assignAll(@PathVariable("id") int bookingId,
                            @RequestParam("guideId") int guideId,
                            @RequestParam("driverId") int driverId,
                            @RequestParam("boatId")   int boatId) {

        bookingService.assignStaff(bookingId, guideId, driverId);
        bookingService.assignBoat(bookingId, boatId);
        bookingService.updateAvailability(guideId, driverId, boatId);
        bookingService.updateBookingStatus(bookingId);

        // Redirect back to booking detail page
        return "redirect:/" + bookingId + "/showBookingDetail";
    }

    @GetMapping("/{id}/showBookingDetail")
    public String showBookingDetail(@PathVariable("id") int bookingId, Model model) {
        Booking booking = bookingService.getBooking(bookingId);

        model.addAttribute("booking", booking);
        return "showBookingDetail";
    }

    // List all bookings
    @GetMapping("/showAllBookings")
    public String listAllBookings(Model model) {
        List<BookingDetails> bookingDetails = bookingService.getAllBookings();
        int todayBookingCount = bookingService.getTodayBookingCount();
        int unassignedCount = bookingService.getUnassignedBookingCount();

        model.addAttribute("bookingDetails", bookingDetails);
        model.addAttribute("todayBookingCount", todayBookingCount);
        model.addAttribute("unassignedCount",   unassignedCount);

        return "showAllBookings";    // resolves to showAllBookings.html
    }

}