package com.se2030.BoatSafariManagement.repository;

import com.se2030.BoatSafariManagement.model.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

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

    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM Feedback WHERE FeedbackId = ?", id);
    }
}