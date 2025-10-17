package com.se2030.BoatSafariManagement.repository;

import com.se2030.BoatSafariManagement.model.MaintenanceLog;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MaintenanceRepository {
    private final DataSource dataSource;

    public MaintenanceRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isBoatUnderMaintenance(Integer boatId, LocalDateTime checkTime) {
        String sql = "SELECT COUNT(*) FROM MaintenanceLog " +
                "WHERE BoatId = ? AND Status IN ('Reported', 'In Progress') " +
                "AND ? BETWEEN DateReported AND COALESCE(DateResolved, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, boatId);
            stmt.setTimestamp(2, Timestamp.valueOf(checkTime));
            stmt.setTimestamp(3, Timestamp.valueOf(checkTime.plusDays(1))); // Default end if null

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking boat maintenance", e);
        }
        return false;
    }

    public List<MaintenanceLog> findActiveMaintenanceByBoat(Integer boatId) {
        List<MaintenanceLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM MaintenanceLog " +
                "WHERE BoatId = ? AND Status IN ('Reported', 'In Progress') " +
                "ORDER BY DateReported DESC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, boatId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    logs.add(mapRowToMaintenanceLog(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching active maintenance logs", e);
        }
        return logs;
    }

    private MaintenanceLog mapRowToMaintenanceLog(ResultSet rs) throws SQLException {
        Timestamp dateResolved = rs.getTimestamp("DateResolved");

        return new MaintenanceLog(
                rs.getInt("MaintenanceId"),
                rs.getInt("BoatId"),
                rs.getInt("ReportedBy_AdminId"),
                rs.getString("IssueDescription"),
                rs.getTimestamp("DateReported").toLocalDateTime(),
                dateResolved != null ? dateResolved.toLocalDateTime() : null,
                rs.getDouble("Cost"),
                rs.getString("Status")
        );
    }
}