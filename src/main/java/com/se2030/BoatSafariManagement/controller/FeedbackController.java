package com.se2030.BoatSafariManagement.controller;

import com.se2030.BoatSafariManagement.model.Feedback;
import com.se2030.BoatSafariManagement.service.FeedbackService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public String singlePage(Model model, @RequestParam(required = false) Integer customerId) {
        List<Feedback> feedbackList = feedbackService.getAllFeedback();
        model.addAttribute("feedbackList", feedbackList);
        model.addAttribute("currentCustomerId", customerId);

        var stats = feedbackService.getFeedbackStatistics();
        model.addAttribute("stats", stats);

        // Add empty feedback object for the form
        model.addAttribute("feedback", new Feedback());

        for (int i = 1; i <= 5; i++) {
            model.addAttribute("rating" + i + "Count",
                    feedbackService.getFeedbackCountByRating(i));
        }

        return "single-page";
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
            return "redirect:" + redirectUrl;
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

        feedbackService.deleteFeedback(id);
        String redirectUrl = customerId != null ? "/feedback?customerId=" + customerId : "/feedback";
        return "redirect:" + redirectUrl;
    }

    // AJAX endpoint to get feedback data for editing
    @GetMapping("/{id}/data")
    @ResponseBody
    public Feedback getFeedbackData(@PathVariable Integer id, @RequestParam(required = false) Integer customerId) {
        var feedback = feedbackService.getFeedbackById(id);
        if (feedback.isPresent()) {
            // Check if customer is trying to access their own feedback
            if (customerId != null && !feedback.get().getCustomerId().equals(customerId)) {
                return null; // Return null if not authorized
            }
            return feedback.get();
        }
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
            testFeedback.setCity("Test City");
            testFeedback.setCountry("Test Country");
            testFeedback.setSubmittedDate(LocalDateTime.now());
            
            Feedback saved = feedbackService.createFeedback(testFeedback);
            return "Test feedback created successfully with ID: " + saved.getFeedbackId();
        } catch (Exception e) {
            return "Test feedback creation failed: " + e.getMessage();
        }
    }
}