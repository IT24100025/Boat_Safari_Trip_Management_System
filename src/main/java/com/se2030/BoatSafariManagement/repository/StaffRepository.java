package com.se2030.BoatSafariManagement.repository;

import com.se2030.BoatSafariManagement.model.Staff;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StaffRepository {
    private final DataSource dataSource;

    public StaffRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Staff> findAvailableStaff(LocalDateTime departureTime, Integer duration, String role) {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT s.*, u.FirstName, u.LastName, u.Email, u.Role " +
                "FROM Staff s " +
                "JOIN [User] u ON s.StaffId = u.UserId " +
                "WHERE s.Availability = 1 " +
                "AND u.Role = ? " +
                "AND s.StaffId NOT IN (" +
                "   SELECT ba.AssignedStaffId FROM Booking ba " +
                "   JOIN Trip t ON ba.TripId = t.TripId " +
                "   WHERE ba.AssignedStaffId IS NOT NULL " +
                "   AND ba.Status IN ('Confirmed', 'Pending') " +
                "   AND t.DepartureTime BETWEEN ? AND ?" +
                ")";

        LocalDateTime tripEnd = departureTime.plusMinutes(duration);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role);
            stmt.setTimestamp(2, Timestamp.valueOf(departureTime.minusMinutes(30)));
            stmt.setTimestamp(3, Timestamp.valueOf(tripEnd.plusMinutes(30)));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    staffList.add(mapRowToStaff(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching available staff", e);
        }
        return staffList;
    }

    public Staff findById(Integer staffId) {
        String sql = "SELECT s.*, u.FirstName, u.LastName, u.Email, u.Role " +
                "FROM Staff s " +
                "JOIN [User] u ON s.StaffId = u.UserId " +
                "WHERE s.StaffId = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, staffId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToStaff(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching staff by ID", e);
        }
        return null;
    }

    public List<Staff> findByRole(String role) {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT s.*, u.FirstName, u.LastName, u.Email, u.Role " +
                "FROM Staff s " +
                "JOIN [User] u ON s.StaffId = u.UserId " +
                "WHERE u.Role = ? AND s.Availability = 1 " +
                "ORDER BY u.FirstName, u.LastName";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    staffList.add(mapRowToStaff(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching staff by role", e);
        }
        return staffList;
    }

    private Staff mapRowToStaff(ResultSet rs) throws SQLException {
        return new Staff(
                rs.getInt("StaffId"),
                rs.getString("LaneNumber"),
                rs.getString("City"),
                rs.getBoolean("Availability"),
                rs.getDouble("Salary"),
                rs.getString("Role"),
                rs.getString("FirstName"),
                rs.getString("LastName"),
                rs.getString("Email")
        );
    }
}