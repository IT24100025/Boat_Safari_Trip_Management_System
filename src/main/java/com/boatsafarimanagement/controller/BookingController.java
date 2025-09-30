package com.boatsafarimanagement.controller;

import com.boatsafarimanagement.service.StaffService;
import org.springframework.ui.Model;
import com.boatsafarimanagement.model.Boat;
import com.boatsafarimanagement.model.Booking;
import com.boatsafarimanagement.model.Staff;
import com.boatsafarimanagement.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final StaffService staffService;

    @Autowired
    public BookingController(BookingService bookingService,
                             StaffService staffService) {
        this.bookingService = bookingService;
        this.staffService = staffService;
    }

    /**
     * Show the assignment form where a user can pick
     * a guide, a driver, and an available boat.
     */
    @GetMapping("/{id}/assign")
    public String showAssignForm(@PathVariable("id") int bookingId, Model model) {
        Booking booking = bookingService.getBooking(bookingId);
        List<Staff> guides  = bookingService.getGuides("Guide");
        List<Staff> drivers = bookingService.getDrivers("Driver");
        List<Boat> boats     = bookingService.getAvailableBoats();

        model.addAttribute("booking", booking);
        model.addAttribute("guides",   guides);
        model.addAttribute("drivers",  drivers);
        model.addAttribute("boats",    boats);

        return "Booking/assign";
    }

    /**
     * Handle the form submission to assign staff and boat.
     */
    @PostMapping("/{id}/assign")
    public String assignAll(@PathVariable("id") int bookingId,
                            @RequestParam("guideId") int guideId,
                            @RequestParam("driverId") int driverId,
                            @RequestParam("boatId")   int boatId) {

        bookingService.assignStaff(bookingId, guideId, driverId);
        bookingService.assignBoat(bookingId, boatId);
        bookingService.updateAvailability(guideId, driverId, boatId);

        // Redirect back to booking detail page
        return "redirect:/bookings/" + bookingId;
    }

    @GetMapping("/{id}")
    public String showBookingDetail(@PathVariable("id") int bookingId, Model model) {
        Booking booking = bookingService.getBooking(bookingId);
        if (booking == null) {
            model.addAttribute("errorMessage", "Booking not found for ID: " + bookingId);
            return "error"; // fallback view
        }
        model.addAttribute("booking", booking);
        return "Booking/detail";
    }

    // List all bookings
    @GetMapping("/all")
    public String listAllBookings(Model model) {
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        return "Booking/all";    // resolves to all.html
    }



}


