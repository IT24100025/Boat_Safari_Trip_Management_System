package com.se2030.BoatSafariManagement.repository;

import com.se2030.BoatSafariManagement.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    
    List<Feedback> findByCustomerId(Integer customerId);
    
    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.tripId = :tripId")
    Double getAverageRatingByTripId(@Param("tripId") Integer tripId);
    
    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.rating = :rating")
    Integer getFeedbackCountByRating(@Param("rating") Integer rating);
    
    @Query("SELECT COUNT(f) FROM Feedback f")
    Integer getTotalFeedbackCount();
    
    @Query("SELECT AVG(f.rating) FROM Feedback f")
    Double getAverageRating();
    
    @Query("SELECT COUNT(DISTINCT f.customerId) FROM Feedback f")
    Integer getUniqueCustomersCount();
    
    @Query("SELECT COUNT(DISTINCT f.tripId) FROM Feedback f")
    Integer getTripsWithFeedbackCount();
    
    default FeedbackStats getFeedbackStatistics() {
        return new FeedbackStats(
            getTotalFeedbackCount(),
            getAverageRating(),
            getUniqueCustomersCount(),
            getTripsWithFeedbackCount()
        );
    }
    
    class FeedbackStats {
        private final Integer totalFeedback;
        private final Double averageRating;
        private final Integer uniqueCustomers;
        private final Integer tripsWithFeedback;
        
        public FeedbackStats(Integer totalFeedback, Double averageRating, 
                           Integer uniqueCustomers, Integer tripsWithFeedback) {
            this.totalFeedback = totalFeedback;
            this.averageRating = averageRating;
            this.uniqueCustomers = uniqueCustomers;
            this.tripsWithFeedback = tripsWithFeedback;
        }
        
        public Integer getTotalFeedback() { return totalFeedback; }
        public Double getAverageRating() { return averageRating; }
        public Integer getUniqueCustomers() { return uniqueCustomers; }
        public Integer getTripsWithFeedback() { return tripsWithFeedback; }
    }
}
