package com.se2030.BoatSafariManagement.repository;

import com.se2030.BoatSafariManagement.model.Trip;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TripRepository {
    private final DataSource dataSource;

    public TripRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Trip> findAll() {
        List<Trip> trips = new ArrayList<>();
        String sql = "SELECT * FROM Trip ORDER BY DepartureTime";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                trips.add(mapRowToTrip(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching trips", e);
        }
        return trips;
    }

    public Trip findById(Integer tripId) {
        String sql = "SELECT * FROM Trip WHERE TripId = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tripId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToTrip(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching trip by ID", e);
        }
        return null;
    }

    public Trip save(Trip trip) {
        String sql = "INSERT INTO Trip (TripName, Duration, DepartureTime, Destinations, Availability, Description, BasePrice) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, trip.getTripName());
            stmt.setInt(2, trip.getDuration());
            stmt.setTimestamp(3, Timestamp.valueOf(trip.getDepartureTime()));
            stmt.setString(4, trip.getDestinations());
            stmt.setInt(5, trip.getAvailability());
            stmt.setString(6, trip.getDescription());
            stmt.setDouble(7, trip.getBasePrice());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating trip failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    trip.setTripId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating trip failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving trip", e);
        }
        return trip;
    }

    public Trip update(Trip trip) {
        String sql = "UPDATE Trip SET TripName = ?, Duration = ?, DepartureTime = ?, Destinations = ?, " +
                "Availability = ?, Description = ?, BasePrice = ? WHERE TripId = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trip.getTripName());
            stmt.setInt(2, trip.getDuration());
            stmt.setTimestamp(3, Timestamp.valueOf(trip.getDepartureTime()));
            stmt.setString(4, trip.getDestinations());
            stmt.setInt(5, trip.getAvailability());
            stmt.setString(6, trip.getDescription());
            stmt.setDouble(7, trip.getBasePrice());
            stmt.setInt(8, trip.getTripId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating trip failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating trip", e);
        }
        return trip;
    }

    public boolean delete(Integer tripId) {
        String sql = "DELETE FROM Trip WHERE TripId = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tripId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting trip", e);
        }
    }

    public List<Trip> findTripsByDateRange(LocalDateTime start, LocalDateTime end) {
        List<Trip> trips = new ArrayList<>();
        String sql = "SELECT * FROM Trip WHERE DepartureTime BETWEEN ? AND ? ORDER BY DepartureTime";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(start));
            stmt.setTimestamp(2, Timestamp.valueOf(end));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    trips.add(mapRowToTrip(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching trips by date range", e);
        }
        return trips;
    }

    private Trip mapRowToTrip(ResultSet rs) throws SQLException {
        return new Trip(
                rs.getInt("TripId"),
                rs.getString("TripName"),
                rs.getInt("Duration"),
                rs.getTimestamp("DepartureTime").toLocalDateTime(),
                rs.getString("Destinations"),
                rs.getInt("Availability"),
                rs.getString("Description"),
                rs.getDouble("BasePrice")
        );
    }
}