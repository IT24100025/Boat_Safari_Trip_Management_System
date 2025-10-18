package com.se2030.BoatSafariManagement.repository;

import com.se2030.BoatSafariManagement.model.MaintenanceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MaintenanceRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<MaintenanceLog> maintenanceRowMapper = (rs, rowNum) -> {
        MaintenanceLog maintenance = new MaintenanceLog();
        maintenance.setMaintenanceId(rs.getInt("MaintenanceId"));
        maintenance.setBoatId(rs.getInt("BoatId"));
        maintenance.setReportedByAdminId(rs.getInt("ReportedBy_AdminId"));
        maintenance.setIssueDescription(rs.getString("IssueDescription"));
        maintenance.setDateReported(rs.getObject("DateReported", LocalDateTime.class));
        maintenance.setDateResolved(rs.getObject("DateResolved", LocalDateTime.class));
        maintenance.setCost(rs.getBigDecimal("Cost"));
        maintenance.setStatus(rs.getString("Status"));
        return maintenance;
    };

    public List<MaintenanceLog> findAll() {
        String sql = "SELECT * FROM MaintenanceLog ORDER BY DateReported DESC";
        return jdbcTemplate.query(sql, maintenanceRowMapper);
    }

    public void save(MaintenanceLog maintenanceLog) {
        String sql = "INSERT INTO MaintenanceLog (BoatId, ReportedBy_AdminId, IssueDescription, DateReported, DateResolved, Cost, Status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                maintenanceLog.getBoatId(),
                maintenanceLog.getReportedByAdminId(),
                maintenanceLog.getIssueDescription(),
                maintenanceLog.getDateReported(),
                maintenanceLog.getDateResolved(),
                maintenanceLog.getCost(),
                maintenanceLog.getStatus());
    }

    public void updateStatus(int id, String status) {
        if ("Resolved".equals(status)) {
            jdbcTemplate.update("UPDATE MaintenanceLog SET Status = ?, DateResolved = GETDATE() WHERE MaintenanceId = ?", status, id);
        } else {
            jdbcTemplate.update("UPDATE MaintenanceLog SET Status = ? WHERE MaintenanceId = ?", status, id);
        }
    }
}