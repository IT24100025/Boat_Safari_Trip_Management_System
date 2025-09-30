package com.se2030.BoatSafariManagement.service;

import com.se2030.BoatSafariManagement.model.Feedback;
import com.se2030.BoatSafariManagement.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public void deleteFeedback(int id) {
        feedbackRepository.deleteById(id);
    }
}