package com.se2030.BoatSafariManagement.controller;

import com.se2030.BoatSafariManagement.model.User;
import com.se2030.BoatSafariManagement.model.Feedback;
import com.se2030.BoatSafariManagement.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/feedback")  // ADD THIS LINE
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping
    public String feedbackPage(@SessionAttribute(name = "user", required = false) User user,
                               @RequestParam(required = false) Integer customerId,
                               Model model) {
        // Allow access for both authenticated users and customers
        // if (user == null && customerId == null) {
        //   return "redirect:/login";
        // }

        List<Feedback> feedbacks;

        // If customer ID is provided (customer login), show only their feedback
        if (customerId != null) {
            feedbacks = feedbackService.getFeedbackByCustomerId(customerId);
            model.addAttribute("currentCustomerId", customerId);
        }
        // If admin user, show all feedback
        else if (user != null && "Admin".equals(user.getRole())) {
            feedbacks = feedbackService.getAllFeedback();
            model.addAttribute("isAdmin", true);
        }
        // If regular user, show their feedback
        else if (user != null) {
            feedbacks = feedbackService.getFeedbackByCustomerId(user.getUserId());
            model.addAttribute("currentCustomerId", user.getUserId());
        }
        // Fallback
        else {
            feedbacks = new ArrayList<>();
        }

        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("feedbackList", feedbacks);

        // Add empty feedback object for form
        model.addAttribute("feedback", new Feedback());

        // Calculate average rating (only for admin view)
        if (user != null && "Admin".equals(user.getRole()) || customerId == null) {
            double averageRating = 0.0;
            if (!feedbacks.isEmpty()) {
                averageRating = feedbacks.stream()
                        .mapToInt(Feedback::getRating)
                        .average()
                        .orElse(0.0);
            }
            model.addAttribute("averageRating", averageRating);

            // Calculate recent feedback count (last 7 days)
            long recentFeedbackCount = feedbacks.stream()
                    .filter(feedback -> {
                        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
                        return feedback.getSubmittedDate() != null &&
                                feedback.getSubmittedDate().isAfter(weekAgo);
                    })
                    .count();
            model.addAttribute("recentFeedbackCount", recentFeedbackCount);

            // Calculate statistics (only for admin)
            if (user != null && "Admin".equals(user.getRole())) {
                try {
                    var stats = feedbackService.getFeedbackStatistics();
                    model.addAttribute("stats", stats);

                    // Add rating distribution
                    model.addAttribute("rating1Count", feedbackService.getFeedbackCountByRating(1));
                    model.addAttribute("rating2Count", feedbackService.getFeedbackCountByRating(2));
                    model.addAttribute("rating3Count", feedbackService.getFeedbackCountByRating(3));
                    model.addAttribute("rating4Count", feedbackService.getFeedbackCountByRating(4));
                    model.addAttribute("rating5Count", feedbackService.getFeedbackCountByRating(5));
                } catch (Exception e) {
                    System.err.println("Error loading statistics: " + e.getMessage());
                }
            }
        }

        return "single-page"; // Make sure this matches your HTML file name
    }

    @PostMapping("/create")
    public String createFeedback(@ModelAttribute Feedback feedback) {
        try {
            System.out.println("Received feedback: " + feedback);
            System.out.println("Customer ID: " + feedback.getCustomerId());
            System.out.println("Trip ID: " + feedback.getTripId());
            System.out.println("Rating: " + feedback.getRating());
            System.out.println("Comment: " + feedback.getComment());

            feedbackService.createFeedback(feedback);
            return "redirect:/feedback?success=true";
        } catch (Exception e) {
            System.err.println("Error creating feedback: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/feedback?error=creation_failed";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateFeedback(@PathVariable Integer id, @ModelAttribute Feedback feedbackDetails,
                                 @RequestParam(required = false) Integer customerId) {
        // Check if customer is trying to edit their own feedback
        if (customerId != null) {
            var existingFeedback = feedbackService.getFeedbackById(id);
            if (existingFeedback.isPresent() && !existingFeedback.get().getCustomerId().equals(customerId)) {
                return "redirect:/feedback?error=unauthorized&customerId=" + customerId;
            }
        }

        var result = feedbackService.updateFeedback(id, feedbackDetails);
        if (result.isPresent()) {
            String redirectUrl = customerId != null ? "/feedback?customerId=" + customerId : "/feedback";
            return "redirect:" + redirectUrl + "&success=true";
        } else {
            String redirectUrl = customerId != null ? "/feedback?error=notfound&customerId=" + customerId : "/feedback?error=notfound";
            return "redirect:" + redirectUrl;
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteFeedback(@PathVariable Integer id, @RequestParam(required = false) Integer customerId) {
        // Check if customer is trying to delete their own feedback
        if (customerId != null) {
            var existingFeedback = feedbackService.getFeedbackById(id);
            if (existingFeedback.isPresent() && !existingFeedback.get().getCustomerId().equals(customerId)) {
                return "redirect:/feedback?error=unauthorized&customerId=" + customerId;
            }
        }

        boolean deleted = feedbackService.deleteFeedback(id);
        String redirectUrl = customerId != null ? "/feedback?customerId=" + customerId : "/feedback";
        return "redirect:" + redirectUrl + (deleted ? "&success=true" : "&error=delete_failed");
    }
    // AJAX endpoint to get feedback data for editing
    @GetMapping("/{id}/data")
    @ResponseBody
    public Feedback getFeedbackData(@PathVariable Integer id, @RequestParam(required = false) Integer customerId) {
        System.out.println("Fetching feedback data for ID: " + id + ", customerId: " + customerId);

        var feedback = feedbackService.getFeedbackById(id);
        if (feedback.isPresent()) {
            // Check if customer is trying to access their own feedback
            if (customerId != null && !feedback.get().getCustomerId().equals(customerId)) {
                System.out.println("Unauthorized access: Customer " + customerId + " trying to access feedback from customer " + feedback.get().getCustomerId());
                return null; // Return null if not authorized
            }
            System.out.println("Returning feedback: " + feedback.get());
            return feedback.get();
        }
        System.out.println("Feedback not found for ID: " + id);
        return null;
    }

    // Health check endpoint
    @GetMapping("/health")
    @ResponseBody
    public String healthCheck() {
        try {
            var feedbackList = feedbackService.getAllFeedback();
            return "Database connection OK. Found " + feedbackList.size() + " feedback entries.";
        } catch (Exception e) {
            return "Database connection failed: " + e.getMessage();
        }
    }

    // Test endpoint to create a simple feedback
    @GetMapping("/test-create")
    @ResponseBody
    public String testCreateFeedback() {
        try {
            Feedback testFeedback = new Feedback();
            testFeedback.setCustomerId(999);
            testFeedback.setTripId(999);
            testFeedback.setBookingId(999);
            testFeedback.setRating(5);
            testFeedback.setComment("Test feedback");
            testFeedback.setSubmittedDate(LocalDateTime.now());

            Feedback saved = feedbackService.createFeedback(testFeedback);
            return "Test feedback created successfully with ID: " + saved.getFeedbackId();
        } catch (Exception e) {
            return "Test feedback creation failed: " + e.getMessage();
        }
    }
}