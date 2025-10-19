package com.se2030.BoatSafariManagement.repository;

import com.se2030.BoatSafariManagement.model.Boat;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BoatRepository {
    private final DataSource dataSource;

    public BoatRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Boat> findAvailableBoats(LocalDateTime departureTime, Integer duration) {
        List<Boat> boats = new ArrayList<>();
        String sql = "SELECT b.* FROM Boat b " +
                "WHERE b.BoatAvailability = 1 " +
                "AND b.Status = 'Active' " +
                "AND b.BoatId NOT IN (" +
                "   SELECT ba.AssignedBoatId FROM Booking ba " +
                "   JOIN Trip t ON ba.TripId = t.TripId " +
                "   WHERE ba.AssignedBoatId IS NOT NULL " +
                "   AND ba.Status IN ('Confirmed', 'Pending') " +
                "   AND t.DepartureTime BETWEEN ? AND ?" +
                ") " +
                "AND b.BoatId NOT IN (" +
                "   SELECT ml.BoatId FROM MaintenanceLog ml " +
                "   WHERE ml.Status IN ('Reported', 'In Progress') " +
                "   AND ? BETWEEN ml.DateReported AND COALESCE(ml.DateResolved, ?)" +
                ")";

        LocalDateTime tripEnd = departureTime.plusMinutes(duration);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(departureTime.minusMinutes(30)));
            stmt.setTimestamp(2, Timestamp.valueOf(tripEnd.plusMinutes(30)));
            stmt.setTimestamp(3, Timestamp.valueOf(departureTime));
            stmt.setTimestamp(4, Timestamp.valueOf(tripEnd));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    boats.add(mapRowToBoat(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching available boats", e);
        }
        return boats;
    }

    public Boat findById(Integer boatId) {
        String sql = "SELECT * FROM Boat WHERE BoatId = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, boatId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToBoat(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching boat by ID", e);
        }
        return null;
    }

    public List<Boat> findAll() {
        List<Boat> boats = new ArrayList<>();
        String sql = "SELECT * FROM Boat ORDER BY BoatName";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                boats.add(mapRowToBoat(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all boats", e);
        }
        return boats;
    }

    private Boat mapRowToBoat(ResultSet rs) throws SQLException {
        return new Boat(
                rs.getInt("BoatId"),
                rs.getInt("BoatOwnerId"),
                rs.getString("BoatName"),
                rs.getString("BoatType"),
                rs.getInt("Capacity"),
                rs.getBoolean("BoatAvailability"),
                rs.getString("Status")
        );
    }
}