package com.se2030.BoatSafariManagement.repository;

import com.se2030.BoatSafariManagement.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TripRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Trip> tripRowMapper = (rs, rowNum) -> {
        Trip trip = new Trip();
        trip.setTripId(rs.getInt("TripId"));
        trip.setTripName(rs.getString("TripName"));
        trip.setDuration(rs.getInt("Duration"));
        trip.setDepartureTime(rs.getObject("DepartureTime", LocalDateTime.class));
        trip.setDestinations(rs.getString("Destinations"));
        trip.setAvailability(rs.getInt("Availability"));
        trip.setDescription(rs.getString("Description"));
        trip.setBasePrice(rs.getBigDecimal("BasePrice"));
        return trip;
    };

    public List<Trip> findAll() {
        // Show only active and cancelled trips, exclude deleted trips
        String sql = "SELECT * FROM Trip WHERE Description NOT LIKE '%[DELETED%' ORDER BY DepartureTime DESC";
        return jdbcTemplate.query(sql, tripRowMapper);
    }

    public void cancelTrip(int id, String reason) {
        String sql = "UPDATE Trip SET Availability = 0, Description = CONCAT(COALESCE(Description, ''), ?) WHERE TripId = ?";
        jdbcTemplate.update(sql, " [CANCELLED: " + reason + "]", id);
    }

    public void deleteTrip(int id) {
        String checkBookingsSql = "SELECT COUNT(*) FROM Booking WHERE TripId = ?";
        Integer bookingCount = jdbcTemplate.queryForObject(checkBookingsSql, Integer.class, id);

        if (bookingCount != null && bookingCount > 0) {
            // Soft delete - mark as deleted in description
            String softDeleteSql = "UPDATE Trip SET Description = CONCAT(COALESCE(Description, ''), ?) WHERE TripId = ?";
            jdbcTemplate.update(softDeleteSql, " [DELETED]", id);
        } else {
            // Hard delete - remove from database
            jdbcTemplate.update("UPDATE Boat SET TripId = NULL WHERE TripId = ?", id);
            jdbcTemplate.update("DELETE FROM Trip WHERE TripId = ?", id);
        }
    }
}