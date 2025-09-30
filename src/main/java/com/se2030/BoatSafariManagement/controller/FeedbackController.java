package com.se2030.BoatSafariManagement.controller;

import com.se2030.BoatSafariManagement.model.User;
import com.se2030.BoatSafariManagement.model.Feedback;
import com.se2030.BoatSafariManagement.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/feedback")
    public String feedbackPage(@SessionAttribute(name = "user", required = false) User user, Model model) {
        if (user == null || !"Admin".equals(user.getRole())) {
            return "redirect:/login";
        }

        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        model.addAttribute("feedbacks", feedbacks);

        // Calculate average rating in controller
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
        return "feedback";
    }
}