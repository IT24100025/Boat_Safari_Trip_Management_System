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

    // Add this method to fix the error
    public List<MaintenanceLog> findAll() {
        List<MaintenanceLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM MaintenanceLog ORDER BY DateReported DESC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                logs.add(mapRowToMaintenanceLog(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all maintenance logs", e);
        }
        return logs;
    }

    // Add this method for saving new maintenance logs
    public MaintenanceLog save(MaintenanceLog maintenanceLog) {
        String sql = "INSERT INTO MaintenanceLog (BoatId, ReportedBy_AdminId, IssueDescription, DateReported, DateResolved, Cost, Status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, maintenanceLog.getBoatId());
            stmt.setInt(2, maintenanceLog.getReportedByAdminId());
            stmt.setString(3, maintenanceLog.getIssueDescription());
            stmt.setTimestamp(4, Timestamp.valueOf(maintenanceLog.getDateReported()));

            if (maintenanceLog.getDateResolved() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(maintenanceLog.getDateResolved()));
            } else {
                stmt.setNull(5, Types.TIMESTAMP);
            }

            // Use BigDecimal instead of double
            stmt.setBigDecimal(6, maintenanceLog.getCost());
            stmt.setString(7, maintenanceLog.getStatus());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        maintenanceLog.setMaintenanceId(generatedKeys.getInt(1));
                    }
                }
            }

            return maintenanceLog;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving maintenance log", e);
        }
    }

    // Add this method for updating status
    public void updateStatus(int id, String status) {
        String sql = "UPDATE MaintenanceLog SET Status = ? WHERE MaintenanceId = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating maintenance status", e);
        }
    }

    // Your existing methods
    public boolean isBoatUnderMaintenance(Integer boatId, LocalDateTime checkTime) {
        String sql = "SELECT COUNT(*) FROM MaintenanceLog " +
                "WHERE BoatId = ? AND Status IN ('Reported', 'In Progress') " +
                "AND ? BETWEEN DateReported AND COALESCE(DateResolved, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, boatId);
            stmt.setTimestamp(2, Timestamp.valueOf(checkTime));
            stmt.setTimestamp(3, Timestamp.valueOf(checkTime.plusDays(1)));

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
                rs.getBigDecimal("Cost"),
                rs.getString("Status")
        );
    }
}