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

    // Add this cancelTrip method
    public boolean cancelTrip(int tripId, String reason) {
        String sql = "UPDATE Trip SET Status = 'Cancelled', CancellationReason = ? WHERE TripId = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, reason);
            stmt.setInt(2, tripId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error cancelling trip", e);
        }
    }

    // Update your existing methods to handle the new fields
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
        // Check if Status column exists in the table
        boolean hasStatusColumn = checkIfColumnExists("Status");

        String sql;
        if (hasStatusColumn) {
            sql = "INSERT INTO Trip (TripName, Duration, DepartureTime, Destinations, Availability, Description, BasePrice, Status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO Trip (TripName, Duration, DepartureTime, Destinations, Availability, Description, BasePrice) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, trip.getTripName());
            stmt.setInt(2, trip.getDuration());
            stmt.setTimestamp(3, Timestamp.valueOf(trip.getDepartureTime()));
            stmt.setString(4, trip.getDestinations());
            stmt.setInt(5, trip.getAvailability());
            stmt.setString(6, trip.getDescription());
            stmt.setBigDecimal(7, trip.getBasePrice());

            if (hasStatusColumn) {
                stmt.setString(8, trip.getStatus() != null ? trip.getStatus() : "Scheduled");
            }

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating trip failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1); // Store in primitive first
                    trip.setTripId(generatedId);
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
        // Check if Status column exists
        boolean hasStatusColumn = checkIfColumnExists("Status");

        String sql;
        if (hasStatusColumn) {
            sql = "UPDATE Trip SET TripName = ?, Duration = ?, DepartureTime = ?, Destinations = ?, " +
                    "Availability = ?, Description = ?, BasePrice = ?, Status = ? WHERE TripId = ?";
        } else {
            sql = "UPDATE Trip SET TripName = ?, Duration = ?, DepartureTime = ?, Destinations = ?, " +
                    "Availability = ?, Description = ?, BasePrice = ? WHERE TripId = ?";
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trip.getTripName());
            stmt.setInt(2, trip.getDuration());
            stmt.setTimestamp(3, Timestamp.valueOf(trip.getDepartureTime()));
            stmt.setString(4, trip.getDestinations());
            stmt.setInt(5, trip.getAvailability());
            stmt.setString(6, trip.getDescription());
            stmt.setBigDecimal(7, trip.getBasePrice());

            if (hasStatusColumn) {
                stmt.setString(8, trip.getStatus());
                stmt.setInt(9, trip.getTripId());
            } else {
                stmt.setInt(8, trip.getTripId());
            }

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
        // Handle conflict detection fields - these are calculated, not stored in DB
        boolean hasConflict = false;
        String conflictDetails = null;
        boolean capacityWarning = false;
        String capacityStatus = "AVAILABLE";

        // Calculate capacity status based on availability
        int availability = rs.getInt("Availability");
        if (availability <= 0) {
            capacityStatus = "FULL";
            capacityWarning = true;
        } else if (availability <= 5) {
            capacityStatus = "WARNING";
            capacityWarning = true;
        }

        // Safely get Status column - use default if column doesn't exist
        String status;
        try {
            status = rs.getString("Status");
            if (status == null) {
                status = "Scheduled"; // Default value
            }
        } catch (SQLException e) {
            // If Status column doesn't exist, use default
            status = "Scheduled";
        }

        return new Trip(
                rs.getInt("TripId"),
                rs.getString("TripName"),
                rs.getInt("Duration"),
                rs.getTimestamp("DepartureTime").toLocalDateTime(),
                rs.getString("Destinations"),
                rs.getInt("Availability"),
                rs.getString("Description"),
                rs.getBigDecimal("BasePrice"),
                status, // Use the status value
                hasConflict,
                conflictDetails,
                capacityWarning,
                capacityStatus
        );
    }

    // Helper method to check if a column exists
    private boolean checkIfColumnExists(String columnName) {
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Trip' AND COLUMN_NAME = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, columnName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            // If we can't check, assume the column doesn't exist
            return false;
        }
        return false;
    }
}