package com.se2030.BoatSafariManagement.repository;

import com.se2030.BoatSafariManagement.model.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class FeedbackRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Feedback> feedbackRowMapper = (rs, rowNum) -> {
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(rs.getInt("FeedbackId"));
        feedback.setCustomerId(rs.getInt("CustomerId"));
        feedback.setTripId(rs.getInt("TripId"));
        feedback.setBookingId(rs.getInt("BookingId"));
        feedback.setRating(rs.getInt("Rating"));
        feedback.setComment(rs.getString("Comment"));
        feedback.setSubmittedDate(rs.getObject("SubmittedDate", LocalDateTime.class));
        return feedback;
    };

    public List<Feedback> findAll() {
        String sql = "SELECT * FROM Feedback ORDER BY SubmittedDate DESC";
        return jdbcTemplate.query(sql, feedbackRowMapper);
    }

    public Optional<Feedback> findById(Integer id) {
        String sql = "SELECT * FROM Feedback WHERE FeedbackId = ?";
        try {
            Feedback feedback = jdbcTemplate.queryForObject(sql, feedbackRowMapper, id);
            return Optional.ofNullable(feedback);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Feedback> findByCustomerId(Integer customerId) {
        String sql = "SELECT * FROM Feedback WHERE CustomerId = ? ORDER BY SubmittedDate DESC";
        return jdbcTemplate.query(sql, feedbackRowMapper, customerId);
    }

    public Feedback save(Feedback feedback) {
        if (feedback.getFeedbackId() == null) {
            // Insert new feedback
            String sql = "INSERT INTO Feedback (CustomerId, TripId, BookingId, Rating, Comment, SubmittedDate) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, feedback.getCustomerId());
                ps.setInt(2, feedback.getTripId());
                ps.setInt(3, feedback.getBookingId());
                ps.setInt(4, feedback.getRating());
                ps.setString(5, feedback.getComment());
                ps.setObject(6, feedback.getSubmittedDate());
                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            if (key != null) {
                feedback.setFeedbackId(key.intValue());
            }
        } else {
            // Update existing feedback
            String sql = "UPDATE Feedback SET Rating = ?, Comment = ? WHERE FeedbackId = ?";
            jdbcTemplate.update(sql, feedback.getRating(), feedback.getComment(),
                    feedback.getFeedbackId());
        }
        return feedback;
    }

    public void deleteById(Integer id) {
        String sql = "DELETE FROM Feedback WHERE FeedbackId = ?";
        jdbcTemplate.update(sql, id);
    }

    public Double getAverageRatingByTripId(Integer tripId) {
        String sql = "SELECT AVG(Rating) FROM Feedback WHERE TripId = ?";
        return jdbcTemplate.queryForObject(sql, Double.class, tripId);
    }

    public Integer getFeedbackCountByRating(Integer rating) {
        String sql = "SELECT COUNT(*) FROM Feedback WHERE Rating = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, rating);
    }

    public FeedbackStats getFeedbackStatistics() {
        String sql = "SELECT " +
                "COUNT(*) as totalFeedback, " +
                "AVG(Rating) as averageRating, " +
                "COUNT(DISTINCT CustomerId) as uniqueCustomers, " +
                "COUNT(DISTINCT TripId) as tripsWithFeedback " +
                "FROM Feedback";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new FeedbackStats(
                        rs.getInt("totalFeedback"),
                        rs.getDouble("averageRating"),
                        rs.getInt("uniqueCustomers"),
                        rs.getInt("tripsWithFeedback")
                ));
    }

    public static class FeedbackStats {
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