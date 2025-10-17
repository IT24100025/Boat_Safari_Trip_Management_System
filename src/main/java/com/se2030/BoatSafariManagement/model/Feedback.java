package com.se2030.BoatSafariManagement.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Feedback")
public class Feedback {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FeedbackId")
    private Integer feedbackId;
    
    @Column(name = "CustomerId")
    private Integer customerId;
    
    @Column(name = "TripId")
    private Integer tripId;
    
    @Column(name = "BookingId")
    private Integer bookingId;
    
    @Column(name = "Rating")
    private Integer rating;
    
    @Column(name = "Comment")
    private String comment;
    
    @Column(name = "City")
    private String city;
    
    @Column(name = "Country")
    private String country;
    
    @Column(name = "SubmittedDate")
    private LocalDateTime submittedDate;

    // Constructors
    public Feedback() {}

    public Feedback(Integer feedbackId, Integer customerId, Integer tripId, Integer bookingId,
                    Integer rating, String comment, String city, String country, LocalDateTime submittedDate) {
        this.feedbackId = feedbackId;
        this.customerId = customerId;
        this.tripId = tripId;
        this.bookingId = bookingId;
        this.rating = rating;
        this.comment = comment;
        this.city = city;
        this.country = country;
        this.submittedDate = submittedDate;
    }

    // Getters and Setters
    public Integer getFeedbackId() { return feedbackId; }
    public void setFeedbackId(Integer feedbackId) { this.feedbackId = feedbackId; }
    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
    public Integer getTripId() { return tripId; }
    public void setTripId(Integer tripId) { this.tripId = tripId; }
    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public LocalDateTime getSubmittedDate() { return submittedDate; }
    public void setSubmittedDate(LocalDateTime submittedDate) { this.submittedDate = submittedDate; }
}