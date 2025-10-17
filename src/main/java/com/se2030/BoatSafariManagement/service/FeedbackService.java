package com.se2030.BoatSafariManagement.service;

import com.se2030.BoatSafariManagement.model.Feedback;
import com.se2030.BoatSafariManagement.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    public Optional<Feedback> getFeedbackById(Integer id) {
        return feedbackRepository.findById(id);
    }

    public List<Feedback> getFeedbackByCustomerId(Integer customerId) {
        return feedbackRepository.findByCustomerId(customerId);
    }

    public Feedback createFeedback(Feedback feedback) {
        System.out.println("Creating feedback: " + feedback);
        
        // Validate required fields
        if (feedback.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer ID is required");
        }
        if (feedback.getTripId() == null) {
            throw new IllegalArgumentException("Trip ID is required");
        }
        if (feedback.getBookingId() == null) {
            throw new IllegalArgumentException("Booking ID is required");
        }
        if (feedback.getRating() == null || feedback.getRating() < 1 || feedback.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        if (feedback.getComment() == null || feedback.getComment().trim().isEmpty()) {
            throw new IllegalArgumentException("Comment is required");
        }

        if (feedback.getSubmittedDate() == null) {
            feedback.setSubmittedDate(LocalDateTime.now());
        }

        if (feedback.getCountry() == null || feedback.getCountry().isEmpty()) {
            feedback.setCountry("Sri Lanka");
        }

        try {
            return feedbackRepository.save(feedback);
        } catch (Exception e) {
            System.err.println("Error saving feedback: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save feedback: " + e.getMessage(), e);
        }
    }

    public Optional<Feedback> updateFeedback(Integer id, Feedback feedbackDetails) {
        Optional<Feedback> existingFeedback = feedbackRepository.findById(id);

        if (existingFeedback.isPresent()) {
            Feedback feedback = existingFeedback.get();

            if (feedbackDetails.getRating() != null) {
                if (feedbackDetails.getRating() < 1 || feedbackDetails.getRating() > 5) {
                    throw new IllegalArgumentException("Rating must be between 1 and 5");
                }
                feedback.setRating(feedbackDetails.getRating());
            }
            if (feedbackDetails.getComment() != null) {
                feedback.setComment(feedbackDetails.getComment());
            }
            if (feedbackDetails.getCity() != null) {
                feedback.setCity(feedbackDetails.getCity());
            }
            if (feedbackDetails.getCountry() != null) {
                feedback.setCountry(feedbackDetails.getCountry());
            }

            return Optional.of(feedbackRepository.save(feedback));
        }

        return Optional.empty();
    }

    public boolean deleteFeedback(Integer id) {
        try {
            feedbackRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Double getAverageRatingForTrip(Integer tripId) {
        return feedbackRepository.getAverageRatingByTripId(tripId);
    }

    public Integer getFeedbackCountByRating(Integer rating) {
        return feedbackRepository.getFeedbackCountByRating(rating);
    }

    public FeedbackRepository.FeedbackStats getFeedbackStatistics() {
        return feedbackRepository.getFeedbackStatistics();
    }
}